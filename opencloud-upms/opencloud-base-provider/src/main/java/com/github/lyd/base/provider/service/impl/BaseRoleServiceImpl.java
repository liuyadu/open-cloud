package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.model.entity.BaseRole;
import com.github.lyd.base.client.model.entity.BaseRoleUser;
import com.github.lyd.base.client.model.entity.BaseUser;
import com.github.lyd.base.provider.mapper.BaseRoleMapper;
import com.github.lyd.base.provider.mapper.BaseRoleUserMapper;
import com.github.lyd.base.provider.service.BaseRoleService;
import com.github.lyd.base.provider.service.BaseUserService;
import com.github.lyd.common.constants.CommonConstants;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author liuyadu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseRoleServiceImpl implements BaseRoleService {
    @Autowired
    private BaseRoleMapper baseRoleMapper;
    @Autowired
    private BaseRoleUserMapper baseRoleUserMapper;
    @Autowired
    private BaseUserService baseUserService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public PageList<BaseRole> findListPage(PageParams pageParams) {
        PageHelper.startPage(pageParams.getPage(), pageParams.getLimit(), pageParams.getOrderBy());
        BaseRole query = pageParams.mapToObject(BaseRole.class);
        ExampleBuilder builder = new ExampleBuilder(BaseRole.class);
        Example example = builder.criteria()
                .andLikeRight("roleCode", query.getRoleCode())
                .andLikeRight("roleName", query.getRoleName()).end().build();
        example.orderBy("roleId").asc();
        List<BaseRole> list = baseRoleMapper.selectByExample(example);
        return new PageList(list);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseRole> findList() {
        ExampleBuilder builder = new ExampleBuilder(BaseRole.class);
        Example example = builder.criteria().end().build();
        example.orderBy("roleId").asc();
        List<BaseRole> list = baseRoleMapper.selectByExample(example);
        return list;
    }

    /**
     * 获取角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public BaseRole getRole(Long roleId) {
        return baseRoleMapper.selectByPrimaryKey(roleId);
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public BaseRole addRole(BaseRole role) {
        if (isExist(role.getRoleCode())) {
            throw new OpenAlertException(String.format("%s编码已存在!", role.getRoleCode()));
        }
        if (role.getStatus() == null) {
            role.setStatus(BaseConstants.ENABLED);
        }
        if (role.getIsPersist() == null) {
            role.setIsPersist(BaseConstants.DISABLED);
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(role.getCreateTime());
        baseRoleMapper.insertSelective(role);
        return role;
    }

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public BaseRole updateRole(BaseRole role) {
        BaseRole saved = getRole(role.getRoleId());
        if (role == null) {
            throw new OpenAlertException("信息不存在!");
        }
        if (!saved.getRoleCode().equals(role.getRoleCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(role.getRoleCode())) {
                throw new OpenAlertException(String.format("%s编码已存在!", role.getRoleCode()));
            }
        }
        role.setUpdateTime(new Date());
        baseRoleMapper.updateByPrimaryKeySelective(role);
        return role;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    @Override
    public void removeRole(Long roleId) {
        if (roleId == null) {
            return;
        }
        BaseRole role = getRole(roleId);
        if (role != null && role.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许删除"));
        }
        int count = getCountByRole(roleId);
        if (count > 0) {
            throw new OpenAlertException("该角色下存在授权人员,不允许删除!");
        }
        baseRoleMapper.deleteByPrimaryKey(roleId);
    }

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    @Override
    public Boolean isExist(String roleCode) {
        if (StringUtils.isBlank(roleCode)) {
            throw new OpenAlertException("roleCode不能为空!");
        }
        ExampleBuilder builder = new ExampleBuilder(BaseRole.class);
        Example example = builder.criteria().andEqualTo("roleCode", roleCode).end().build();
        return baseRoleMapper.selectCountByExample(example) > 0;
    }

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    @Override
    public void saveUserRoles(Long userId, String... roles) {
        if (userId == null || roles == null) {
            return;
        }
        BaseUser user = baseUserService.getUserByUserId(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new OpenAlertException("默认用户无需分配!");
        }
        // 先清空,在添加
        removeUserRoles(userId);
        if (roles.length > 0) {
            List<BaseRoleUser> list = Lists.newArrayList();
            for (String roleId : roles) {
                BaseRoleUser roleUser = new BaseRoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(Long.parseLong(roleId));
                list.add(roleUser);
            }
            // 批量保存
            baseRoleUserMapper.insertList(list);
        }
    }

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    @Override
    public void saveRoleUsers(Long roleId, String... userIds) {
        if (roleId == null || userIds == null) {
            return;
        }
        // 先清空,在添加
        removeRoleUsers(roleId);
        if (userIds.length > 0) {
            List<BaseRoleUser> list = Lists.newArrayList();
            for (String userId : userIds) {
                BaseRoleUser roleUser = new BaseRoleUser();
                roleUser.setUserId(Long.parseLong(userId));
                roleUser.setRoleId(roleId);
                list.add(roleUser);
            }
            // 批量保存
            baseRoleUserMapper.insertList(list);
        }
    }

    /**
     * 查询角色成员
     *
     * @return
     */
    @Override
    public List<BaseRoleUser> findRoleUsers(Long roleId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria().andEqualTo("roleId", roleId).end().build();
        return baseRoleUserMapper.selectByExample(example);
    }

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    @Override
    public int getCountByRole(Long roleId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria().andEqualTo("roleId", roleId).end().build();
        int result = baseRoleUserMapper.selectCountByExample(example);
        return result;
    }

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCountByUser(Long userId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria().andEqualTo("userId", userId).end().build();
        int result = baseRoleUserMapper.selectCountByExample(example);
        return result;
    }

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    @Override
    public void removeRoleUsers(Long roleId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria().andEqualTo("roleId", roleId).end().build();
        baseRoleUserMapper.deleteByExample(example);
    }

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    @Override
    public void removeUserRoles(Long userId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria().andEqualTo("userId", userId).end().build();
        baseRoleUserMapper.deleteByExample(example);
    }

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Boolean isExist(Long userId, Long roleId) {
        ExampleBuilder builder = new ExampleBuilder(BaseRoleUser.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId)
                .andEqualTo("roleId", roleId)
                .end().build();
        int result = baseRoleUserMapper.selectCountByExample(example);
        return result > 0;
    }


    /**
     * 获取组员角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<BaseRole> getUserRoles(Long userId) {
        List<BaseRole> roles = baseRoleUserMapper.selectUserRoleList(userId);
        return roles;
    }

    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return baseRoleUserMapper.selectUserRoleIdList(userId);
    }


}
