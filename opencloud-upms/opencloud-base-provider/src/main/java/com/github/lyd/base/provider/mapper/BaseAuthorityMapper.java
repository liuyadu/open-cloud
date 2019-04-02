package com.github.lyd.base.provider.mapper;

import com.github.lyd.base.client.model.AccessAuthority;
import com.github.lyd.base.client.model.BaseApiAuthority;
import com.github.lyd.base.client.model.BaseMenuAuthority;
import com.github.lyd.base.client.model.BaseOperationAuthority;
import com.github.lyd.base.client.model.entity.BaseAuthority;
import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.common.security.OpenGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface BaseAuthorityMapper extends CrudMapper<BaseAuthority> {

    /**
     * 查询所有访问权限列表
     */
    List<AccessAuthority> selectAccessAuthority();


    /**
     * 查询已授权权限列表
     *
     * @param map
     * @return
     */
    List<OpenGrantedAuthority> selectAllGrantedAuthority(Map map);


    /**
     * 获取菜单权限
     *
     * @param map
     * @return
     */
    List<BaseMenuAuthority> selectMenuAuthority(Map map);

    /**
     * 获取操作权限
     *
     * @param map
     * @return
     */
    List<BaseOperationAuthority> selectOperationAuthority(Map map);

    /**
     * 获取API权限
     *
     * @param map
     * @return
     */
    List<BaseApiAuthority> selectApiAuthority(Map map);
}
