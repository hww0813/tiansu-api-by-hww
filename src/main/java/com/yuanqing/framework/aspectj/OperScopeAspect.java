package com.yuanqing.framework.aspectj;

import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.framework.aspectj.lang.annotation.OperScope;
import com.yuanqing.framework.web.domain.BaseEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 * 操作行为数据过滤处理
 * @author xucan
 * @version 1.0
 * @Date 2021-06-18 10:47
 */
@Aspect
@Component
public class OperScopeAspect {


    // 配置织入点
    @Pointcut("@annotation(com.yuanqing.framework.aspectj.lang.annotation.OperScope)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        OperScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
                dataScopeFilter(joinPoint, controllerDataScope.operAlias());
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param operAlias 别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint , String operAlias)
    {
        StringBuilder sqlString = new StringBuilder();

        sqlString.append(StringUtils.format(" OR {}.action <> 10 and {}.action <> 11 ", operAlias,operAlias));

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            Object[] args = joinPoint.getArgs();
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.setDataScope(" AND (" + sqlString.substring(4) + ")");
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private OperScope getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(OperScope.class);
        }
        return null;
    }
}
