package com.tcl.jr.log.aspect;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

@Aspect
@Component
public class WebLogAspect {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    ThreadLocal<Long> startTime = new ThreadLocal<>();

	 @Pointcut("execution(public * com.tcl.jr.controller.*.*(..))")
	 public void webLog() {
		 
	 }
	 
	 @Before("webLog()")
	 public void deBefore(JoinPoint joinPoint) throws Throwable {
		 
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = attributes.getRequest();
	    logger.info("-------------------start request-----------------");
	     
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("HTTP_HEAD Type : " + request.getHeader("Content-Type"));
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        //logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            //记录application/json时的传参，SpringMVC中使用@RequestBody接收的值
            logger.info(getRequestPayload(request));
        } else {
            //记录请求的键值对
            for (String key : request.getParameterMap().keySet()) {
                logger.info(key + "----" + request.getParameter(key));
            }
        }

	          
	 }
	 
	 @AfterReturning(returning = "ret", pointcut = "webLog()")
	 public void doAfterReturning(Object ret) throws Throwable {
        logger.info("RESPONSE : " + ret);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
        logger.info("------------------request over------------------");
	 }
	 
	 //后置异常
	 @AfterThrowing(throwing = "ex", pointcut = "webLog()")
	 public void throwss(JoinPoint jp, Exception ex){
		 //logger.info("方法异常时执行.....");
		 String params = "";    
         if (jp.getArgs() !=  null && jp.getArgs().length > 0) {    
             for ( int i = 0; i < jp.getArgs().length; i++) {    
                 params += JSONObject.toJSON(jp.getArgs()[i]).toString() + ";";
            }  
        }
		 logger.error("ERROR_METHOD: " + jp.getTarget().getClass().getName() + jp.getSignature().getName());
		 logger.error("ERROR_CODE: " + ex.getClass().getName());
		 logger.error("ERROR_MESSAGE: " + ex.getMessage());
		 logger.error("ERROR_PARAME: " + params);		 
	 }
	 
	 @After("webLog()")
	 public void after(JoinPoint jp){
//	        logger.info("方法最后执行.....");
		 
	 }

	 
	 private String getRequestPayload(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader()) {
            char[]buff = new char[1024];
            int len;
            while((len = reader.read(buff)) != -1) {
                sb.append(buff,0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


	 
	 
	 
	 
	 
	
}
