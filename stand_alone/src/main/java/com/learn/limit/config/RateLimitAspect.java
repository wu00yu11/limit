package com.learn.limit.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.learn.limit.annotation.RateLimit;
import com.learn.limit.constant.RespType;
import com.learn.limit.vo.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: limit
 * @description:
 * @author: zjj
 * @create: 2019-12-18 11:08
 **/
@Component
@Scope
@Aspect
public class RateLimitAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    /* 用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter) */
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    private static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    private RateLimiter rateLimiter;

    @Autowired
    private HttpServletResponse response;

    @Pointcut("@annotation(com.learn.limit.annotation.RateLimit)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        /* 获取拦截的方法名 */
        Signature sig = joinPoint.getSignature();
        /* 获取拦截的方法名 */
        MethodSignature msig = (MethodSignature) sig;
        /* 返回被织入增加处理目标对象 */
        Object target = joinPoint.getTarget();
        /* 为了获取注解信息 */
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        /* 获取注解信息 */
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        /* 获取注解每秒加入桶中的token */
        double limitNum = annotation.limitNum();
        /* 注解所在方法名区分不同的限流策略 */
        String functionName = msig.getName();

        /* 获取rateLimiter */
        if(map.containsKey(functionName)){
            rateLimiter = map.get(functionName);
        }else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }

        try {
            if (rateLimiter.tryAcquire()) {
                /* 执行方法 */
                obj = joinPoint.proceed();
            } else {
                /* 拒绝了请求（服务降级） */
                String result = objectMapper.writeValueAsString(new Result(RespType.SERVER_EXCEPTION));
                log.info("拒绝了请求：" + result);
                outErrorResult(result);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
    /* 将结果返回 */
    public void outErrorResult(String result) {
        response.setContentType("application/json;charset=UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(result.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
