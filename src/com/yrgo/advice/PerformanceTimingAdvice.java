package com.yrgo.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {

    public Object performTimingMeasurement(ProceedingJoinPoint method) throws Throwable {
        long startTime = System.nanoTime();
        try {
            return method.proceed();
        }
        finally {
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            long timeTakenInMs = timeTaken / 1000000;
            System.out.printf(
                "Time taken for the method %s from the class %s took %dms%n",
                method.getSignature().getName(), AopProxyUtils.ultimateTargetClass(method.getTarget()).getName(), timeTakenInMs);
        }
    }
}
