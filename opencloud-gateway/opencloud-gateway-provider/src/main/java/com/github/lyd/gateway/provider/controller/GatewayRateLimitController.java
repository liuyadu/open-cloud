package com.github.lyd.gateway.provider.controller;

import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.model.PageList;
import com.github.lyd.common.model.PageParams;
import com.github.lyd.common.model.ResultBody;
import com.github.lyd.common.utils.StringUtils;
import com.github.lyd.gateway.client.model.entity.GatewayRateLimit;
import com.github.lyd.gateway.provider.service.GatewayRateLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 网关流量控制
 *
 * @author: liuyadu
 * @date: 2019/3/12 15:12
 * @description:
 */
@Api(tags = "网关流量控制")
@RestController
public class GatewayRateLimitController {

    @Autowired
    private GatewayRateLimitService gatewayRateLimitService;
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 获取分页接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping("/gateway/limit/rate")
    public ResultBody<PageList<GatewayRateLimit>> getRateLimitListPage(@RequestParam Map map) {
        return ResultBody.success(gatewayRateLimitService.findListPage(new PageParams(map)));
    }

    /**
     * 查询策略已绑定API列表
     *
     * @param policyId
     * @return
     */
    @ApiOperation(value = "查询策略已绑定API列表", notes = "获取分页接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", paramType = "form"),
    })
    @GetMapping("/gateway/limit/rate/api/list")
    public ResultBody<PageList<GatewayRateLimit>> getRateLimitApiList(
            @RequestParam("policyId") Long policyId
    ) {
        return ResultBody.success(gatewayRateLimitService.findRateLimitApiList(policyId));
    }

    /**
     * 绑定API
     *
     * @param policyId 策略ID
     * @param apiIds   API接口ID.多个以,隔开.选填
     * @return
     */
    @ApiOperation(value = "绑定API", notes = "一个API只能绑定一个策略")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", value = "策略ID", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "apiIds", value = "API接口ID.多个以,隔开.选填", defaultValue = "", required = false, paramType = "form")
    })
    @PostMapping("/gateway/limit/rate/api/add")
    public ResultBody addRateLimitApis(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "apiIds", required = false) String apiIds
    ) {
        gatewayRateLimitService.addRateLimitApis(policyId, StringUtils.isNotBlank(apiIds) ? apiIds.split(",") : new String[]{});
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }

    /**
     * 获取流量控制
     *
     * @param policyId
     * @return
     */
    @ApiOperation(value = "获取流量控制", notes = "获取流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "策略ID", paramType = "path"),
    })
    @GetMapping("/gateway/limit/rate/{policyId}/info")
    public ResultBody<GatewayRateLimit> getRateLimit(@PathVariable("policyId") Long policyId) {
        return ResultBody.success(gatewayRateLimitService.getRateLimitPolicy(policyId));
    }

    /**
     * 添加流量控制
     *
     * @param policyName   策略名称
     * @param serviceId    服务名
     * @param limit        限制数
     * @param intervalUnit 单位时间
     * @param limitType    限流规则类型
     * @return
     */
    @ApiOperation(value = "添加流量控制", notes = "添加流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = true, value = "服务名", paramType = "form"),
            @ApiImplicitParam(name = "limit", required = true, value = "限制数", paramType = "form"),
            @ApiImplicitParam(name = "intervalUnit", required = true, value = "单位时间:second-秒,minute-分钟,hour-小时,day-天", allowableValues = "second,minute,hour,day", paramType = "form"),
            @ApiImplicitParam(name = "limitType", required = true, value = "限流规则类型:url,origin,user", allowableValues = "url,origin,user", paramType = "form")
    })
    @PostMapping("/gateway/limit/rate/add")
    public ResultBody<Long> addRateLimit(
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "serviceId") String serviceId,
            @RequestParam(value = "limit") Long limit,
            @RequestParam(value = "intervalUnit") String intervalUnit,
            @RequestParam(value = "limitType") String limitType
    ) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyName(policyName);
        rateLimit.setServiceId(serviceId);
        rateLimit.setLimit(limit);
        rateLimit.setIntervalUnit(intervalUnit);
        rateLimit.setLimitType(limitType);
        Long policyId = null;
        GatewayRateLimit result = gatewayRateLimitService.addRateLimitPolicy(rateLimit);
        if(result!=null){
            policyId = result.getPolicyId();
        }
        return ResultBody.success(policyId);
    }

    /**
     * 编辑流量控制
     *
     * @param policyId     流量控制ID
     * @param policyName   策略名称
     * @param serviceId    服务名
     * @param limit        限制数
     * @param intervalUnit 单位时间
     * @param limitType    限流规则类型
     * @return
     */
    @ApiOperation(value = "编辑流量控制", notes = "编辑流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "接口Id", paramType = "form"),
            @ApiImplicitParam(name = "policyName", required = true, value = "策略名称", paramType = "form"),
            @ApiImplicitParam(name = "serviceId", required = true, value = "服务名", paramType = "form"),
            @ApiImplicitParam(name = "limit", required = true, value = "限制数", paramType = "form"),
            @ApiImplicitParam(name = "intervalUnit", required = true, value = "单位时间:second-秒,minute-分钟,hour-小时,day-天", allowableValues = "second,minute,hour,day", paramType = "form"),
            @ApiImplicitParam(name = "limitType", required = true, value = "限流规则类型:url,origin,user", allowableValues = "url,origin,user", paramType = "form")
    })
    @PostMapping("/gateway/limit/rate/update")
    public ResultBody updateRateLimit(
            @RequestParam("policyId") Long policyId,
            @RequestParam(value = "policyName") String policyName,
            @RequestParam(value = "serviceId") String serviceId,
            @RequestParam(value = "limit") Long limit,
            @RequestParam(value = "intervalUnit") String intervalUnit,
            @RequestParam(value = "limitType") String limitType
    ) {
        GatewayRateLimit rateLimit = new GatewayRateLimit();
        rateLimit.setPolicyId(policyId);
        rateLimit.setPolicyName(policyName);
        rateLimit.setServiceId(serviceId);
        rateLimit.setLimit(limit);
        rateLimit.setIntervalUnit(intervalUnit);
        rateLimit.setLimitType(limitType);
        gatewayRateLimitService.updateRateLimitPolicy(rateLimit);
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }


    /**
     * 移除流量控制
     *
     * @param policyId
     * @return
     */
    @ApiOperation(value = "移除流量控制", notes = "移除流量控制")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "policyId", required = true, value = "policyId", paramType = "form"),
    })
    @PostMapping("/gateway/limit/rate/remove")
    public ResultBody removeRateLimit(
            @RequestParam("policyId") Long policyId
    ) {
        gatewayRateLimitService.removeRateLimitPolicy(policyId);
        // 刷新网关
        openRestTemplate.refreshGateway();
        return ResultBody.success();
    }
}
