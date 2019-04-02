package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.common.model.ResultBody;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liuyadu
 */
public interface BaseAppRemoteApi {

    /**
     * 获取应用基础信息
     *
     * @param appId 应用Id
     * @return
     */
    @GetMapping("/app/{appId}/info")
    ResultBody<BaseApp> getApp(@PathVariable("appId") String appId);

    /**
     * 获取应用开发配置信息
     * @param appId
     * @return
     */
    @GetMapping("/app/client/{appId}/info")
    ResultBody<BaseClientDetails> getAppClientInfo(@PathVariable("appId") String appId);
}
