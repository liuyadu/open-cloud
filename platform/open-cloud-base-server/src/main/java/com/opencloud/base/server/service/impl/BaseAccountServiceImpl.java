package com.opencloud.base.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.base.client.constants.BaseConstants;
import com.opencloud.base.client.model.entity.BaseAccount;
import com.opencloud.base.client.model.entity.BaseAccountLogs;
import com.opencloud.base.server.mapper.BaseAccountLogsMapper;
import com.opencloud.base.server.mapper.BaseAccountMapper;
import com.opencloud.base.server.service.BaseAccountService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 通用账号
 *
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BaseAccountServiceImpl extends BaseServiceImpl<BaseAccountMapper, BaseAccount> implements BaseAccountService {

    @Autowired
    private BaseAccountMapper baseAccountMapper;
    @Autowired
    private BaseAccountLogsMapper baseAccountLogsMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 根据主键获取账号信息
     *
     * @param accountId
     * @return
     */
    @Override
    public BaseAccount getAccountById(Long accountId) {
        return baseAccountMapper.selectById(accountId);
    }

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public BaseAccount getAccount(String account, String accountType, String domain) {
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccount::getAccount, account)
                .eq(BaseAccount::getAccountType, accountType)
                .eq(BaseAccount::getDomain, domain);
        return baseAccountMapper.selectOne(queryWrapper);

    }

    /**
     * 注册账号
     *
     * @param userId
     * @param account
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    @Override
    public BaseAccount register(Long userId, String account, String password, String accountType, Integer status, String domain, String registerIp) {
        if (isExist(account, accountType, domain)) {
            // 账号已被注册
            throw new RuntimeException(String.format("account=[%s],domain=[%s]", account, domain));
        }
        //加密
        String encodePassword = passwordEncoder.encode(password);
        BaseAccount baseAccount = new BaseAccount(userId, account, encodePassword, accountType, domain, registerIp);
        baseAccount.setCreateTime(new Date());
        baseAccount.setUpdateTime(baseAccount.getCreateTime());
        baseAccount.setStatus(status);
        baseAccountMapper.insert(baseAccount);
        return baseAccount;
    }


    /**
     * 检测账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    @Override
    public Boolean isExist(String account, String accountType, String domain) {
        QueryWrapper<BaseAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccount::getAccount, account)
                .eq(BaseAccount::getAccountType, accountType)
                .eq(BaseAccount::getDomain, domain);
        int count = baseAccountMapper.selectCount(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 删除账号
     *
     * @param accountId
     * @return
     */
    @Override
    public int removeAccount(Long accountId) {
        return baseAccountMapper.deleteById(accountId);
    }


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     */
    @Override
    public int updateStatus(Long accountId, Integer status) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setAccountId(accountId);
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        return baseAccountMapper.updateById(baseAccount);
    }

    /**
     * 根据用户更新账户状态
     *
     * @param userId
     * @param domain
     * @param status
     */
    @Override
    public int updateStatusByUserId(Long userId, String domain, Integer status) {
        if (status == null) {
            return 0;
        }
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setStatus(status);
        QueryWrapper<BaseAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseAccount::getDomain, domain)
                .eq(BaseAccount::getUserId, userId);
        return baseAccountMapper.update(baseAccount, wrapper);
    }

    /**
     * 重置用户密码
     *
     * @param userId
     * @param domain
     * @param password
     */
    @Override
    public int updatePasswordByUserId(Long userId, String domain, String password) {
        BaseAccount baseAccount = new BaseAccount();
        baseAccount.setUpdateTime(new Date());
        baseAccount.setPassword(passwordEncoder.encode(password));
        QueryWrapper<BaseAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .in(BaseAccount::getAccountType, BaseConstants.ACCOUNT_TYPE_USERNAME, BaseConstants.ACCOUNT_TYPE_EMAIL, BaseConstants.ACCOUNT_TYPE_MOBILE)
                .eq(BaseAccount::getUserId, userId)
                .eq(BaseAccount::getDomain, domain);
        return baseAccountMapper.update(baseAccount, wrapper);
    }

    /**
     * 根据用户ID删除账号
     *
     * @param userId
     * @param domain
     * @return
     */
    @Override
    public int removeAccountByUserId(Long userId, String domain) {
        QueryWrapper<BaseAccount> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(BaseAccount::getUserId, userId)
                .eq(BaseAccount::getDomain, domain);
        return baseAccountMapper.delete(wrapper);
    }


    /**
     * 添加登录日志
     *
     * @param log
     */
    @Override
    public void addLoginLog(BaseAccountLogs log) {
        QueryWrapper<BaseAccountLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(BaseAccountLogs::getAccountId, log.getAccountId())
                .eq(BaseAccountLogs::getUserId, log.getUserId());
        int count = baseAccountLogsMapper.selectCount(queryWrapper);
        log.setLoginTime(new Date());
        log.setLoginNums(count + 1);
        baseAccountLogsMapper.insert(log);
    }
}
