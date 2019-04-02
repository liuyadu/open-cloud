package com.github.lyd.base.provider.controller;

import com.github.lyd.base.client.api.BaseAppRemoteApi;
import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.base.provider.service.BaseAppService;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 系统用户信息
 *
 * @author liuyadu
 */
@Api(tags = "系统应用管理")
@RestController
public class BaseAppController implements BaseAppRemoteApi {
    @Autowired
    private BaseAppService baseAppService;

    /**
     * 获取分页应用列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页应用列表", notes = "获取分页应用列表")
    @GetMapping("/app")
    public ResultBody<PageList<BaseApp>> getAppListPage(@RequestParam Map map) {
        PageList<BaseApp> pageList = baseAppService.findListPage(new PageParams(map));
        return ResultBody.success(pageList);
    }

    /**
     * 获取应用详情
     *
     * @param appId
     * @return
     */
    @ApiOperation(value = "获取应用详情", notes = "仅限系统内部调用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/{appId}/info")
    @Override
    public ResultBody<BaseApp> getApp(
            @PathVariable("appId") String appId
    ) {
        BaseApp appInfo = baseAppService.getAppInfo(appId);
        return ResultBody.success(appInfo);
    }

    /**
     * 获取应用开发配置信息
     *
     * @param appId
     * @return
     */
    @ApiOperation(value = "获取应用开发配置信息", notes = "获取应用开发配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/app/client/{appId}/info")
    @Override
    public ResultBody<BaseClientDetails> getAppClientInfo(
            @PathVariable("appId") String appId
    ) {
        BaseClientDetails clientInfo = baseAppService.getAppClientInfo(appId);
        return ResultBody.success(clientInfo);
    }

