package com.github.common.tenant.configuration;

import com.github.common.tenant.datasource.DynamicDataSourceAspect;
import com.github.common.tenant.datasource.DynamicDataSourceContextHolder;
import com.github.common.tenant.datasource.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2019/3/20 13:20
 * @description:
 */
@Configuration
@EnableConfigurationProperties(value = {OpenTenantProperties.class})
public class DynamicDataSourceConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceConfiguration.class);


    @ConditionalOnProperty(value = "opencloud.tenant",matchIfMissing = true)
    @ConditionalOnMissingBean(DynamicDataSourceAspect.class)
    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect(OpenTenantProperties openSaasProperties){
        logger.info("==> Current tenant is [{}]",openSaasProperties.getTenantId());
       return new DynamicDataSourceAspect(openSaasProperties);
    }

    @ConditionalOnProperty(value = "opencloud.tenant",matchIfMissing = true)
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(DataSourceProperties dataSourceProperties,OpenTenantProperties openSaasProperties) {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        if (dataSourceProperties != null) {
            // 默认数据源配置
            DataSourceBuilder builder = DataSourceBuilder.create().driverClassName(dataSourceProperties.getDriverClassName());
            builder.url(dataSourceProperties.getUrl());
            builder.username(dataSourceProperties.getUsername());
            builder.password(dataSourceProperties.getPassword());
            builder.type(dataSourceProperties.getType());
            dataSourceMap.put("master", builder.build());
        }

        if(openSaasProperties!=null){
            // 租户数据源配置
            DataSourceBuilder builder = DataSourceBuilder.create().driverClassName(openSaasProperties.getDatasource().getDriverClassName());
            builder.url(openSaasProperties.getDatasource().getUrl());
            builder.username(openSaasProperties.getDatasource().getUsername());
            builder.password(openSaasProperties.getDatasource().getPassword());
            builder.type(openSaasProperties.getDatasource().getType());
            dataSourceMap.put(openSaasProperties.getTenantId(),builder.build());
        }
        // 设置master 为默认数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(dataSourceMap.get("master"));
        // 可动态路由的数据源里装载了所有可以被路由的数据源
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        // To put datasource keys into DataSourceContextHolder
        DynamicDataSourceContextHolder.dataSourceKeys.addAll(dataSourceMap.keySet());
        return dynamicRoutingDataSource;
    }

}
