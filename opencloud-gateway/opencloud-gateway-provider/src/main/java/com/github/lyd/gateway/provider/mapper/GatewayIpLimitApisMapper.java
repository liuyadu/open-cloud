package com.github.lyd.gateway.provider.mapper;

import com.github.lyd.common.mapper.CrudMapper;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;
import com.github.lyd.gateway.client.model.entity.GatewayIpLimitApi;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface GatewayIpLimitApisMapper extends CrudMapper<GatewayIpLimitApi> {

    List<GatewayIpLimitApisDto> selectIpLimitApisDto(@Param("policyType") int policyType);
}
