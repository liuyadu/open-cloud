package com.github.common.tenant.datasource;

import com.github.common.tenant.configuration.OpenTenantProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多数据源,AOP。方法执行前切换数据源
 * Multiple DataSource Aspect
 *
 * @author liuyadu
 */
@Aspect
public class DynamicDataSourceAspect {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    private OpenTenantProperties openSaasProperties;

    public DynamicDataSourceAspect(OpenTenantProperties openSaasProperties) {
        this.openSaasProperties = openSaasProperties;
    }

    public OpenTenantProperties getOpenSaasProperties() {
        return openSaasProperties;
    }

    public void setOpenSaasProperties(OpenTenantProperties openSaasProperties) {
        this.openSaasProperties = openSaasProperties;
    }

    /**
     * Dao aspect.
     */
    @Pointcut("execution( * com.github..*.mapper.*.*(..))")
    public void daoAspect() {
    }

    /**
     * 选择数据源 DataSource
     *
     * @param point the point
     */
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point) {
        if (openSaasProperties.getTenantId() != null) {
            DynamicDataSourceContextHolder.setDataSourceKey(openSaasProperties.getTenantId());
            logger.info("==> Switch DataSource to [{}] in Method [{}] tenant is [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature(),openSaasProperties.getTenantId());
        }else{
            DynamicDataSourceContextHolder.setDataSourceKey("master");
        }
    }

    /**
     * 重置 DataSource
     *
     * @param point the point
     */
    @After("daoAspect())")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("==> Restore DataSource to [{}] in Method [{}]", DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }


}
