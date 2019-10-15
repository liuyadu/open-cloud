package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.base.client.constants.ResourceType;
import com.opencloud.base.client.model.AuthorityApi;
import com.opencloud.base.client.model.AuthorityMenu;
import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.base.client.model.entity.*;
import com.opencloud.base.server.mapper.*;
import com.opencloud.base.server.service.*;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.exception.OpenException;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenAuthority;
import com.opencloud.common.security.OpenHelper;
import com.opencloud.common.security.OpenSecurityConstants;
import com.opencloud.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 系统权限管理
 * 对菜单、操作、API等进行权限分配操作
 *
 * @author liuyadu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAuthorityServiceImpl extends BaseServiceImpl<BaseAuthorityMapper, BaseAuthority> implements BaseAuthorityService {

    @Autowired
    private BaseAuthorityMapper baseAuthorityMapper;
    @Autowired
    private BaseAuthorityRoleMapper baseAuthorityRoleMapper;
    @Autowired
    private BaseAuthorityUserMapper baseAuthorityUserMapper;
    @Autowired
    private BaseAuthorityAppMapper baseAuthorityAppMapper;
    @Autowired
    private BaseAuthorityActionMapper baseAuthorityActionMapper;
    @Autowired
    private BaseMenuService baseMenuService;
    @Autowired
    private BaseActionService baseActionService;
    @Autowired
    private BaseApiService baseApiService;
    @Autowired
    private BaseRoleService baseRoleService;
    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseAppService baseAppService;
    @Autowired
    private RedisTokenStore redisTokenStore;

    @Value("${spring.application.name}")
    private String DEFAULT_SERVICE_ID;

    /**
     * 获取访问权限列表
     *
     * @return
     */
    @Override
    public List<AuthorityResource> findAuthorityResource() {
        List<AuthorityResource> list = Lists.newArrayList();
        // 已授权资源权限
        List<AuthorityResource> resourceList = baseAuthorityMapper.selectAllAuthorityResource();
        if (resourceList != null) {
            list.addAll(resourceList);
        }
        return list;
    }

    /**
     * 获取菜单权限列表
     *
     * @return
     */
    @Override
    public List<AuthorityMenu> findAuthorityMenu(Integer status) {
        Map map = Maps.newHashMap();
        map.put("status", status);
        List<AuthorityMenu> authorities = baseAuthorityMapper.selectAuthorityMenu(map);
        authorities.sort((AuthorityMenu h1, AuthorityMenu h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return authorities;

    }

    @Override
    public List<AuthorityApi> findAuthorityApi(String serviceId) {
        Map map = Maps.newHashMap();
        map.put("serviceId", serviceId);
        map.put("status", 1);
        List<AuthorityApi> authorities = baseAuthorityMapper.selectAuthorityApi(map);
        return authorities;

    }

    /**
     * 查询功能按钮权限列表
     *
     * @param actionId
     * @return
     */
    @Override
    public List<BaseAuthorityAction> findAuthorityAction(Long actionId) {
        QueryWrapper<BaseAuthorityAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAuthorityAction::getActionId, actionId);
        return baseAuthorityActionMapper.selectList(queryWrapper);
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
            BaseMenu menu = baseMenuService.getMenu(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_MENU + menu.getMenuCode();
            baseAuthority.setMenuId(resourceId);
            baseAuthority.setStatus(menu.getStatus());
        }
        if (ResourceType.action.equals(resourceType)) {
            BaseAction operation = baseActionService.getAction(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_ACTION + operation.getActionCode();
            baseAuthority.setActionId(resourceId);
            baseAuthority.setStatus(operation.getStatus());
        }
        if (ResourceType.api.equals(resourceType)) {
            BaseApi api = baseApiService.getApi(resourceId);
            authority = OpenSecurityConstants.AUTHORITY_PREFIX_API + api.getApiCode();
            baseAuthority.setApiId(resourceId);
            baseAuthority.setStatus(api.getStatus());
        }
        if (authority == null) {
            return null;
        }
        // 设置权限标识
        baseAuthority.setAuthority(authority);
        if (baseAuthority.getAuthorityId() == null) {
            baseAuthority.setCreateTime(new Date());
            baseAuthority.setUpdateTime(baseAuthority.getCreateTime());
            // 新增权限
            baseAuthorityMapper.insert(baseAuthority);
        } else {
            // 修改权限
            baseAuthority.setUpdateTime(new Date());
            baseAuthorityMapper.updateById(baseAuthority);
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
    public void removeAuthority(Long resourceId, ResourceType resourceType) {
        if (isGranted(resourceId, resourceType)) {
            throw new OpenAlertException(String.format("资源已被授权,不允许删除!取消授权后,再次尝试!"));
        }
        QueryWrapper<BaseAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        baseAuthorityMapper.delete(queryWrapper);
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
        QueryWrapper<BaseAuthority> queryWrapper = buildQueryWrapper(resourceId, resourceType);
        return baseAuthorityMapper.selectOne(queryWrapper);
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
        if (authority == null || authority.getAuthorityId() == null) {
            return false;
        }
        QueryWrapper<BaseAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda().eq(BaseAuthorityRole::getAuthorityId, authority.getAuthorityId());
        int roleGrantedCount = baseAuthorityRoleMapper.selectCount(roleQueryWrapper);
        QueryWrapper<BaseAuthorityUser> userQueryWrapper = new QueryWrapper();
        userQueryWrapper.lambda().eq(BaseAuthorityUser::getAuthorityId, authority.getAuthorityId());
        int userGrantedCount = baseAuthorityUserMapper.selectCount(userQueryWrapper);
        QueryWrapper<BaseAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda().eq(BaseAuthorityApp::getAuthorityId, authority.getAuthorityId());
        int appGrantedCount = baseAuthorityAppMapper.selectCount(appQueryWrapper);
        return roleGrantedCount > 0 || userGrantedCount > 0 || appGrantedCount > 0;
    }

    /**
     * 构建权限对象
     *
     * @param resourceId
     * @param resourceType
     * @return
     */
    private QueryWrapper<BaseAuthority> buildQueryWrapper(Long resourceId, ResourceType resourceType) {
        QueryWrapper<BaseAuthority> queryWrapper = new QueryWrapper();
        if (ResourceType.menu.equals(resourceType)) {
            queryWrapper.lambda().eq(BaseAuthority::getMenuId, resourceId);
        }
        if (ResourceType.action.equals(resourceType)) {
            queryWrapper.lambda().eq(BaseAuthority::getActionId, resourceId);
        }
        if (ResourceType.api.equals(resourceType)) {
            queryWrapper.lambda().eq(BaseAuthority::getApiId, resourceId);
        }
        return queryWrapper;
    }


    /**
     * 移除应用权限
     *
     * @param appId
     */
    @Override
    public void removeAuthorityApp(String appId) {
        QueryWrapper<BaseAuthorityApp> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(BaseAuthorityApp::getAppId, appId);
        baseAuthorityAppMapper.delete(queryWrapper);
    }

    /**
     * 移除功能按钮权限
     *
     * @param actionId
     */
    @Override
    public void removeAuthorityAction(Long actionId) {
        QueryWrapper<BaseAuthorityAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAuthorityAction::getActionId, actionId);
        baseAuthorityActionMapper.delete(queryWrapper);
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
    public void addAuthorityRole(Long roleId, Date expireTime, String... authorityIds) {
        if (roleId == null) {
            return;
        }
        // 清空角色已有授权
        QueryWrapper<BaseAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda().eq(BaseAuthorityRole::getRoleId, roleId);
        baseAuthorityRoleMapper.delete(roleQueryWrapper);
        BaseAuthorityRole authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                authority = new BaseAuthorityRole();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setRoleId(roleId);
                authority.setExpireTime(expireTime);
                authority.setCreateTime(new Date());
                authority.setUpdateTime(authority.getCreateTime());
                // 批量添加授权
                baseAuthorityRoleMapper.insert(authority);
            }

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
    public void addAuthorityUser(Long userId, Date expireTime, String... authorityIds) {
        if (userId == null) {
            return;
        }
        BaseUser user = baseUserService.getUserById(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new OpenAlertException("默认用户无需授权!");
        }
        // 获取用户角色列表
        List<Long> roleIds = baseRoleService.getUserRoleIds(userId);
        // 清空用户已有授权
        // 清空角色已有授权
        QueryWrapper<BaseAuthorityUser> userQueryWrapper = new QueryWrapper();
        userQueryWrapper.lambda().eq(BaseAuthorityUser::getUserId, userId);
        baseAuthorityUserMapper.delete(userQueryWrapper);
        BaseAuthorityUser authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                if (roleIds != null && roleIds.size() > 0) {
                    // 防止重复授权
                    if (isGrantedByRoleIds(id, roleIds.toArray(new Long[roleIds.size()]))) {
                        continue;
                    }
                }
                authority = new BaseAuthorityUser();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setUserId(userId);
                authority.setExpireTime(expireTime);
                authority.setCreateTime(new Date());
                authority.setUpdateTime(authority.getCreateTime());
                baseAuthorityUserMapper.insert(authority);
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
    public void addAuthorityApp(String appId, Date expireTime, String... authorityIds) {
        if (appId == null) {
            return;
        }
        BaseApp baseApp = baseAppService.getAppInfo(appId);
        if (baseApp == null) {
            return;
        }
        // 清空应用已有授权
        QueryWrapper<BaseAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda().eq(BaseAuthorityApp::getAppId, appId);
        baseAuthorityAppMapper.delete(appQueryWrapper);
        BaseAuthorityApp authority = null;
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                authority = new BaseAuthorityApp();
                authority.setAuthorityId(Long.parseLong(id));
                authority.setAppId(appId);
                authority.setExpireTime(expireTime);
                authority.setCreateTime(new Date());
                authority.setUpdateTime(authority.getCreateTime());
                baseAuthorityAppMapper.insert(authority);

            }
        }
        // 获取应用最新的权限列表
        List<OpenAuthority> authorities = findAuthorityByApp(appId);
        // 动态更新tokenStore客户端
        OpenHelper.updateOpenClientAuthorities(redisTokenStore,baseApp.getApiKey(),authorities);
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
    public void addAuthorityApp(String appId, Date expireTime, String authorityId) {
        BaseAuthorityApp authority = new BaseAuthorityApp();
        authority.setAppId(appId);
        authority.setAuthorityId(Long.parseLong(authorityId));
        authority.setExpireTime(expireTime);
        authority.setCreateTime(new Date());
        authority.setUpdateTime(authority.getCreateTime());
        QueryWrapper<BaseAuthorityApp> appQueryWrapper = new QueryWrapper();
        appQueryWrapper.lambda()
                .eq(BaseAuthorityApp::getAppId, appId)
                .eq(BaseAuthorityApp::getAuthorityId, authorityId);
        int count = baseAuthorityAppMapper.selectCount(appQueryWrapper);
        if (count > 0) {
            return;
        }
        authority.setCreateTime(new Date());
        baseAuthorityAppMapper.insert(authority);
    }

    /**
     * 添加功能按钮权限
     *
     * @param actionId
     * @param authorityIds
     * @return
     */
    @Override
    public void addAuthorityAction(Long actionId, String... authorityIds) {
        if (actionId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeAuthorityAction(actionId);
        if (authorityIds != null && authorityIds.length > 0) {
            for (String id : authorityIds) {
                Long authorityId = Long.parseLong(id);
                BaseAuthorityAction authority = new BaseAuthorityAction();
                authority.setActionId(actionId);
                authority.setAuthorityId(authorityId);
                authority.setCreateTime(new Date());
                authority.setUpdateTime(authority.getCreateTime());
                baseAuthorityActionMapper.insert(authority);
            }
        }
    }

    /**
     * 获取应用已授权权限
     *
     * @param appId
     * @return
     */
    @Override
    public List<OpenAuthority> findAuthorityByApp(String appId) {
        List<OpenAuthority> authorities = Lists.newArrayList();
        List<OpenAuthority> list = baseAuthorityAppMapper.selectAuthorityByApp(appId);
        if (list != null) {
            authorities.addAll(list);
        }
        return authorities;
    }

    /**
     * 获取角色已授权权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<OpenAuthority> findAuthorityByRole(Long roleId) {
        return baseAuthorityRoleMapper.selectAuthorityByRole(roleId);
    }

    /**
     * 获取所有可用权限
     *
     * @param type = null 查询全部  type = 1 获取菜单和操作 type = 2 获取API
     * @return
     */
    @Override
    public List<OpenAuthority> findAuthorityByType(String type) {
        Map map = Maps.newHashMap();
        map.put("type", type);
        map.put("status", 1);
        return baseAuthorityMapper.selectAuthorityAll(map);
    }

    /**
     * 获取用户已授权权限
     *
     * @param userId
     * @param root   超级管理员
     * @return
     */
    @Override
    public List<OpenAuthority> findAuthorityByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityByType("1");
        }
        List<OpenAuthority> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<OpenAuthority> roleGrantedAuthority = findAuthorityByRole(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<OpenAuthority> userGrantedAuthority = baseAuthorityUserMapper.selectAuthorityByUser(userId);
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
    public List<AuthorityMenu> findAuthorityMenuByUser(Long userId, Boolean root) {
        if (root) {
            // 超级管理员返回所有
            return findAuthorityMenu(null);
        }
        // 用户权限列表
        List<AuthorityMenu> authorities = Lists.newArrayList();
        List<BaseRole> rolesList = baseRoleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                // 加入角色已授权
                List<AuthorityMenu> roleGrantedAuthority = baseAuthorityRoleMapper.selectAuthorityMenuByRole(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<AuthorityMenu> userGrantedAuthority = baseAuthorityUserMapper.selectAuthorityMenuByUser(userId);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        //根据优先级从小到大排序
        authorities.sort((AuthorityMenu h1, AuthorityMenu h2) -> h1.getPriority().compareTo(h2.getPriority()));
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
    public Boolean isGrantedByRoleIds(String authorityId, Long... roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            throw new OpenException("roleIds is empty");
        }
        QueryWrapper<BaseAuthorityRole> roleQueryWrapper = new QueryWrapper();
        roleQueryWrapper.lambda()
                .in(BaseAuthorityRole::getRoleId, roleIds)
                .eq(BaseAuthorityRole::getAuthorityId, authorityId);
        int count = baseAuthorityRoleMapper.selectCount(roleQueryWrapper);
        return count > 0;
    }

    /**
     * 清理无效数据
     *
     * @param serviceId
     * @param codes
     */
    @Override
    public void clearInvalidApi(String serviceId, Collection<String> codes) {
        if (StringUtils.isBlank(serviceId)) {
            return;
        }
        List<String> invalidApiIds = baseApiService.listObjs(new QueryWrapper<BaseApi>().select("api_id").eq("service_id", serviceId).notIn(codes!=null&&!codes.isEmpty(),"api_code", codes), e -> e.toString());
        if (invalidApiIds != null) {
            // 防止删除默认api
            invalidApiIds.remove("1");
            invalidApiIds.remove("2");
            // 获取无效的权限
            if (invalidApiIds.isEmpty()) {
                return;
            }
            List<String> invalidAuthorityIds = listObjs(new QueryWrapper<BaseAuthority>().select("authority_id").in("api_id", invalidApiIds), e -> e.toString());
            if (invalidAuthorityIds != null && !invalidAuthorityIds.isEmpty()) {
                // 移除关联数据
                baseAuthorityAppMapper.delete(new QueryWrapper<BaseAuthorityApp>().in("authority_id", invalidAuthorityIds));
                baseAuthorityActionMapper.delete(new QueryWrapper<BaseAuthorityAction>().in("authority_id", invalidAuthorityIds));
                baseAuthorityRoleMapper.delete(new QueryWrapper<BaseAuthorityRole>().in("authority_id", invalidAuthorityIds));
                baseAuthorityUserMapper.delete(new QueryWrapper<BaseAuthorityUser>().in("authority_id", invalidAuthorityIds));
                // 移除权限数据
                this.removeByIds(invalidAuthorityIds);
                // 移除接口资源
                baseApiService.removeByIds(invalidApiIds);
            }
        }
    }
}
