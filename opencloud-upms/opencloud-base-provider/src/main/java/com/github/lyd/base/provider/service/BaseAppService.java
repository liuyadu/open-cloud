package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * 应用信息管理
 *
 * @author liuyadu
 */
public interface BaseAppService {

    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    PageList<BaseApp> findListPage(PageParams pageParams);

    /**
     * 获取app信息
     *
     * @param appId
     * @return
     */
    BaseApp getAppInfo(String appId);

    /**
     * 获取app和应用信息
     *
     * @param appId
     * @return
     */
    BaseClientDetails getAppClientInfo(String appId);


    /**
     * 更新应用开发新型
     * @param baseClientDetails
     */
    BaseClientDetails updateAppClientInfo(BaseClientDetails baseClientDetails);

    /**
     * 添加应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp addAppInfo(BaseApp app);

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    BaseApp updateInfo(BaseApp app);


    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    String restSecret(String appId);

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    void removeApp(String appId);
}
