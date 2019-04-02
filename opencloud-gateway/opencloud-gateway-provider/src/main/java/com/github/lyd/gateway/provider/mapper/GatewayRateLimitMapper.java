package com.github.lyd.gateway.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimit;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRateLimitMapper extends CrudMapper<GatewayRateLimit> {
}
