package com.proximityservice.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

/**
 * Aspect for logging service method execution.
 * Logs method entry/exit, parameters, return values, execution time, and exceptions.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all public methods in the BusinessService class
     */
    @Around("execution(public * com.proximityservice.service.BusinessService.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        // Log method entry with parameters
        logger.info(">>> [ENTERING] {}.{}", className, methodName);
        logger.debug(">>> [PARAMETERS] {}.{} with args: {}", className, methodName, Arrays.toString(args));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // Execute the method
            Object result = joinPoint.proceed();

            stopWatch.stop();

            // Log successful execution with return value
            logger.info("<<< [EXITING] {}.{} - Execution completed successfully in {} ms",
                    className, methodName, stopWatch.getLastTaskTimeMillis());
            logger.debug("<<< [RETURN VALUE] {}.{} returned: {}", className, methodName, result);

            return result;
        } catch (Exception e) {
            stopWatch.stop();

            // Log exception details
            logger.error("!!! [EXCEPTION] {}.{} - Exception occurred after {} ms",
                    className, methodName, stopWatch.getLastTaskTimeMillis(), e);
            logger.error("!!! [ERROR DETAILS] Exception type: {}, Message: {}",
                    e.getClass().getSimpleName(), e.getMessage());

            throw e;
        }
    }

}

