package com.aspectlib.test;

import android.util.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
/**
 * Created by lining on 2017/10/19.
 */
@Aspect
public class TraceAspect {
    private String TAG = "TAG ";
    private static final String POINT_METHOD = "execution(* com.lining.gradlebuild.MainActivity.*(..))";
    private static final String POINT_CALLMETHOD = "call(* com.lining.gradlebuild.MainActivity.*(..))";
    @Pointcut(POINT_METHOD)
    public void methodAnnotated(){}
    @Pointcut(POINT_CALLMETHOD)
    public void methodCallAnnotated(){}
    @Around("methodAnnotated()")
    public Object aronudWeaverPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
        String result = "----------------------------->aroundWeaverPoint";
        Log.e(TAG,"----------------------------->aroundWeaverPoint");
        return  result;//替换原方法的返回值
    }
    @Before("methodCallAnnotated()")
    public void beforecall(JoinPoint joinPoint){
        Log.e(TAG,"beforecall");
    }
}
