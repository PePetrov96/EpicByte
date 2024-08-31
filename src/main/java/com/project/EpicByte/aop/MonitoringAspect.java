package com.project.EpicByte.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MonitoringAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitoringAspect.class);

    @Pointcut("@annotation(SlowExecutionWarning)")
    void onSlowExecutionWarning() {
    }

    @Around("onSlowExecutionWarning()")
    Object monitorExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        //before
        SlowExecutionWarning annotation = getAnnotation(pjp);
        long threshold = annotation.threshold();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //process
        var result = pjp.proceed();

        //after
        stopWatch.stop();
        long executionTime = stopWatch.lastTaskInfo().getTimeMillis();

        if (executionTime > threshold) {
            LOGGER.warn("Method {} executed in {} ms, which is above threshold of {} ms.",
                    pjp.getSignature().getName(), executionTime, threshold);
        }

        return result;
    }

    private static SlowExecutionWarning getAnnotation(ProceedingJoinPoint pjp) {
        return ((MethodSignature) pjp.getSignature())
                .getMethod()
                .getAnnotation(SlowExecutionWarning.class);
    }
}