package com.github.lyd.gateway.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayLogsMapper extends CrudMapper<GatewayAccessLogs> {
}
