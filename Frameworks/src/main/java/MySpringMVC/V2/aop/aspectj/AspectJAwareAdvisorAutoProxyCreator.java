package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.framework.ProxyFactory;
import MySpringMVC.V2.beans.config.BeanPostProcessor;
import MySpringMVC.V2.context.support.AbstractApplicationContext;
import MySpringMVC.V2.core.CloneUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author SuccessZhang
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor {

    private final AbstractApplicationContext applicationContext;

    private List<AbstractAspectJAdvice> configAdvices;

    public AspectJAwareAdvisorAutoProxyCreator(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        ParserConfig.getGlobalInstance().putDeserializer(AbstractAspectJAdvice.class, new ObjectDeserializer() {
            @SuppressWarnings("unchecked")
            @Override
            public AbstractAspectJAdvice deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                try {
                    AbstractAspectJAdvice result = new AbstractAspectJAdvice() {
                    };
                    Map<String, Object> map = parser.parseObject(Map.class);
                    for (Field field : AbstractAspectJAdvice.class.getDeclaredFields()) {
                        field.setAccessible(true);
                        Object value = map.get(field.getName());
                        Type fieldType = field.getType();
                        if (value instanceof JSONObject) {
                            value = JSON.parseObject(value.toString(), fieldType);
                        }
                        if (fieldType.equals(Class.class)) {
                            field.set(result, Class.forName(value.toString()));
                        } else if (fieldType.equals(AbstractAspectJAdvice.AdviceType.class)) {
                            field.set(result, AbstractAspectJAdvice.AdviceType.getAdviceType(value.toString()));
                        } else {
                            field.set(result, value);
                        }
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public int getFastMatchToken() {
                return 0;
            }
        });
        this.configAdvices = JSON.parseArray(applicationContext.getConfig().getProperty("advisors"), AspectJPointcutAdvisor.class)
                .stream().map(AspectJPointcutAdvisor::getAdvice)
                //将表达式按照优先级、表达式长度排序
                .sorted(new Comparator<AbstractAspectJAdvice>() {
                    @Override
                    public int compare(AbstractAspectJAdvice o1, AbstractAspectJAdvice o2) {
                        return Integer.compare(o2.getDeclarationOrder(), o1.getDeclarationOrder());
                    }
                })
                .sorted(new Comparator<AbstractAspectJAdvice>() {
                    @Override
                    public int compare(AbstractAspectJAdvice o1, AbstractAspectJAdvice o2) {
                        return Integer.compare(o1.getPointcut().getExpression().length(), o2.getPointcut().getExpression().length());
                    }
                }.reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        Class<?> targetClass = bean.getClass();
        List<Object> advices = getAdvicesAndAdvisorsForBean(targetClass, beanName);
        if (advices.isEmpty()) {
            return bean;
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvisors(advices);
        proxyFactory.setTarget(bean);
        proxyFactory.setTargetClass(targetClass);
        proxyFactory.setInterfaces(Arrays.asList(targetClass.getInterfaces()));
        return proxyFactory.getProxy(targetClass.getClassLoader());
    }

    private List<Object> getAdvicesAndAdvisorsForBean(Class<?> targetClass, String beanName) throws Exception {
        List<Object> advices = new LinkedList<>();
        for (AbstractAspectJAdvice config : configAdvices) {
            Map<String, Method> methodMap = new HashMap<>(8);
            for (Method method : config.getDeclaringClass().getDeclaredMethods()) {
                methodMap.put(method.getName(), method);
            }
            AspectJExpressionPointcut pointcut = config.getPointcut();
            boolean match = false;
            String unprocessed = pointcut.getExpression();
            //匹配类修饰部分
            int index = unprocessed.indexOf(" ");
            String proceeding = unprocessed.substring(0, index);
            if (!Pattern.compile(proceeding).matcher(Modifier.toString(targetClass.getModifiers())).matches()) {
                continue;
            }

            List<String> targets = new ArrayList<>();
            targets.add(targetClass.getName());
            for (Class<?> i : targetClass.getInterfaces()) {
                targets.add(i.getName());
            }
            Pattern pattern;
            unprocessed = unprocessed.trim().substring(index + 1);
            String temp = null;
            for (String target : targets) {
                temp = CloneUtils.clone(unprocessed);
                if (temp != null) {
                    while (temp.length() > 0) {
                        index = temp.indexOf(".");
                        proceeding = temp.substring(0, index);
                        if ("".equals(proceeding)) {
                            index = temp.indexOf("..");
                            proceeding = temp.substring(0, index);
                        } else {
                            proceeding = proceeding + ".*";
                        }
                        if (proceeding.contains("(")) {
                            match = true;
                            break;
                        }
                        pattern = Pattern.compile(proceeding);
                        if (pattern.matcher(target).matches()) {
                            temp = temp.substring(index + 1);
                            if (index >= target.length()) {
                                match = true;
                                break;
                            }
                            target = target.substring(index + 1);
                        } else {
                            break;
                        }
                    }
                }
                if (match) {
                    break;
                }
            }
            if (!match) {
                continue;
            }
            unprocessed = temp;
            index = unprocessed.indexOf("(");
            Pattern methodName = Pattern.compile(unprocessed.substring(0, index));
            pattern = Pattern.compile(unprocessed.substring(index + 1, unprocessed.length() - 1)
                    .replaceAll("\\.\\.", ".*"));
            for (Method method : targetClass.getDeclaredMethods()) {
                if (methodName.matcher(method.getName()).matches()) {
                    //方法名能匹配上
                    StringBuilder full = new StringBuilder();
                    for (Class<?> type : method.getParameterTypes()) {
                        full.append(type.getName()).append(",");
                    }
                    full.deleteCharAt(full.length() - 1);
                    if (pattern.matcher(full.toString()).matches()) {
                        //入参能匹配上
                        AbstractAspectJAdvice advice = null;
                        switch (config.getAdviceType()) {
                            case before:
                                advice = new AspectJMethodBeforeAdvice(config.getDeclaringClass(), config.getMethodName(), pointcut, config.getDeclarationOrder());
                                break;
                            case around:
                                advice = new AspectJAroundAdvice(config.getDeclaringClass(), config.getMethodName(), pointcut, config.getDeclarationOrder());
                                break;
                            case afterReturning:
                                advice = new AspectJAfterReturningAdvice(config.getDeclaringClass(), config.getMethodName(), pointcut, config.getDeclarationOrder());
                                advice.setReturningName(config.getReturningName());
                                break;
                            case afterThrowing:
                                advice = new AspectJAfterThrowingAdvice(config.getDeclaringClass(), config.getMethodName(), pointcut, config.getDeclarationOrder());
                                advice.setThrowingName(config.getThrowingName());
                                break;
                            case after:
                                advice = new AspectJAfterAdvice(config.getDeclaringClass(), config.getMethodName(), pointcut, config.getDeclarationOrder());
                                break;
                            default:
                                break;
                        }
                        advice.setAspect(this.applicationContext.getBean(config.getAspectName()));
                        advice.setAspectName(config.getAspectName());
                        advice.setAspectJAdviceMethod(methodMap.get(config.getMethodName()));
                        pointcut.addAdvice(method, advice);
                    }
                }
            }
            if (pointcut.containsAdvice()) {
                advices.add(pointcut);
            }
        }
        return advices;
    }
}
