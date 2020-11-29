package com.example.dc.controller;

import com.example.dc.pojo.SysLog;
import com.example.dc.service.SysLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import java.util.Date;


public class LogAop {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;

    private Date startTime; // 访问时间
    private Class executionClass;// 访问的类
    private Method executionMethod; // 访问的方法

    @Before("execution(* com.example.dc.controller.*.*(..))")
    public void doBefore(JoinPoint jp) throws NoSuchMethodException {
        startTime = new Date(); // 访问时间
        executionClass = jp.getTarget().getClass();// 获取访问的类
        String methodName = jp.getSignature().getName();// 获取访问的方法的名称

        Object[] args = jp.getArgs();// 获取访问的方法的参数
        if (args == null || args.length == 0) {
            executionMethod = executionClass.getMethod(methodName); // 只能获取无参数方法
        } else {
            // 有参数，就将args中所有元素遍历，获取对应的Class,装入到一个Class[]
            Class[] classArgs = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classArgs[i] = args[i].getClass();
            }
            executionMethod = executionClass.getMethod(methodName, classArgs);// 获取有参数方法
        }

    }
    @After("execution(* com.example.dc.controller.*.*(..))")
    public void doAfter(JoinPoint jp) throws Exception {
        String url="";
        Long time=new Date().getTime()-startTime.getTime();//获取访问时长
        if (executionClass!=null&&executionMethod!=null&&executionClass!=LogAop.class){

            //1.获取类上的@RequestMapping("/orders")
            RequestMapping classAnnotation = (RequestMapping) executionClass.getAnnotation(RequestMapping.class);
            if (classAnnotation!=null){
                String[] classValues = classAnnotation.value();

                //2.获取方法上的@RequestMapping(xxx)
                RequestMapping methodAnnotation = executionMethod.getAnnotation(RequestMapping.class);
                if (methodAnnotation!=null){
                    String[] methodValue = methodAnnotation.value();
                    url=classValues[0]+methodValue[0];
                }
            }
        }
        //获取当前ip
        String ip = request.getRemoteAddr();

        //获取当前操作的用户
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if (principal==null){
            return;
        }
        String username = principal.toString();


        SysLog sysLog=new SysLog();
        sysLog.setExecutionTime(time);
        sysLog.setIp(ip);
        sysLog.setMethod("[类名] "+executionClass.getName()+"[方法名] "+executionMethod.getName());
        sysLog.setUrl(url);
        sysLog.setUsername(username);
        sysLog.setVisitTime(startTime);

        //调用service
        sysLogService.save(sysLog);

    }
}
