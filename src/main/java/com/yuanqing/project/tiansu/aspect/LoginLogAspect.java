package com.yuanqing.project.tiansu.aspect;

import com.alibaba.fastjson.JSONArray;
import com.yuanqing.common.enums.SelfAuditSeverityEnum;
import com.yuanqing.framework.redis.RedisCache;
import com.yuanqing.project.tiansu.domain.log.TiansuLoginLog;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.time.LocalDateTime;


@Aspect
@Component
public class LoginLogAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginLogAspect.class);

    @Autowired
    private RedisCache redisCache;

    //登录失败日志
    @Pointcut("execution(public * com.yuanqing.framework.security.service.SysLoginService.login(..))")
    public void loginFailLog() {
    }

    @After("loginFailLog()")
    public void loginFailLog(JoinPoint joinPoint) throws Throwable {
        Object obj[] = joinPoint.getArgs();
        String username = (String) obj[0];

        String key = String.format("%s", username);
        Integer failedCnt = redisCache.getCacheObject(key);

        //登录失败次数限制
        Integer failedLoginCnt = redisCache.getCacheObject("tiansuFailedLoginCnt");
        Integer failedLoginTime = redisCache.getCacheObject("tiansuFailedLoginTime");

        String msg = null;
        if (failedCnt >= failedLoginCnt) {
            msg = "登录失败次数超过限制，此账户将锁定"+failedLoginTime+"分钟！";
            handleLog(joinPoint, username, msg, false);
        } else if (failedCnt > 0 && failedCnt < failedLoginCnt) {
            msg = "登录失败，用户名或密码错误！";
            handleLog(joinPoint, username, msg, false);
        }else{
            msg = "登录成功！";
            handleLog(joinPoint, username, msg, true);
        }
    }

    protected void handleLog(JoinPoint joinPoint, String username, String msg, Boolean result) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String httpMethod = request.getMethod();
        String actionUrl = request.getRequestURI();
        if (actionUrl.equals("/api/login")) {
            httpMethod = "LOGIN";
        }
        if (httpMethod.equals("LOGIN")) {
            TiansuLoginLog loginLog = new TiansuLoginLog();
            loginLog.setStime(LocalDateTime.now());
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
                InetAddress addr = InetAddress.getLocalHost();
                ipAddress = addr.getHostAddress().toString(); //获取本机ip
            }
            String actionName = joinPoint.getSignature().getDeclaringTypeName();
            // 发送mq消息的消息体太长，插入数据库会报错
            if (actionName.endsWith("MqSendController")) {
                return ;
            }
            String actionMethod = joinPoint.getSignature().getName();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();

            String actionArgs = "";
            if (parameterNames != null && joinPoint.getArgs() != null) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    actionArgs += (parameterNames[i] + " = " + joinPoint.getArgs()[i] + ",");
                }
            } else {
                actionArgs = "";
            }
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append("\nIP地址：" + ipAddress +
//                    "\n用户名：" + username +
//                    "\n时间：" + LocalDateTime.now() +
//                    "\n调用：" + actionName + "的" + actionMethod + "方法");
//            stringBuffer.append("\n登录");
            SelfAuditSeverityEnum severity = SelfAuditSeverityEnum.COMMON;
//            stringBuffer.append("\n登录");
//            if (actionArgs.length() > 0) {
//                actionArgs = actionArgs.substring(0, actionArgs.length() - 1);
//            }
//            stringBuffer.append("\n参数：" + actionArgs);
//            stringBuffer.append("\n结果：" + msg);
//            String describe = stringBuffer.toString();
            loginLog.setUsername(username);
            loginLog.setUrl(actionUrl);
            loginLog.setName(actionName);
            loginLog.setMethod(actionMethod);
            loginLog.setArgs(actionArgs);
            loginLog.setType(httpMethod);
            loginLog.setIpAddress(ipAddress);
            loginLog.setResult(result);
            loginLog.setSeverity(severity);
            loginLog.setDescribe(msg);
            loginLog.setEtime(LocalDateTime.now());
            loginLog.setModular("tiansu-api");
            Object object = JSONArray.toJSON(loginLog);
            String json = object.toString();
            LOGGER.error(json);
        }
    }
}
