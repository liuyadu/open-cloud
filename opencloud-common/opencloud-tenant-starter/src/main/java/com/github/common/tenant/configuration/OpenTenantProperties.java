package com.github.common.tenant.configuration;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author: liuyadu
 * @date: 2019/3/20 12:55
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.tenant")
public class OpenTenantProperties {
    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 租户数据源
     */
    private DataSourceProperties datasource;

    /**
     * 共享表名,不区分租户
     */
    private Set<String> shareTables;

    /**
     * 当前模块表名,区分租户
     */
    private Set<String> moduleTables;

    /**
     * 模块安装sql文件路径,可用于动态部署当前模块
     */
    private String initSql;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Set<String> getShareTables() {
        return shareTables;
    }

    public void setShareTables(Set<String> shareTables) {
        this.shareTables = shareTables;
    }

    public Set<String> getModuleTables() {
        return moduleTables;
    }

    public void setModuleTables(Set<String> moduleTables) {
        this.moduleTables = moduleTables;
    }

    public String getInitSql() {
        return initSql;
    }

    public void setInitSql(String initSql) {
        this.initSql = initSql;
    }

    public DataSourceProperties getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSourceProperties datasource) {
        this.datasource = datasource;
    }

    @Override
    public String toString() {
        return "OpenTenantProperties{" +
                "tenantId='" + tenantId + '\'' +
                ", shareTables=" + shareTables +
                ", moduleTables=" + moduleTables +
                ", initSql='" + initSql + '\'' +
                '}';
    }
}
