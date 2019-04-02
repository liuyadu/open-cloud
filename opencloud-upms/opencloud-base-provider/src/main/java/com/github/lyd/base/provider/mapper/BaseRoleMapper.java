package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.common.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseRoleMapper extends CrudMapper<BaseRole> {

    List<BaseRole> selectRoleList(Map params);
}
