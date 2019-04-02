package com.github.lyd.base.provider.service.impl;

import com.github.lyd.auth.client.constants.AuthConstants;
import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.model.entity.BaseApp;
import com.github.lyd.base.provider.mapper.BaseAppMapper;
import com.github.lyd.base.provider.service.BaseAppService;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.utils.BeanConvertUtils;
import com.github.lyd.common.utils.RandomValueUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAppServiceImpl implements BaseAppService {

    @Autowired
    private BaseAppMapper baseAppMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;

    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseApp> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseApp query =  pageParams.mapToObject(BaseApp.class);
        ExampleBuilder builder = new ExampleBuilder(BaseApp.class);
        Example example = builder.criteria()
                .andEqualTo("userId", query.getUserId())
                .andEqualTo("appType", query.getAppType())
                .andEqualTo("appId", query.getAppId())
                .andLikeRight("appName", query.getAppName())
                .andLikeRight("appNameEn", query.getAppName()).end().build();
        List<BaseApp> list = baseAppMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 获取app详情
     *
     * @param appId
     * @return
     */
    @Cacheable(value = "apps", key = "#appId")
    @Override
    public BaseApp getAppInfo(String appId) {
        return baseAppMapper.selectByPrimaryKey(appId);
    }

    /**
     * 获取app和应用信息
     *
     * @param appId
     * @return
     */
    @Override
    @Cacheable(value = "apps", key = "'client:'+#appId")
    public BaseClientDetails getAppClientInfo(String appId) {
        try {
            BaseClientDetails clientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(appId);
            clientDetails.setAuthorities(baseAuthorityService.findAppGrantedAuthority(appId));
            return clientDetails;
        } catch (Exception e) {
            log.error("clientDetailsClient.getClient error:{}", e.getMessage());
        }
        return null;
    }

    /**
     * 更新应用开发新型
     *
     * @param baseClientDetails
     */
    @CachePut(value = "apps", key = "'client:'+#baseClientDetails.clientId")
    @Override
    public BaseClientDetails updateAppClientInfo(BaseClientDetails baseClientDetails) {
        BaseApp app = getAppInfo(baseClientDetails.getClientId());
        Map info = BeanConvertUtils.objectToMap(app);
        baseClientDetails.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(baseClientDetails);
        return baseClientDetails;
    }


    /**
     * 添加应用
     *
     * @param app
     * @return 应用信息
     */
    @CachePut(value = "apps", key = "#app.appId")
    @Override
    public BaseApp addAppInfo(BaseApp app) {
        String clientId = String.valueOf(System.currentTimeMillis());
        String clientSecret = RandomValueUtils.uuid();
        app.setAppId(clientId);
        app.setAppSecret(clientSecret);
        app.setCreateTime(new Date());
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        baseAppMapper.insertSelective(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getAppId());
        client.setClientSecret(app.getAppSecret());
        client.setAdditionalInformation(info);
        client.setAccessTokenValiditySeconds(AuthConstants.ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(AuthConstants.REFRESH_TOKEN_VALIDITY_SECONDS);
        jdbcClientDetailsService.addClientDetails(client);
        return app;
    }

    /**
     * 修改应用
     *
     * @param app 应用
     * @return 应用信息
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    @Override
    public BaseApp updateInfo(BaseApp app) {
        BaseApp appInfo = getAppInfo(app.getAppId());
        if (appInfo == null) {
            throw new OpenAlertException(app.getAppId() + "应用不存在!");
        }
        app.setUpdateTime(new Date());
        baseAppMapper.updateByPrimaryKeySelective(app);
        Map info = BeanConvertUtils.objectToMap(app);
        // 修改客户端信息
        BaseClientDetails client = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(app.getAppId());
        client.setAdditionalInformation(info);
        jdbcClientDetailsService.updateClientDetails(client);
        return app;
    }

    /**
     * 重置秘钥
     *
     * @param appId
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    public String restSecret(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许修改"));
        }
        // 生成新的密钥
        String clientSecret = RandomValueUtils.uuid();
        appInfo.setAppSecret(clientSecret);
        appInfo.setUpdateTime(new Date());
        baseAppMapper.updateByPrimaryKeySelective(appInfo);
        jdbcClientDetailsService.updateClientSecret(appInfo.getAppId(), clientSecret);
        return clientSecret;
    }

    /**
     * 删除应用
     *
     * @param appId
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = {"apps"}, key = "#app.appId"),
            @CacheEvict(value = {"apps"}, key = "'client:'+#app.appId")
    })
    @Override
    public void removeApp(String appId) {
        BaseApp appInfo = getAppInfo(appId);
        if (appInfo == null) {
            throw new OpenAlertException(appId + "应用不存在!");
        }
        if (appInfo.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        // 移除应用权限
        baseAuthorityService.removeAppAuthority(appId);
        baseAppMapper.deleteByPrimaryKey(appInfo.getAppId());
        jdbcClientDetailsService.removeClientDetails(appInfo.getAppId());
    }


}
