package com.github.lyd.auth.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2019/2/14 14:34
 * @description:
 */
@ConfigurationProperties(prefix = "opencloud.social")
public class SocialOAuth2ClientProperties {

    private Map<String, SocialOAuth2ClientDetails> oauth2;

    public Map<String, SocialOAuth2ClientDetails> getOauth2() {
        return oauth2;
    }

    public void setOauth2(Map<String, SocialOAuth2ClientDetails> oauth2) {
        this.oauth2 = oauth2;
    }
}
