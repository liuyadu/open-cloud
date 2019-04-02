package com.github.lyd.auth.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.lyd.auth.client.constants.AuthConstants;
import com.github.lyd.auth.client.config.SocialOAuth2ClientDetails;
import com.github.lyd.auth.client.config.SocialOAuth2ClientProperties;
import com.github.lyd.auth.client.service.ThirdPartyAuthService;
import com.github.lyd.common.http.OpenRestTemplate;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 微信oauth2认证实现类
 *
 * @author liuyadu
 */
@Service("giteeService")
@Slf4j
public class GiteeAuthServiceImpl implements ThirdPartyAuthService {

    @Autowired
    private OpenRestTemplate restTemplate;
    @Autowired
    private SocialOAuth2ClientProperties socialAuthProperties;
    /**
     * 微信 登陆页面的URL
     */
    private final static String AUTHORIZATION_URL = "%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";
    /**
     * 获取token的URL
     */
    private final static String ACCESS_TOKEN_URL = "%s?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s";

    /**
     * 获取用户信息的 URL，oauth_consumer_key 为 apiKey
     */
    private static final String USER_INFO_URL = "%s?access_token=%s";


    @Override
    public String getAuthorizationUrl() {
        String url = String.format(AUTHORIZATION_URL, getClientDetails().getUserAuthorizationUri(), getClientDetails().getClientId(), getClientDetails().getRedirectUri(), getClientDetails().getScope(),System.currentTimeMillis());
        return url;
    }

    @Override
    public String getAccessToken(String code) {
        String url = String.format(ACCESS_TOKEN_URL, getClientDetails().getAccessTokenUri(), getClientDetails().getClientId(), getClientDetails().getClientSecret(), code,getClientDetails().getRedirectUri());
        Map data = Maps.newHashMap();
        String resp = restTemplate.postForObject(url,data, String.class);
        if (resp != null && resp.contains("access_token")) {
                JSONObject jsonObject = JSONObject.parseObject(resp);
                return  jsonObject.getString("access_token");
        }
        log.error("Gitee获得access_token失败，code无效，resp:{}", resp);
        return null;
    }

    @Override
    public String getOpenId(String accessToken) {
        return null;
    }

    @Override
    public JSONObject getUserInfo(String accessToken, String openId) {
        String url = String.format(USER_INFO_URL, getClientDetails().getUserInfoUri(), accessToken);
        String resp = restTemplate.getForObject(url, String.class);
        JSONObject data = JSONObject.parseObject(resp);
        return data;
    }

    @Override
    public String refreshToken(String code) {
        return null;
    }

    /**
     * 获取登录成功地址
     *
     * @return
     */
    @Override
    public String getLoginSuccessUrl() {
        return getClientDetails().getLoginSuccessUri();
    }

    /**
     * 获取客户端配置信息
     *
     * @return
     */
    @Override
    public SocialOAuth2ClientDetails getClientDetails() {
        return socialAuthProperties.getOauth2().get(AuthConstants.LOGIN_GITEE);
    }

}
