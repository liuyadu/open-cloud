package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.base.provider.mapper.BaseUserMapper;
import com.github.lyd.base.provider.service.BaseUserService;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public BaseUser addUser(BaseUser baseUser) {
        baseUserMapper.insertSelective(baseUser);
        return baseUser;
    }

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public BaseUser updateUser(BaseUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return null;
        }
        baseUserMapper.updateByPrimaryKeySelective(baseUser);
        return baseUser;
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseUser> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseUser query = pageParams.mapToObject(BaseUser.class);
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .andEqualTo("userId", query.getUserId())
                .andEqualTo("userType", query.getUserType())
                .andEqualTo("userName", query.getUserName())
                .andEqualTo("mobile", query.getMobile()).end().build();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseUser> findList() {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria().end().build();
        example.orderBy("userId").asc();
        List<BaseUser> list = baseUserMapper.selectByExample(example);
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUser getUserByUserId(Long userId) {
        return baseUserMapper.selectByPrimaryKey(userId);
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getUserByUsername(String username) {
        ExampleBuilder builder = new ExampleBuilder(BaseUser.class);
        Example example = builder.criteria()
                .andEqualTo("userName", username)
                .end().build();
        BaseUser saved = baseUserMapper.selectOneByExample(example);
        return saved;
    }


}
