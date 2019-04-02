package com.github.lyd.base.client.api;

import com.github.lyd.base.client.model.BaseUserAccountDto;
import com.github.lyd.common.model.ResultBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuyadu
 */
public interface BaseUserAccountRemoteApi {
    /**
     * 登录
     *
     * @param username 登录名
     * @return
     */
    @PostMapping("/account/localLogin")
    ResultBody<BaseUserAccountDto> localLogin(@RequestParam(value = "username") String username);


    /**
     * 注册第三方登录账号
     *
     * @param account
     * @param password
     * @param accountType
     * @return
     */
    @PostMapping("/account/register/thirdParty")
    ResultBody registerThirdPartyAccount(
            @RequestParam(value = "account") String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "accountType") String accountType
    );

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @PostMapping("/account/reset/password")
    ResultBody resetPassword(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword") String newPassword
    );
}
