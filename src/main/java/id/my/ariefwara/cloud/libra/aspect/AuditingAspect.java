package id.my.ariefwara.cloud.libra.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class AuditingAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditingAspect.class);

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public Object logAudit(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        logger.info("START | Method: {} | Params: {}", signature.toShortString(), Arrays.toString(joinPoint.getArgs()));

        try {
            Object result = joinPoint.proceed();
            logger.info("SUCCESS | Execution Time: {} ms", Duration.between(start, LocalDateTime.now()).toMillis());
            return result;
        } catch (Exception ex) {
            logger.error("FAILED | Exception: {}", ex.getMessage());
            throw ex;
        }
    }
}
