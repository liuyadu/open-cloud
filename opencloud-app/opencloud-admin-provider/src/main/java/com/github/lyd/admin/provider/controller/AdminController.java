package com.github.lyd.admin.provider.controller;

import com.github.lyd.common.configuration.CommonProperties;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.DateUtils;
import com.github.lyd.common.utils.RandomValueUtils;
import com.github.lyd.common.utils.SignatureUtils;
import com.github.lyd.common.utils.WebUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "运营后台开放接口")
@RestController
public class AdminController {
    @Autowired
    private OpenRestTemplate openRestTemplate;
    @Autowired
    private ResourceServerProperties resourceServerProperties;
    @Autowired
    private CommonProperties commonProperties;
    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @param username
     * @param password
     * @return access_token
     */
    @ApiOperation(value = "获取用户访问令牌", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "form"),
            @ApiImplicitParam(name = "password", required = true, value = "登录密码", paramType = "form")
    })
    @PostMapping("/login/token")
    public Object getLoginToken(@RequestParam String username, @RequestParam String password, @RequestHeader HttpHeaders headers) {
        // 使用oauth2密码模式登录.
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("username", username);
        postParameters.add("password", password);
        postParameters.add("client_id", resourceServerProperties.getClientId());
        postParameters.add("client_secret", resourceServerProperties.getClientSecret());
        postParameters.add("grant_type", "password");
        // 使用客户端的请求头,发起请求
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除 原来的请求头,防止token失效
        headers.remove(HttpHeaders.AUTHORIZATION);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity(postParameters, headers);
        Map result = openRestTemplate.postForObject(commonProperties.getAccessTokenUri(), request, Map.class);
        if (result.containsKey("access_token")) {
            return ResultBody.success(result);
        } else {
            return result;
        }
    }

    /**
     * 参数签名
     *
     * @param
     * @return
     */
    @ApiOperation(value = "内部应用请求签名", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params1", value = "参数1", paramType = "form"),
            @ApiImplicitParam(name = "params2", value = "参数2", paramType = "form"),
            @ApiImplicitParam(name = "paramsN", value = "参数...", paramType = "form"),
    })
    @PostMapping(value = "/sign")
    public ResultBody sign(HttpServletRequest request) {
        Map params = WebUtils.getParameterMap(request);
        Map appMap = Maps.newHashMap();
        appMap.put("clientId", resourceServerProperties.getClientId());
        appMap.put("nonce", RandomValueUtils.uuid().substring(0, 16));
        appMap.put("timestamp", DateUtils.getTimestampStr());
        appMap.put("signType", "SHA256");
        params.putAll(appMap);
        String sign = SignatureUtils.getSign(params, resourceServerProperties.getClientSecret());
        appMap.put("sign", sign);
        return ResultBody.success(appMap);
    }
}
