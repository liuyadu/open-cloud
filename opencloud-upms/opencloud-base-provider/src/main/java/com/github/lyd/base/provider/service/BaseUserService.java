package com.github.lyd.base.provider.service;

import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;

import java.util.List;

/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface BaseUserService {

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    BaseUser addUser(BaseUser baseUser);

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    BaseUser updateUser(BaseUser baseUser);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    PageList<BaseUser> findListPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<BaseUser> findList();


    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    BaseUser getUserByUserId(Long userId);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    BaseUser getUserByUsername(String username);

}
