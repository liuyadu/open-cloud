package com.github.lyd.common.http;

import com.github.lyd.common.configuration.CommonProperties;
import com.github.lyd.common.model.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * 自定义RestTemplate请求工具类
 *
 * @author: liuyadu
 * @date: 2018/12/11 15:51
 * @description:
 */
@Slf4j
public class OpenRestTemplate extends RestTemplate {

    private CommonProperties commonProperties;

    public OpenRestTemplate(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    /**
     * 构建网关Oauth2 client_credentials方式请求
     *
     * @return
     */
    public OAuth2RestTemplate buildOauth2ClientRequest() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setClientId(commonProperties.getClientId());
        resource.setClientSecret(commonProperties.getClientSecret());
        resource.setAccessTokenUri(commonProperties.getAccessTokenUri());
        resource.setAuthenticationScheme(AuthenticationScheme.form);
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource);
        return restTemplate;
    }

    /**
     * 构建网关Oauth2 password方式请求
     *
     * @return
     */
    public OAuth2RestTemplate buildOauth2PasswordRequest(String username, String password) {
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setClientId(commonProperties.getClientId());
        resource.setClientSecret(commonProperties.getClientSecret());
        resource.setAccessTokenUri(commonProperties.getAccessTokenUri());
        resource.setAuthenticationScheme(AuthenticationScheme.form);
        resource.setGrantType("password");
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource);
        return restTemplate;
    }

    /**
     * 刷新网关
     * 注:不要频繁调用!
     * 1.资源权限发生变化时可以调用
     * 2.流量限制变化时可以调用
     * 3.IP访问发生变化时可以调用
     * 4.智能路由发生变化时可以调用
     */
    public void refreshGateway() {
        try {
            Assert.notNull(this.commonProperties, "网关信息错误");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> formEntity = new HttpEntity<String>("", headers);
            ResultBody resultBody = buildOauth2ClientRequest().postForObject(commonProperties.getApiServerAddr().concat("/actuator/refresh-gateway"), formEntity, ResultBody.class);
            log.info("refreshGateway:{}", resultBody);
        } catch (Exception e) {
            log.error("refreshGateway error:{}", e.getMessage());
        }
    }

    public CommonProperties getcommonProperties() {
        return commonProperties;
    }

    public void setcommonProperties(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }
}
