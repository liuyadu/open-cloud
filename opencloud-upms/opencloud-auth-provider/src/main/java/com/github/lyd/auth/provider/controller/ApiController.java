package com.github.lyd.auth.provider.controller;

import com.github.lyd.auth.provider.service.impl.GiteeAuthServiceImpl;
import com.github.lyd.auth.provider.service.impl.QQAuthServiceImpl;
import com.github.lyd.auth.provider.service.impl.WechatAuthServiceImpl;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.security.OpenAuthUser;
import com.github.lyd.common.security.OpenHelper;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "认证开放接口")
@RestController
public class ApiController {
    @Autowired
    private QQAuthServiceImpl qqAuthService;
    @Autowired
    private WechatAuthServiceImpl wechatAuthService;
    @Autowired
    private GiteeAuthServiceImpl giteeAuthService;

    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/user/me")
    public ResultBody getUserProfile() {
        OpenAuthUser user = OpenHelper.getAuthUser();
        return ResultBody.success(user != null ? user.getUserProfile() : null);
    }

    /**
     * 获取第三方登录配置
     *
     * @return
     */
    @ApiOperation(value = "获取第三方登录配置", notes = "任何人都可访问")
    @GetMapping("/login/other/config")
    @ResponseBody
    public ResultBody getLoginOtherConfig() {
        Map<String, String> map = Maps.newHashMap();
        map.put("qq", qqAuthService.getAuthorizationUrl());
        map.put("wechat", wechatAuthService.getAuthorizationUrl());
        map.put("gitee", giteeAuthService.getAuthorizationUrl());
        return ResultBody.success(map);
    }

}
