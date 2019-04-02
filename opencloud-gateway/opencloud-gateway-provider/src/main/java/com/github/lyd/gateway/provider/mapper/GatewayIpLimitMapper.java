package com.github.lyd.gateway.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimit;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayIpLimitMapper extends CrudMapper<GatewayIpLimit> {
}
