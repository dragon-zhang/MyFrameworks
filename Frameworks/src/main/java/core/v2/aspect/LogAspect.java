package core.v2.aspect;

import MySpringMVC.V2.annotation.Component;
import MySpringMVC.V2.aop.intercept.JoinPoint;
import MySpringMVC.V2.aop.intercept.ProceedingJoinPoint;

import java.util.Arrays;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 *
 * @author Admin
 */
@Component
public class LogAspect {

    /**
     * 在核心业务执行前执行，不能阻止核心业务的调用。
     *
     * @param joinPoint
     */
    private void doBefore(JoinPoint joinPoint) {
        System.out.println("-----doBefore().invoke-----");
        System.out.println(" 此处意在执行核心业务逻辑前，做一些安全性的判断等等");
        System.out.println(" 可通过joinPoint来获取所需要的内容");
        System.out.println("-----End of doBefore()------");
    }

    /**
     * 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
     * <p>
     * 注意：当核心业务抛异常后，立即退出，转向After Advice
     * 执行完毕After Advice，再转到Throwing Advice
     *
     * @return
     * @throws Throwable
     */
    private Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("-----doAround().invoke-----");

        Object[] args = proceedingJoinPoint.getArgs();
        System.out.println("args->" + Arrays.toString(args));
        //调用核心逻辑
        Object retVal = proceedingJoinPoint.proceed(args);

        System.out.println("-----End of doAround()------");
        return retVal;
    }

    /**
     * 核心业务逻辑退出后（包括正常执行结束和异常退出），执行此Advice
     *
     * @param joinPoint
     */
    private void doAfter(JoinPoint joinPoint) {
        System.out.println("-----doAfter().invoke-----");
        System.out.println(" 此处意在执行核心业务逻辑之后，做一些日志记录操作等等");
        System.out.println(" 可通过joinPoint来获取所需要的内容");
        System.out.println("-----End of doAfter()------");
    }

    /**
     * 核心业务逻辑调用正常退出后，不管是否有返回值，正常退出后，均执行此Advice
     *
     * @param joinPoint
     * @param returnValue
     */
    private void doReturn(JoinPoint joinPoint, Object returnValue) {
        System.out.println("-----doReturn().invoke-----");
        System.out.println(" 此处可以对返回值做进一步处理：" + returnValue);
        System.out.println(" 可通过joinPoint来获取所需要的内容");
        System.out.println("-----End of doReturn()------");
    }

    /**
     * 核心业务逻辑调用异常退出后，执行此Advice，处理错误信息
     *
     * @param joinPoint
     * @param throwable
     */
    private void doThrowing(JoinPoint joinPoint, Throwable throwable) {
        System.out.println("-----doThrowing().invoke-----");
        System.out.println(" 错误信息：" + throwable.getMessage());
        System.out.println(" 此处意在执行核心业务逻辑出错时，捕获异常，并可做一些日志记录操作等等");
        System.out.println(" 可通过joinPoint来获取所需要的内容");
        System.out.println("-----End of doThrowing()------");
    }
}