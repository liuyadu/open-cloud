package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.entity.BaseAppAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.common.security.OpenGrantedAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseAppAuthorityMapper extends CrudMapper<BaseAppAuthority> {

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    List<OpenGrantedAuthority> selectAppGrantedAuthority(@Param("appId") String appId);
}