    /**
     * 添加应用信息
     *
     * @param appName   应用名称
     * @param appNameEn 应用英文名称
     * @param appOs     手机应用操作系统:ios-苹果 android-安卓
     * @param appType   应用类型:server-应用服务 app-手机应用 pc-PC网页应用 wap-手机网页应用
     * @param appIcon   应用图标
     * @param appDesc   应用说明
     * @param status    状态
     * @param website   官网地址
     * @param userId    开发者
     * @param userType  开发者类型
     * @return
     */
    @ApiOperation(value = "添加应用信息", notes = "添加应用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "应用名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appNameEn", value = "应用英文名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appType", value = "应用类型(server-应用服务 app-手机应用 pc-PC网页应用 wap-手机网页应用)", allowableValues = "server,app,pc,wap", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appIcon", value = "应用图标", paramType = "form"),
            @ApiImplicitParam(name = "appOs", value = "手机应用操作系统", allowableValues = "android,ios", required = false, paramType = "form"),
            @ApiImplicitParam(name = "appDesc", value = "应用说明", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "website", value = "官网地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "0-平台,其他填写真实Id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "userType", value = "开发者类型", allowableValues = "platform,isp,dev", required = true, paramType = "form")
    })
    @PostMapping("/app/add")
    public ResultBody<String> addApp(
            @RequestParam(value = "appName") String appName,
            @RequestParam(value = "appNameEn") String appNameEn,
            @RequestParam(value = "appType") String appType,
            @RequestParam(value = "appIcon") String appIcon,
            @RequestParam(value = "appOs", required = false) String appOs,
            @RequestParam(value = "appDesc", required = false) String appDesc,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "website") String website,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "userType") String userType
    ) {
        BaseApp app = new BaseApp();
        app.setAppName(appName);
        app.setAppNameEn(appNameEn);
        app.setAppType(appType);
        app.setAppOs(appOs);
        app.setAppIcon(appIcon);
        app.setAppDesc(appDesc);
        app.setStatus(status);
        app.setWebsite(website);
        app.setUserId(userId);
        app.setUserType(userType);
        BaseApp result = baseAppService.addAppInfo(app);
        String appId = null;
        if (result != null) {
            appId = result.getAppId();
        }
        return ResultBody.success(appId);
    }

    /**
     * 编辑应用信息
     *
     * @param appId
     * @param appName   应用名称
     * @param appNameEn 应用英文名称
     * @param appOs     手机应用操作系统:ios-苹果 android-安卓
     * @param appType   应用类型:server-应用服务 app-手机应用 pc-PC网页应用 wap-手机网页应用
     * @param appIcon   应用图标
     * @param appDesc   应用说明
     * @param status    状态
     * @param website   官网地址
     * @param userId    开发者
     * @param userType  开发者类型
     * @return
     * @
     */
    @ApiOperation(value = "编辑应用信息", notes = "编辑应用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appName", value = "应用名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appNameEn", value = "应用英文名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appType", value = "应用类型(server-应用服务 app-手机应用 pc-PC网页应用 wap-手机网页应用)", allowableValues = "server,app,pc,wap", required = true, paramType = "form"),
            @ApiImplicitParam(name = "appIcon", value = "应用图标", paramType = "form"),
            @ApiImplicitParam(name = "appOs", value = "手机应用操作系统", allowableValues = "android,ios", required = false, paramType = "form"),
            @ApiImplicitParam(name = "appDesc", value = "应用说明", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "website", value = "官网地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "userId", value = "0-平台,其他填写真实Id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "userType", value = "开发者类型", allowableValues = "platform,isp,dev", required = true, paramType = "form")
    })
    @PostMapping("/app/update")
    public ResultBody updateApp(
            @RequestParam("appId") String appId,
            @RequestParam(value = "appName") String appName,
            @RequestParam(value = "appNameEn") String appNameEn,
            @RequestParam(value = "appType") String appType,
            @RequestParam(value = "appIcon", required = false) String appIcon,
            @RequestParam(value = "appOs", required = false) String appOs,
            @RequestParam(value = "appDesc", required = false) String appDesc,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "website") String website,
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "userType") String userType
    ) {
        BaseApp app = new BaseApp();
        app.setAppId(appId);
        app.setAppName(appName);
        app.setAppNameEn(appNameEn);
        app.setAppType(appType);
        app.setAppOs(appOs);
        app.setAppIcon(appIcon);
        app.setAppDesc(appDesc);
        app.setStatus(status);
        app.setWebsite(website);
        app.setUserId(userId);
        app.setUserType(userType);
        baseAppService.updateInfo(app);
        return ResultBody.success();
    }


    /**
     * 完善应用开发信息
     *
     * @param appId                应用名称
     * @param grantTypes           授权类型(多个使用,号隔开)
     * @param redirectUrls         第三方应用授权回调地址(多个使用,号隔开)
     * @param scopes               用户授权范围(多个使用,号隔开)
     * @param autoApproveScopes    用户自动授权范围(多个使用,号隔开)
     * @param accessTokenValidity  令牌有效期(秒)
     * @param refreshTokenValidity 刷新令牌有效期(秒)
     * @return
     */
    @ApiOperation(value = "完善应用开发信息", notes = "完善应用开发信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "grantTypes", value = "授权类型(多个使用,号隔开)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "redirectUrls", value = "第三方应用授权回调地址", required = true, paramType = "form"),
            @ApiImplicitParam(name = "scopes", value = "用户授权范围(多个使用,号隔开)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "autoApproveScopes", value = "用户自动授权范围(多个使用,号隔开)", required = false, paramType = "form"),
            @ApiImplicitParam(name = "accessTokenValidity", value = "令牌有效期(秒)", required = true, paramType = "form"),
            @ApiImplicitParam(name = "refreshTokenValidity", value = "刷新令牌有效期(秒)", required = true, paramType = "form")
    })
    @PostMapping("/app/client/update")
    public ResultBody<String> updateAppClientInfo(
            @RequestParam("appId") String appId,
            @RequestParam(value = "grantTypes") String grantTypes,
            @RequestParam(value = "redirectUrls") String redirectUrls,
            @RequestParam(value = "scopes") String scopes,
            @RequestParam(value = "accessTokenValidity", required = true) Integer accessTokenValidity,
            @RequestParam(value = "refreshTokenValidity", required = true) Integer refreshTokenValidity,
            @RequestParam(value = "autoApproveScopes", required = false) String autoApproveScopes
    ) {
        BaseClientDetails client = new BaseClientDetails(appId, "", scopes, grantTypes, "", redirectUrls);
        client.setAccessTokenValiditySeconds(accessTokenValidity);
        client.setRefreshTokenValiditySeconds(refreshTokenValidity);
        client.setAutoApproveScopes(autoApproveScopes != null ? Arrays.asList(autoApproveScopes.split(",")) : null);
        baseAppService.updateAppClientInfo(client);
        return ResultBody.success();
    }


    /**
     * 重置应用秘钥
     *
     * @param appId 应用Id
     * @return
     */
    @ApiOperation(value = "重置应用秘钥", notes = "重置应用秘钥")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
    })
    @PostMapping("/app/reset")
    public ResultBody<String> resetAppSecret(
            @RequestParam("appId") String appId
    ) {
        String result = baseAppService.restSecret(appId);
        return ResultBody.success(result);
    }

    /**
     * 删除应用信息
     *
     * @param appId
     * @return
     */
    @ApiOperation(value = "删除应用信息", notes = "删除应用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用Id", required = true, paramType = "form"),
    })
    @PostMapping("/app/remove")
    public ResultBody removeApp(
            @RequestParam("appId") String appId
    ) {
        baseAppService.removeApp(appId);
        return ResultBody.success();
    }
}
