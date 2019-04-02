package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.AccessAuthority;
import com.github.lyd.base.client.model.BaseMenuAuthority;
import com.github.lyd.common.model.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 权限控制API接口
 *
 * @author liuyadu
 */
public interface BaseAuthorityRemoteApi {
    /**
     * 获取所有访问权限列表
     * @return
     */
    @GetMapping("/authority/access/list")
    ResultBody<List<AccessAuthority>> getAccessAuthorityList();

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @GetMapping("/authority/menu/list")
    ResultBody<List<BaseMenuAuthority>> getMenuAuthorityList();
}
