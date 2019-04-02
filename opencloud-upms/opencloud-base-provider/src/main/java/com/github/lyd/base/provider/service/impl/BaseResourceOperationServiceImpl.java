package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.entity.BaseResourceOperation;
import com.github.lyd.base.client.model.entity.BaseResourceOperationApi;
import com.github.lyd.base.provider.mapper.BaseResourceOperationApiMapper;
import com.github.lyd.base.provider.mapper.BaseResourceOperationMapper;
import com.github.lyd.base.provider.service.BaseAuthorityService;
import com.github.lyd.base.provider.service.BaseResourceOperationService;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class BaseResourceOperationServiceImpl implements BaseResourceOperationService {
    @Autowired
    private BaseResourceOperationMapper baseResourceOperationMapper;
    @Autowired
    private BaseResourceOperationApiMapper baseResourceOperationApiMapper;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseResourceOperation> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseResourceOperation query =  pageParams.mapToObject(BaseResourceOperation.class);
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.criteria()
                .andLikeRight("operationCode", query.getOperationCode())
                .andLikeRight("operationName", query.getOperationName()).end().build();
        example.orderBy("operationId").asc().orderBy("priority").asc();
        List<BaseResourceOperation> list = baseResourceOperationMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @Override
    public List<BaseResourceOperation> findListByMenuId(Long menuId) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.criteria()
                .andEqualTo("menuId", menuId).end().build();
        List<BaseResourceOperation> list = baseResourceOperationMapper.selectByExample(example);
        return list;
    }

    /**
     * 根据主键获取Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public BaseResourceOperation getOperation(Long operationId) {
        return baseResourceOperationMapper.selectByPrimaryKey(operationId);
    }


    /**
     * 检查Operation编码是否存在
     *
     * @param operationCode
     * @return
     */
    @Override
    public Boolean isExist(String operationCode) {
        ExampleBuilder builder = new ExampleBuilder(BaseResourceOperation.class);
        Example example = builder.criteria()
                .andEqualTo("operationCode", operationCode)
                .end().build();
        int count = baseResourceOperationMapper.selectCountByExample(example);
        return count > 0 ? true : false;
    }

    /**
     * 添加Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public BaseResourceOperation addOperation(BaseResourceOperation operation) {
        if (isExist(operation.getOperationCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", operation.getOperationCode()));
        }
        if (operation.getMenuId() == null) {
            operation.setMenuId(0L);
        }
        if (operation.getPriority() == null) {
            operation.setPriority(0);
        }
        if (operation.getStatus() == null) {
            operation.setStatus(BaseConstants.ENABLED);
        }
        if (operation.getIsPersist() == null) {
            operation.setIsPersist(BaseConstants.DISABLED);
        }
        operation.setCreateTime(new Date());
        operation.setServiceId(DEFAULT_SERVICE_ID);
        operation.setUpdateTime(operation.getCreateTime());
        baseResourceOperationMapper.insertSelective(operation);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
        return operation;
    }

    /**
     * 修改Operation操作
     *
     * @param operation
     * @return
     */
    @Override
    public BaseResourceOperation updateOperation(BaseResourceOperation operation) {
        BaseResourceOperation saved = getOperation(operation.getOperationId());
        if (saved == null) {
            throw new OpenAlertException(String.format("%s信息不存在", operation.getOperationId()));
        }
        if (!saved.getOperationCode().equals(operation.getOperationCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(operation.getOperationCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", operation.getOperationCode()));
            }
        }
        if (operation.getMenuId() == null) {
            operation.setMenuId(0L);
        }
        if (operation.getPriority() == null) {
            operation.setPriority(0);
        }
        operation.setUpdateTime(new Date());
        baseResourceOperationMapper.updateByPrimaryKeySelective(operation);
        // 同步权限表里的信息
        baseAuthorityService.saveOrUpdateAuthority(operation.getOperationId(), ResourceType.operation);
        return operation;
    }

    /**
     * 移除Operation
     *
     * @param operationId
     * @return
     */
    @Override
    public void removeOperation(Long operationId) {
        BaseResourceOperation operation = getOperation(operationId);
        if (operation != null && operation.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        removeOperationApi(operationId);
        baseAuthorityService.removeAuthority(operationId, ResourceType.operation);
        baseResourceOperationMapper.deleteByPrimaryKey(operationId);
    }

    /**
     * 操作绑定接口资源
     *
     * @param operationId
     * @param apiIds
     * @return
     */
    @Override
    public void addOperationApi(Long operationId, String... apiIds) {
        if (operationId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeOperationApi(operationId);
        if (apiIds != null && apiIds.length > 0) {
            List<BaseResourceOperationApi> list = Lists.newArrayList();
            for (String api : apiIds) {
                Long apiId = Long.parseLong(api);
                BaseResourceOperationApi item = new BaseResourceOperationApi();
                item.setOperationId(operationId);
                item.setApiId(apiId);
                list.add(item);
            }
            baseResourceOperationApiMapper.insertList(list);
        }
    }

    /**
     * 查询操作已绑定接口
     *
     * @param operationId
     * @return
     */
    @Override
    public List<BaseResourceOperationApi> findOperationApi(Long operationId) {
        BaseResourceOperationApi query = new BaseResourceOperationApi();
        query.setOperationId(operationId);
       return baseResourceOperationApiMapper.select(query);
    }

    /**
     * 移除操作已绑定接口
     * @param operationId
     */
    @Override
    public void removeOperationApi(Long operationId) {
        BaseResourceOperationApi operationApi = new BaseResourceOperationApi();
        operationApi.setOperationId(operationId);
        baseResourceOperationApiMapper.delete(operationApi);
    }
}
