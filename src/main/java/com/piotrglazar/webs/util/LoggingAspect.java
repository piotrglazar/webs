package com.piotrglazar.webs.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order
public class LoggingAspect {

    private final LoggingAspectLogic loggingAspectLogic;

    @Autowired
    public LoggingAspect(final LoggingAspectLogic loggingAspectLogic) {
        this.loggingAspectLogic = loggingAspectLogic;
    }

    @SuppressWarnings("all")
    @Around("@annotation(com.piotrglazar.webs.util.OperationLogging)")
    public Object logOperation(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return loggingAspectLogic.logOperation(proceedingJoinPoint);
    }
}
