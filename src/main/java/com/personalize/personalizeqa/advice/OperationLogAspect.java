package com.personalize.personalizeqa.advice;


import com.alibaba.fastjson.JSON;
import com.personalize.personalizeqa.annotationEntity.OperationLogging;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.Log;
import com.personalize.personalizeqa.server.ILogService;
import com.personalize.personalizeqa.utils.HttpUtils;
import com.personalize.personalizeqa.utils.UserHolder;
import com.personalize.personalizeqa.utils.id.IdGenerate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Aspect
public class OperationLogAspect {
    @Autowired
    private IdGenerate<Long> idGenerate;
    @Autowired
    private ILogService logService;
    private final ThreadLocal<LocalDateTime> startTime = new ThreadLocal<>();

    @Before("@annotation(operationLogging)")
    public void before(OperationLogging operationLogging){
        startTime.set(LocalDateTime.now());
    }

    @AfterReturning(pointcut = "@annotation(operationLogging)",returning = "result")
    public void afterReturning(JoinPoint joinPoint,OperationLogging operationLogging,Object result){
        String resultStr = JSON.toJSONString(result);
        if (resultStr.length()>250){
            resultStr = resultStr.substring(0,100);
        }
        buildAndSaveLog(joinPoint,operationLogging,resultStr);

    }
    @AfterThrowing(pointcut = "@annotation(operationLogging)",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint,OperationLogging operationLogging,Exception e){
        String resultStr = e.getMessage();
        if (resultStr.length()>250){
            resultStr = resultStr.substring(0,250);
        }
        buildAndSaveLog(joinPoint,operationLogging,resultStr);
    }
    @Transactional(rollbackFor = Exception.class)
    public void buildAndSaveLog(JoinPoint joinPoint, OperationLogging operationLogging, String resultStr){
        UserDTO user = UserHolder.getUser();
        Log log  = Log.builder().id(idGenerate.generate().toString())
                .description(operationLogging.description())
                .method(joinPoint.getSignature().getName())
                .params(JSON.toJSONString(joinPoint.getArgs()))
                .result(resultStr)
                .actionTime(startTime.get())
                .duration(Duration.between(startTime.get(),LocalDateTime.now()).toMillis())
                .actionUser(user.getUsername())
                .actionAuthority(user.getAuthority())
                .operationType(operationLogging.type().ordinal())
                .build();
        HttpUtils.getRequest().map(HttpUtils::getIpAddress).ifPresent(log::setIp);
        logService.save(log);
    }
}
