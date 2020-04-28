package MySpringMVC.V2.aop.aspectj;

import MySpringMVC.V2.aop.ProxyMethodInvocation;
import MySpringMVC.V2.aop.intercept.ProceedingJoinPoint;
import lombok.Data;

import java.lang.reflect.AccessibleObject;

/**
 * @author SuccessZhang
 * @date 2020/04/16
 */
@Data
public class MethodInvocationProceedingJoinPoint implements ProceedingJoinPoint {

    private final ProxyMethodInvocation methodInvocation;

    private Object[] defensiveCopyOfArgs;

    public MethodInvocationProceedingJoinPoint(ProxyMethodInvocation methodInvocation) {
        this.methodInvocation = methodInvocation;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.methodInvocation.invocableClone().proceed();
    }

    @Override
    public Object getThis() {
        return this.methodInvocation.getProxy();
    }

    @Override
    public Object getTarget() {
        return this.methodInvocation.getThis();
    }

    @Override
    public Object[] getArgs() {
        if (this.defensiveCopyOfArgs == null) {
            Object[] argsSource = this.methodInvocation.getArguments();
            this.defensiveCopyOfArgs = new Object[argsSource.length];
            System.arraycopy(argsSource, 0, this.defensiveCopyOfArgs, 0, argsSource.length);
        }
        return this.defensiveCopyOfArgs;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return this.methodInvocation.getMethod();
    }

    @Override
    public Object proceed(Object[] args) throws Throwable {
        if (args.length != this.methodInvocation.getArguments().length) {
            throw new IllegalArgumentException("Expecting " +
                    this.methodInvocation.getArguments().length + " arguments to proceed, " +
                    "but was passed " + args.length + " arguments");
        }
        this.methodInvocation.setArguments(args);
        return this.methodInvocation.invocableClone(args).proceed();
    }
}
