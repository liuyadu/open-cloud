package com.github.lyd.base.provider.service.impl;

import com.github.lyd.base.client.constants.BaseConstants;
import com.github.lyd.base.client.constants.ResourceType;
import com.github.lyd.base.client.model.AccessAuthority;
import com.github.lyd.base.client.model.BaseApiAuthority;
import com.github.lyd.base.client.model.BaseMenuAuthority;
import com.github.lyd.base.client.model.entity.*;
import com.github.lyd.base.provider.mapper.BaseAppAuthorityMapper;
import com.github.lyd.base.provider.mapper.BaseAuthorityMapper;
import com.github.lyd.base.provider.mapper.BaseRoleAuthorityMapper;
import com.github.lyd.base.provider.mapper.BaseUserAuthorityMapper;
import com.github.lyd.base.provider.service.*;
import com.github.lyd.common.constants.CommonConstants;
import com.github.lyd.common.exception.OpenAlertException;
import com.github.lyd.common.exception.OpenException;
import com.github.lyd.common.mapper.ExampleBuilder;
import com.github.lyd.common.security.OpenGrantedAuthority;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * 系统权限管理
 * 对菜单、操作、API等进行权限分配操作
 *
 * @author liuyadu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAuthorityServiceImpl implements BaseAuthorityService {

    @Autowired
    private BaseAuthorityMapper baseAuthorityMapper;
    @Autowired
    private BaseRoleAuthorityMapper baseRoleAuthorityMapper;
    @Autowired
    private BaseUserAuthorityMapper baseUserAuthorityMapper;
    @Autowired
    private BaseAppAuthorityMapper baseAppAuthorityMapper;
    @Autowired
    private BaseResourceMenuService baseResourceMenuService;
    @Autowired
    private BaseResourceOperationService baseResourceOperationService;
    @Autowired
    private BaseResourceApiService baseResourceApiService;
    @Autowired
    private BaseRoleService baseRoleService;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseAppService baseAppService;

    private final static String SEPARATOR = ":";

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 获取访问权限列表
     *
     * @return
     */
    @Override
    public List<AccessAuthority> findAccessAuthority() {
        return baseAuthorityMapper.selectAccessAuthority();
    }

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @Override
    public List<BaseMenuAuthority> findMenuAuthority(Integer status) {
        Map map = Maps.newHashMap();
        map.put("status", status);
        List<BaseMenuAuthority> authorities = baseAuthorityMapper.selectMenuAuthority(map);
        return authorities;

    }

    @Override
    public List<BaseApiAuthority> findApiAuthority(Integer isOpen, String serviceId) {
        Map map = Maps.newHashMap();
        map.put("serviceId", serviceId);
        map.put("status", 1);
        map.put("isOpen", isOpen);
        List<BaseApiAuthority> authorities = baseAuthorityMapper.selectApiAuthority(map);
        return authorities;

    }


    /**
     * 保存或修改权限
     *
     * @param resourceId
     * @param resourceType
     * @return 权限Id
     */
    @Override
    public BaseAuthority saveOrUpdateAuthority(Long resourceId, ResourceType resourceType) {
        BaseAuthority baseAuthority = getAuthority(resourceId, resourceType);
        String authority = null;
        if (baseAuthority == null) {
            baseAuthority = new BaseAuthority();
        }
        if (ResourceType.menu.equals(resourceType)) {
            BaseResourceMenu menu = baseResourceMenuService.getMenu(resourceId);
            authority = ResourceType.menu.name() + SEPARATOR + menu.getMenuCode();
            baseAuthority.setMenuId(resourceId);
            baseAuthority.setStatus(menu.getStatus());
        }
        if (ResourceType.operation.equals(resourceType)) {
            BaseResourceOperation operation = baseResourceOperationService.getOperation(resourceId);
            authority = ResourceType.operation.name() + SEPARATOR + operation.getOperationCode();
            baseAuthority.setOperationId(resourceId);
            baseAuthority.setStatus(operation.getStatus());
        }
        if (ResourceType.api.equals(resourceType)) {
            BaseResourceApi api = baseResourceApiService.getApi(resourceId);
            authority = api.getApiCode();
            baseAuthority.setApiId(resourceId);
            baseAuthority.setStatus(api.getStatus());
        }
        if (authority == null) {
            return null;
        }
        // 设置权限标识
        baseAuthority.setAuthority(authority);
        if (baseAuthority.getAuthorityId() == null) {
            // 新增权限
            baseAuthorityMapper.insertSelective(baseAuthority);
        } else {
            // 修改权限
            baseAuthorityMapper.updateByPrimaryKeySelective(baseAuthority);
        }
        return baseAuthority;
    }

    /**
     * 移除权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public Long removeAuthority(Long resourceId, ResourceType resourceType) {
        if (isGranted(resourceId, resourceType)) {
            throw new OpenAlertException(String.format("资源已被授权,不允许删除!取消授权后,再次尝试!"));
        }
        BaseAuthority authority = buildAuthority(resourceId, resourceType);
        if (authority == null) {
            return null;
        }
        baseAuthorityMapper.delete(authority);
        return authority.getAuthorityId();
    }

    /**
     * 获取权限
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public BaseAuthority getAuthority(Long resourceId, ResourceType resourceType) {
        if (resourceId == null || resourceType == null) {
            return null;
        }
        BaseAuthority authority = buildAuthority(resourceId, resourceType);
        if (authority == null) {
            return null;
        }
        return baseAuthorityMapper.selectOne(authority);
    }

    /**
     * 是否已被授权
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    @Override
    public Boolean isGranted(Long resourceId, ResourceType resourceType) {
        BaseAuthority authority = getAuthority(resourceId, resourceType);
        if (authority == null || authority.getAuthorityId()==null) {
            return false;
        }
        BaseRoleAuthority roleAuthority = new BaseRoleAuthority();
        roleAuthority.setAuthorityId(authority.getAuthorityId());
        int roleGrantedCount = baseRoleAuthorityMapper.selectCount(roleAuthority);
        BaseUserAuthority userAuthority = new BaseUserAuthority();
        userAuthority.setAuthorityId(authority.getAuthorityId());
        int userGrantedCount = baseUserAuthorityMapper.selectCount(userAuthority);
        BaseAppAuthority appAuthority = new BaseAppAuthority();
        appAuthority.setAuthorityId(authority.getAuthorityId());
        int appGrantedCount = baseAppAuthorityMapper.selectCount(appAuthority);
        return roleGrantedCount > 0 || userGrantedCount > 0 || appGrantedCount > 0;
    }

    /**
     * 构建权限对象
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    private BaseAuthority buildAuthority(Long resourceId, ResourceType resourceType) {
        BaseAuthority authority = null;
        if (ResourceType.menu.equals(resourceType)) {
            authority = new BaseAuthority();
            authority.setMenuId(resourceId);
        }
        if (ResourceType.operation.equals(resourceType)) {
            authority = new BaseAuthority();
            authority.setOperationId(resourceId);
        }
        if (ResourceType.api.equals(resourceType)) {
            authority = new BaseAuthority();
            authority.setApiId(resourceId);
        }
        return authority;
    }


    /**
     * 移除应用权限
     *
     * @param appId
     */
    @Override
    public void removeAppAuthority(String appId) {
        BaseAppAuthority baseAppAuthority = new BaseAppAuthority();
        baseAppAuthority.setAppId(appId);
        baseAppAuthorityMapper.delete(baseAppAuthority);
    }


    /**
     * 角色授权
     *
     * @param roleId       角色ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @Override
    public void addRoleAuthority(Long roleId, Date expireTime, String... authorityIds) {
        if (roleId == null) {
            return;
        }
        // 清空角色已有授权
        ExampleBuilder builder = new ExampleBuilder(BaseRoleAuthority.class);
        Example example = builder.criteria()
                .andEqualTo("roleId", roleId).end().build();
        baseRoleAuthorityMapper.deleteByExample(example);
        List<BaseRoleAuthority> authorities = Lists.newArrayList();
        BaseRoleAuthority authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                authority = new BaseRoleAuthority();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setRoleId(roleId);
                authority.setExpireTime(expireTime);
                authorities.add(authority);
            }
            // 批量添加授权
            baseRoleAuthorityMapper.insertList(authorities);
        }
    }

    /**
     * 用户授权
     *
     * @param userId       用户ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @Override
    public void addUserAuthority(Long userId, Date expireTime, String... authorityIds) {
        if (userId == null) {
            return;
        }
        BaseUser user = baseUserService.getUserByUserId(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new OpenAlertException("默认用户无需授权!");
        }
        // 获取用户角色列表
        List<Long> roleIds = baseRoleService.getUserRoleIds(userId);
        // 清空用户已有授权
        ExampleBuilder builder = new ExampleBuilder(BaseUserAuthority.class);
        Example example = builder.criteria()
                .andEqualTo("userId", userId).end().build();
        baseUserAuthorityMapper.deleteByExample(example);
        List<BaseUserAuthority> authorities = Lists.newArrayList();
        BaseUserAuthority authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                if (roleIds != null && roleIds.size() > 0) {
                    // 防止重复授权
                    if (isGrantByRoles(id, roleIds.toArray(new Long[roleIds.size()]))) {
                        continue;
                    }
                }
                authority = new BaseUserAuthority();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setUserId(userId);
                authority.setExpireTime(expireTime);
                authorities.add(authority);
            }
            if (!authorities.isEmpty()) {
                // 批量添加授权
                baseUserAuthorityMapper.insertList(authorities);
            }
        }
    }

    /**
     * 应用授权
     *
     * @param appId        应用ID
     * @param expireTime   过期时间,null表示长期,不限制
     * @param authorityIds 权限集合
     * @return
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAppAuthority(String appId, Date expireTime, String... authorityIds) {
        if (appId == null) {
            return;
        }
        BaseApp baseApp = baseAppService.getAppInfo(appId);
        if (baseApp == null) {
            return;
        }
        if (baseApp.getIsPersist().equals(BaseConstants.ENABLED)) {
            throw new OpenAlertException(String.format("保留数据,不允许授权"));
        }
        // 清空应用已有授权
        BaseAppAuthority appAuthority = new BaseAppAuthority();
        appAuthority.setAppId(appId);
        baseAppAuthorityMapper.delete(appAuthority);
        List<BaseAppAuthority> authorities = Lists.newArrayList();
        BaseAppAuthority authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                authority = new BaseAppAuthority();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setAppId(appId);
                authority.setExpireTime(expireTime);
                authorities.add(authority);
            }
            // 批量添加授权
            baseAppAuthorityMapper.insertList(authorities);
        }
    }

    /**
     * 应用授权-添加单个权限
     *
     * @param appId
     * @param expireTime
     * @param authorityId
     */
    @CacheEvict(value = {"apps"}, key = "'client:'+#appId")
    @Override
    public void addAppAuthority(String appId, Date expireTime, String authorityId) {
        BaseAppAuthority appAuthority = new BaseAppAuthority();
        appAuthority.setAppId(appId);
        appAuthority.setAuthorityId(Long.parseLong(authorityId));
        int count = baseAppAuthorityMapper.selectCount(appAuthority);
        if (count > 0) {
            return;
        }
        appAuthority.setExpireTime(expireTime);
        baseAppAuthorityMapper.insertSelective(appAuthority);
    }

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    @Override
    public List<OpenGrantedAuthority> findAppGrantedAuthority(String appId) {
        return baseAppAuthorityMapper.selectAppGrantedAuthority(appId);
    }

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<OpenGrantedAuthority> findRoleGrantedAuthority(Long roleId) {
        return baseRoleAuthorityMapper.selectRoleGrantedAuthority(roleId);
    }

    /**
     * 获取所有可用权限
     *
     * @param type = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @return
     */
    @Override
    public List<OpenGrantedAuthority> findGrantedAuthority(String type) {
        Map map = Maps.newHashMap();
        map.put("type", type);
        map.put("status", 1);
        return baseAuthorityMapper.selectAllGrantedAuthority(map);
    }

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @param root   超级管理员
     * @return
     */
    @Override
    public List<OpenGrantedAuthority> findUserGrantedAuthority(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findGrantedAuthority("1");
        }
        List<OpenGrantedAuthority> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<OpenGrantedAuthority> roleGrantedAuthority = findRoleGrantedAuthority(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<OpenGrantedAuthority> userGrantedAuthority = baseUserAuthorityMapper.selectUserGrantedAuthority(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        return authorities;
    }

    /**
     * 获取用户已授权权限详情
     *
     * @param userId
     * @param root   超级管理员
     * @return
     */
    @Override
    public List<BaseMenuAuthority> findUserMenuAuthority(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findMenuAuthority(null);
        }
        // 用户权限列表
        List<BaseMenuAuthority> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<BaseMenuAuthority> roleGrantedAuthority = baseRoleAuthorityMapper.selectRoleMenuAuthority(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<BaseMenuAuthority> userGrantedAuthority = baseUserAuthorityMapper.selectUserMenuAuthority(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        return authorities;
    }

    /**
     * 检测权限是否被多个角色授权
     *
     * @param authorityId
     * @param roleIds
     * @return
     */
    @Override
    public Boolean isGrantByRoles(String authorityId, Long... roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            throw new OpenException("roleIds is empty");
        }
        ExampleBuilder builder = new ExampleBuilder(BaseRoleAuthority.class);
        Example example = builder.criteria().andIn("roleId", roleIds).andEqualTo("authorityId", authorityId).end().build();
        int count = baseRoleAuthorityMapper.selectCountByExample(example);
        return count > 0;
    }


}
