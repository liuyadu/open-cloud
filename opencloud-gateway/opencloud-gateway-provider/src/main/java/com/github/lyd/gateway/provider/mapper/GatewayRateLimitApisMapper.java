package com.github.lyd.gateway.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimitApi;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayRateLimitApisMapper extends CrudMapper<GatewayRateLimitApi> {

    List<GatewayRateLimitApisDto> selectRateLimitApisDto();

}
