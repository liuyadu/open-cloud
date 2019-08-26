package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opencloud.base.server.mapper.BaseUserMapper;
import com.opencloud.base.server.service.BaseAccountService;
import com.opencloud.base.server.service.BaseRoleService;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.UserAccount;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseAccountLogs;
import com.opencloud.base.client.model.entity.BaseRole;
import com.opencloud.base.client.model.entity.BaseUser;
import com.opencloud.base.server.service.BaseAuthorityService;
import com.opencloud.base.server.service.BaseUserService;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenAlertException;
import com.opencloud.common.model.PageParams;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.common.security.OpenAuthority;
import com.opencloud.common.security.OpenSecurityConstants;
import com.opencloud.common.utils.StringUtils;
import com.opencloud.common.utils.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseUserServiceImpl extends BaseServiceImpl<BaseUserMapper, BaseUser> implements BaseUserService {

    @Autowired
    private BaseUserMapper baseUserMapper;
    @Autowired
    private BaseRoleService roleService;
    @Autowired
    private BaseAuthorityService baseAuthorityService;
    @Autowired
    private BaseAccountService baseAccountService;

    private final String ACCOUNT_DOMAIN = BaseConstants.ACCOUNT_DOMAIN_ADMIN;

    /**
     * 添加系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void addUser(BaseUser baseUser) {
        if(getUserByUsername(baseUser.getUserName())!=null){
            throw new OpenAlertException("用户名:"+baseUser.getUserName()+"已存在!");
        }
        baseUser.setCreateTime(new Date());
        baseUser.setUpdateTime(baseUser.getCreateTime());
        //保存系统用户信息
        baseUserMapper.insert(baseUser);
        //默认注册用户名账户
        baseAccountService.register(baseUser.getUserId(), baseUser.getUserName(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_USERNAME, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        if (StringUtils.matchEmail(baseUser.getEmail())) {
            //注册email账号登陆
            baseAccountService.register(baseUser.getUserId(), baseUser.getEmail(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_EMAIL, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (StringUtils.matchMobile(baseUser.getMobile())) {
            //注册手机号账号登陆
            baseAccountService.register(baseUser.getUserId(), baseUser.getMobile(), baseUser.getPassword(), BaseConstants.ACCOUNT_TYPE_MOBILE, baseUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
    }

    /**
     * 更新系统用户
     *
     * @param baseUser
     * @return
     */
    @Override
    public void updateUser(BaseUser baseUser) {
        if (baseUser == null || baseUser.getUserId() == null) {
            return;
        }
        if (baseUser.getStatus() != null) {
            baseAccountService.updateStatusByUserId(baseUser.getUserId(), ACCOUNT_DOMAIN, baseUser.getStatus());
        }
        baseUserMapper.updateById(baseUser);
    }

    /**
     * 添加第三方登录用户
     *
     * @param baseUser
     * @param accountType
     */
    @Override
    public void addUserThirdParty(BaseUser baseUser, String accountType) {
        if (!baseAccountService.isExist(baseUser.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            baseUser.setUserType(BaseConstants.USER_TYPE_ADMIN);
            baseUser.setCreateTime(new Date());
            baseUser.setUpdateTime(baseUser.getCreateTime());
            //保存系统用户信息
            baseUserMapper.insert(baseUser);
            // 注册账号信息
            baseAccountService.register(baseUser.getUserId(), baseUser.getUserName(), baseUser.getPassword(), accountType, BaseConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public void updatePassword(Long userId, String password) {
        baseAccountService.updatePasswordByUserId(userId, ACCOUNT_DOMAIN, password);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<BaseUser> findListPage(PageParams pageParams) {
        BaseUser query = pageParams.mapToObject(BaseUser.class);
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), BaseUser::getUserId, query.getUserId())
                .eq(ObjectUtils.isNotEmpty(query.getUserType()), BaseUser::getUserType, query.getUserType())
                .eq(ObjectUtils.isNotEmpty(query.getUserName()), BaseUser::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getMobile()), BaseUser::getMobile, query.getMobile());
        queryWrapper.orderByDesc("create_time");
        return baseUserMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<BaseUser> findAllList() {
        List<BaseUser> list = baseUserMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 依据系统用户Id查询系统用户信息
     *
     * @param userId
     * @return
     */
    @Override
    public BaseUser getUserById(Long userId) {
        return baseUserMapper.selectById(userId);
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    @Override
    public UserAccount getUserAccount(Long userId) {
        // 用户权限列表
        List<OpenAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map> roles = Lists.newArrayList();
        List<BaseRole> rolesList = roleService.getUserRoles(userId);
        if (rolesList != null) {
            for (BaseRole role : rolesList) {
                Map roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                OpenAuthority authority = new OpenAuthority(role.getRoleId().toString(), OpenSecurityConstants.AUTHORITY_PREFIX_ROLE + role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
        }

        //查询系统用户资料
        BaseUser baseUser = getUserById(userId);

        // 加入用户权限
        List<OpenAuthority> userGrantedAuthority = baseAuthorityService.findAuthorityByUser(userId, CommonConstants.ROOT.equals(baseUser.getUserName()));
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        UserAccount userAccount = new UserAccount();
        // 昵称
        userAccount.setNickName(baseUser.getNickName());
        // 头像
        userAccount.setAvatar(baseUser.getAvatar());
        // 权限信息
        userAccount.setAuthorities(authorities);
        userAccount.setRoles(roles);
        return userAccount;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public BaseUser getUserByUsername(String username) {
        QueryWrapper<BaseUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseUser::getUserName, username);
        BaseUser saved = baseUserMapper.selectOne(queryWrapper);
        return saved;
    }


    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserAccount login(String account) {
        if (StringUtils.isBlank(account)) {
            return null;
        }
        Map<String, String> parameterMap = WebUtils.getParameterMap(WebUtils.getHttpServletRequest());
        // 第三方登录标识
        String loginType = parameterMap.get("login_type");
        BaseAccount baseAccount = null;
        if (StringUtils.isNotBlank(loginType)) {
            baseAccount = baseAccountService.getAccount(account, loginType, ACCOUNT_DOMAIN);
        } else {
            // 非第三方登录

            //用户名登录
            baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);

            // 手机号登陆
            if (StringUtils.matchMobile(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }
            // 邮箱登陆
            if (StringUtils.matchEmail(account)) {
                baseAccount = baseAccountService.getAccount(account, BaseConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }
        }

        // 获取用户详细信息
        if (baseAccount != null) {
            //添加登录日志
            try {
                HttpServletRequest request = WebUtils.getHttpServletRequest();
                if (request != null) {
                    BaseAccountLogs log = new BaseAccountLogs();
                    log.setDomain(ACCOUNT_DOMAIN);
                    log.setUserId(baseAccount.getUserId());
                    log.setAccount(baseAccount.getAccount());
                    log.setAccountId(String.valueOf(baseAccount.getAccountId()));
                    log.setAccountType(baseAccount.getAccountType());
                    log.setLoginIp(WebUtils.getRemoteAddress(request));
                    log.setLoginAgent(request.getHeader(HttpHeaders.USER_AGENT));
                    baseAccountService.addLoginLog(log);
                }
            } catch (Exception e) {
                log.error("添加登录日志失败:{}", e);
            }
            // 用户权限信息
            UserAccount userAccount = getUserAccount(baseAccount.getUserId());
            // 复制账号信息
            BeanUtils.copyProperties(baseAccount, userAccount);
            return userAccount;
        }
        return null;
    }
}
