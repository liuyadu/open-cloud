package com.github.common.tenant.interceptor;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.Properties;

/**
 * MyBatis拦截器
 *
 * @author liuyadu
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}))
public class MyBatisInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Connection o = (Connection) invocation.getArgs()[0];
        invocation.getArgs();
        try {
            RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
            String sql = handler.getBoundSql().getSql();
            return invocation.proceed();
        } catch (Exception e) {
            logger.error("执行失败！", e);
            return null;
        } finally {

        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

}