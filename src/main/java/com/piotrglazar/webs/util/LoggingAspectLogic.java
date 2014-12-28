package com.piotrglazar.webs.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class LoggingAspectLogic {

    private static final Logger LOG = LoggerFactory.getLogger("Operation");

    @SuppressWarnings("all")
    public Object logOperation(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final String operationName = extractOperationName(proceedingJoinPoint);

        LOG.info("{}", operationName);

        try {
            return proceedingJoinPoint.proceed();
        } catch (Exception e) {
            LOG.info("{} failed with", operationName, e);
            throw e;
        }
    }

    private String extractOperationName(final ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        Method operation = getAnnotatedMethod(proceedingJoinPoint);

        final OperationLogging operationLogging = operation.getAnnotation(OperationLogging.class);
        return operationLogging.operation();
    }

    private Method getAnnotatedMethod(final ProceedingJoinPoint proceedingJoinPoint) throws NoSuchMethodException {
        final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method operation = signature.getMethod();
        // is it an interface? if yes, fetch the method from interface implementation
        if (operation.getDeclaringClass().isInterface()) {
            operation = proceedingJoinPoint.getTarget().getClass().getDeclaredMethod(operation.getName(), operation.getParameterTypes());
        }
        return operation;
    }
}
