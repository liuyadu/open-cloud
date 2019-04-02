package com.github.lyd.gateway.provider.service.feign;

import com.github.lyd.base.client.api.BaseAuthorityRemoteApi;
import com.github.lyd.base.client.constants.BaseConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: liuyadu
 * @date: 2018/10/24 16:49
 * @description:
 */
@Component
@FeignClient(value = BaseConstants.BASE_SERVICE)
public interface BaseAuthorityRemoteService extends BaseAuthorityRemoteApi {


}
