package com.github.lyd.gateway.provider.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义网关配置
 *
 * @author: liuyadu
 * @date: 2018/11/23 14:40
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.api-gateway")
public class ApiGatewayProperties {
    /**
     * 是否开启签名验证
     */
    private Boolean checkSign = true;
    /**
     * 是否开启动态访问控制
     */
    private Boolean accessControl = true;

    /**
     * 始终放行
     */
    private String permitAll;

    /**
     * 无权限允许访问
     */
    private String noAuthorityAllow;


    public Boolean getCheckSign() {
        return checkSign;
    }

    public void setCheckSign(Boolean checkSign) {
        this.checkSign = checkSign;
    }

    public Boolean getAccessControl() {
        return accessControl;
    }

    public void setAccessControl(Boolean accessControl) {
        this.accessControl = accessControl;
    }

    public String getPermitAll() {
        return permitAll;
    }

    public void setPermitAll(String permitAll) {
        this.permitAll = permitAll;
    }

    public String getNoAuthorityAllow() {
        return noAuthorityAllow;
    }

    public void setNoAuthorityAllow(String noAuthorityAllow) {
        this.noAuthorityAllow = noAuthorityAllow;
    }
}
