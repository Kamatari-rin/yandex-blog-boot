package org.example.logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class UserServiceLogger {

//    private static final Logger logger = LoggerFactory.getLogger(UserServiceLogger.class);
//
//    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
//    public void controllerMethods() {}
//
//    @Pointcut("within(org.example.service..*)")
//    public void serviceMethods() {}
//
//    @Pointcut("within(org.example.repository..*)")
//    public void repositoryMethods() {}
//
//    @Before("controllerMethods() || serviceMethods() || repositoryMethods()")
//    public void logBeforeMethod(JoinPoint joinPoint) {
//        logger.info("Вход в метод: {}.{} с аргументами: {}",
//                joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(),
//                Arrays.toString(joinPoint.getArgs()));
//    }
//
//    @AfterReturning(pointcut = "controllerMethods() || serviceMethods() || repositoryMethods()", returning = "result")
//    public void logAfterMethod(JoinPoint joinPoint, Object result) {
//        logger.info("Метод: {}.{} выполнен успешно. Результат: {}",
//                joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(),
//                result);
//    }
}
