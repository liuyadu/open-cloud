package com.github.lyd.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义网关配置
 *
 * @author: liuyadu
 * @date: 2018/11/23 14:40
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.common")
public class CommonProperties {
    /**
     * 网关客户端Id
     */
    private String clientId;
    /**
     * 网关客户端密钥
     */
    private String clientSecret;
    /**
     * 网关服务地址
     */
    private String apiServerAddr;

    /**
     * 平台认证服务地址
     */
    private String authServerAddr;

    /**
     * 后台部署地址
     */
    private String adminServerAddr;

    /**
     * 认证范围
     */
    private String scope;
    /**
     * 获取token
     */
    private String accessTokenUri;
    /**
     * 认证地址
     */
    private String userAuthorizationUri;
    /**
     * 获取token地址
     */
    private String tokenInfoUri;
    /**
     * 获取用户信息地址
     */
    private String userInfoUri;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getApiServerAddr() {
        return apiServerAddr;
    }

    public void setApiServerAddr(String apiServerAddr) {
        this.apiServerAddr = apiServerAddr;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getUserAuthorizationUri() {
        return userAuthorizationUri;
    }

    public void setUserAuthorizationUri(String userAuthorizationUri) {
        this.userAuthorizationUri = userAuthorizationUri;
    }

    public String getTokenInfoUri() {
        return tokenInfoUri;
    }

    public void setTokenInfoUri(String tokenInfoUri) {
        this.tokenInfoUri = tokenInfoUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getAdminServerAddr() {
        return adminServerAddr;
    }

    public void setAdminServerAddr(String adminServerAddr) {
        this.adminServerAddr = adminServerAddr;
    }

    public String getAuthServerAddr() {
        return authServerAddr;
    }

    public void setAuthServerAddr(String authServerAddr) {
        this.authServerAddr = authServerAddr;
    }

    @Override
    public String toString() {
        return "CommonProperties{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", apiServerAddr='" + apiServerAddr + '\'' +
                ", authServerAddr='" + authServerAddr + '\'' +
                ", adminServerAddr='" + adminServerAddr + '\'' +
                ", scope='" + scope + '\'' +
                ", accessTokenUri='" + accessTokenUri + '\'' +
                ", userAuthorizationUri='" + userAuthorizationUri + '\'' +
                ", tokenInfoUri='" + tokenInfoUri + '\'' +
                ", userInfoUri='" + userInfoUri + '\'' +
                '}';
    }
}
