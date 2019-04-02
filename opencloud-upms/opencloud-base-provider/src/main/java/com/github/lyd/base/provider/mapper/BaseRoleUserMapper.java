package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.base.client.model.entity.BaseRoleUser;
import com.github.lyd.common.mapper.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface BaseRoleUserMapper extends CrudMapper<BaseRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<BaseRole> selectUserRoleList(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     * @param userId
     * @return
     */
    List<Long> selectUserRoleIdList(@Param("userId") Long userId);
}
