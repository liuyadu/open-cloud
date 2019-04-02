package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceApi;
import com.github.lyd.base.provider.mapper.BaseResourceApiMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceApiService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseResourceApiServiceImpl implements BaseResourceApiService {
    @Autowired
    private BaseResourceApiMapper baseResourceApiMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseResourceApi> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseResourceApi query = pageParams.mapToObject(BaseResourceApi.class);
        ExampleBuilder builder = new ExampleBuilder(BaseResourceApi.class);
        Example example = builder.criteria()
                .andLikeRight("path", query.getPath())
                .andLikeRight("apiName", query.getApiName())
                .andLikeRight("apiCode", query.getApiCode())
                .andEqualTo("serviceId", query.getServiceId())
                .andEqualTo("status", query.getStatus())
                .andEqualTo("isAuth", query.getIsAuth())
                .andEqualTo("isOpen", query.getIsOpen())
                .end().build();
        example.orderBy("apiId").asc().orderBy("priority").asc();
        List<BaseResourceApi> list = baseResourceApiMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseResourceApi> findAllList(String serviceId) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceApi.class);
        Example example = builder.criteria().andEqualTo("serviceId", serviceId).end().build();
        example.orderBy("apiId").asc().orderBy("priority").asc();
        List<BaseResourceApi> list = baseResourceApiMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据主键获取接口
     *
     * @param apiId
     * @return
     */
    @Override
    public BaseResourceApi getApi(Long apiId) {
        return baseResourceApiMapper.selectByPrimaryKey(apiId);
    }


    @Override
    public Boolean isExist(String apiCode, String serviceId) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceApi.class);
        Example example = builder.criteria()
                .andEqualTo("apiCode", apiCode)
                .andEqualTo("serviceId", serviceId)
                .end().build();
        int count = baseResourceApiMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    @Override
    public BaseResourceApi addApi(BaseResourceApi api) {
        if (isExist(api.getApiCode(), api.getServiceId())) {
            throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getStatus() == null) {
            api.setStatus(BaseConstants.ENABLED);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        if (api.getIsPersist() == null) {
            api.setIsPersist(0);
        }
        if (api.getIsOpen() == null) {
            api.setIsOpen(0);
        }
        if (api.getIsAuth() == null) {
            api.setIsAuth(0);
        }
        api.setCreateTime(new Date());
        api.setUpdateTime(api.getCreateTime());
        baseResourceApiMapper.insertSelective(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
        return api;
    }

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    @Override
    public BaseResourceApi updateApi(BaseResourceApi api) {
        BaseResourceApi saved = getApi(api.getApiId());
        if (saved == null) {
            throw new OpenAlertException("信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode(), api.getServiceId())) {
                throw new OpenAlertException(String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getApiCategory() == null) {
            api.setApiCategory(BaseConstants.DEFAULT_API_CATEGORY);
        }
        api.setUpdateTime(new Date());
        baseResourceApiMapper.updateByPrimaryKeySelective(api);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(api.getApiId(), ResourceType.api);
        return api;
    }

    /**
     * 查询接口
     *
     * @param apiCode
     * @param serviceId
     * @return
     */
    @Override
    public BaseResourceApi getApi(String apiCode, String serviceId) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceApi.class);
        Example example = builder.criteria()
                .andEqualTo("apiCode", apiCode)
                .andEqualTo("serviceId", serviceId)
                .end().build();
        return baseResourceApiMapper.selectOneByExample(example);
    }


    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    @Override
    public void removeApi(Long apiId) {
        BaseResourceApi api = getApi(apiId);
        if (api != null && api.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        baseAuthorityService.removeAuthority(apiId, ResourceType.api);
        baseResourceApiMapper.deleteByPrimaryKey(apiId);
    }


    /**
     * 获取数量
     *
     * @param baseResourceApi
     * @return
     */
    @Override
    public int getCount(BaseResourceApi baseResourceApi) {
        return baseResourceApiMapper.selectCount(baseResourceApi);
    }


}
