package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.BaseMenuAuthority;
import com.github.lyd.base.client.model.entity.BaseRoleAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.common.security.OpenGrantedAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface BaseRoleAuthorityMapper extends CrudMapper<BaseRoleAuthority> {

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<OpenGrantedAuthority> selectRoleGrantedAuthority(@Param("roleId") Long roleId);

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    List<BaseMenuAuthority> selectRoleMenuAuthority(@Param("roleId") Long roleId);
}
