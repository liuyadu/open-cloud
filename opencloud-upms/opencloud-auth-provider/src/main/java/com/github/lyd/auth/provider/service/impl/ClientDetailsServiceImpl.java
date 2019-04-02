package com.github.lyd.auth.provider.service.impl;

import com.github.lyd.auth.provider.service.feign.BaseAppRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private BaseAppRemoteService baseAppRemoteService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails details = baseAppRemoteService.getAppClientInfo(clientId).getData();
        if (details != null) {
            Map info = details.getAdditionalInformation();
            if (info != null && (info.get("status") == null || !info.get("status").equals(1))) {
                // 客户端未启用,返回权限为空,禁止调用接口
                details.setAuthorities(Collections.emptyList());
            }
            return details;
        }
        return null;
    }
}
