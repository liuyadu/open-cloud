package com.github.lyd.gateway.provider.locator;

import com.github.lyd.base.client.model.AccessAuthority;
import com.github.lyd.gateway.client.model.GatewayIpLimitApisDto;
import com.github.lyd.gateway.client.model.GatewayRateLimitApisDto;
import com.github.lyd.gateway.provider.service.GatewayIpLimitService;
import com.github.lyd.gateway.provider.service.GatewayRateLimitService;
import com.github.lyd.gateway.provider.service.feign.BaseAuthorityRemoteService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.StringToMatchTypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义动态权限加载器
 *
 * @author liuyadu
 */
@Slf4j
public class AccessLocator {
    /**
     * 单位时间
     */
    /**
     * 1分钟
     */
    public static final long SECONDS_IN_MINUTE = 60;
    /**
     * 一小时
     */
    public static final long SECONDS_IN_HOUR = 3600;
    /**
     * 一天
     */
    public static final long SECONDS_IN_DAY = 24 * 3600;

    /**
     * 请求总时长
     */
    public static final int PERIOD_SECOND_TTL = 10;
    public static final int PERIOD_MINUTE_TTL = 2 * 60 + 10;
    public static final int PERIOD_HOUR_TTL = 2 * 3600 + 10;
    public static final int PERIOD_DAY_TTL = 2 * 3600 * 24 + 10;

    /**
     * 权限列表
     */
    private HashMap<String, Collection<ConfigAttribute>> allConfigAttribute;
    /**
     * 权限列表
     */
    private List<AccessAuthority> authorityList;

    /**
     * IP黑名单
     */
    private List<GatewayIpLimitApisDto> ipBlackList;

    /**
     * Ip白名单
     */
    private List<GatewayIpLimitApisDto> ipWhiteList;

    /**
     * 网关API接口流量控制列表
     */
    private List<GatewayRateLimitApisDto> rateLimitApiList;

    private RateLimitProperties rateLimitProperties;
    private ZuulRouteLocator zuulRoutesLocator;
    private BaseAuthorityRemoteService baseAuthorityRemoteService;
    private GatewayIpLimitService gatewayIpLimitService;
    private GatewayRateLimitService gatewayRateLimitService;
    private StringToMatchTypeConverter converter;


    public AccessLocator(ZuulRouteLocator zuulRoutesLocator, RateLimitProperties rateLimitProperties, BaseAuthorityRemoteService baseAuthorityRemoteService, GatewayIpLimitService gatewayIpLimitService, GatewayRateLimitService gatewayRateLimitService) {
        this.zuulRoutesLocator = zuulRoutesLocator;
        this.rateLimitProperties = rateLimitProperties;
        this.baseAuthorityRemoteService = baseAuthorityRemoteService;
        this.gatewayIpLimitService = gatewayIpLimitService;
        this.gatewayRateLimitService = gatewayRateLimitService;
        this.converter = new StringToMatchTypeConverter();
    }

    /**
     * 刷新访问配置
     */
    public void doRefresh() {
        loadAuthority();
        loadIpBlackList();
        loadIpWhiteList();
        loadRateLimit();
    }

    /**
     * 获取路由后的地址
     *
     * @return
     */
    protected String getZuulPath(String serviceId, String path) {
        if (path == null) {
            path = "";
        }
        List<Route> rotes = zuulRoutesLocator.getRoutes();
        if (rotes != null && !rotes.isEmpty()) {
            for (Route route : rotes) {
                // 服务ID相同
                if (route.getId().equals(serviceId)) {
                    return route.getPrefix().concat(path);
                }
            }
        }
        return path;
    }

    /**
     * 加载授权列表
     */
    public void loadAuthority() {
        allConfigAttribute = Maps.newHashMap();
        authorityList = Lists.newArrayList();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        try {
            // 查询所有接口
            authorityList = baseAuthorityRemoteService.getAccessAuthorityList().getData();
            if (authorityList != null) {
                for (AccessAuthority item : authorityList) {
                    String path = item.getPath();
                    if (path == null) {
                        continue;
                    }
                    String fullPath = getZuulPath(item.getServiceId(), path);
                    item.setPath(fullPath);
                    array = allConfigAttribute.get(fullPath);
                    if (array == null) {
                        array = new ArrayList<>();
                    }
                    if (!array.contains(item.getAuthority())) {
                        cfg = new SecurityConfig(item.getAuthority());
                        array.add(cfg);
                    }
                    allConfigAttribute.put(fullPath, array);
                }
            }
        } catch (Exception e) {
            log.error("加载动态权限错误:{}", e.getMessage());
        }
        log.info("=============加载动态权限:{}==============",authorityList.size());
    }

    /**
     * 加载IP黑名单
     */
    public void loadIpBlackList() {
        ipBlackList = Lists.newArrayList();
        try {
            ipBlackList = gatewayIpLimitService.findBlackList();
            if (ipBlackList != null) {
                for (GatewayIpLimitApisDto item : ipBlackList) {
                    item.setPath(getZuulPath(item.getServiceId(), item.getPath()));
                }
            }
        } catch (Exception e) {
            log.error("加载IP黑名单错误:{}", e.getMessage());
        }
        log.info("=============加载IP黑名单:{}==============",ipBlackList.size());
    }

    /**
     * 加载IP白名单
     */
    public void loadIpWhiteList() {
        ipWhiteList = Lists.newArrayList();
        try {
            ipWhiteList = gatewayIpLimitService.findWhiteList();
            if (ipWhiteList != null) {
                for (GatewayIpLimitApisDto item : ipWhiteList) {
                    item.setPath(getZuulPath(item.getServiceId(), item.getPath()));
                }
            }
        } catch (Exception e) {
            log.error("加载IP白名单错误:{}", e.getMessage());
        }
        log.info("=============加载IP白名单:{}==============",ipWhiteList.size());
    }

    /**
     * 加载限流配置
     * 1. 认证用户（Authenticated User）
     * 使用已认证的用户名（username）或'anonymous'
     * 2. 原始请求（Request Origin）
     * 使用系统用户的原始请求
     * 3. URL
     * 使用上游请求的地址
     * 4. 针对每个服务的全局配置
     * 该方式不会验证Request Origin，Authenticated User或URL
     * 使用该方式只需不设置‘type’参数即可
     *
     * @return
     */
    public void loadRateLimit() {
        LinkedHashMap<String, List<RateLimitProperties.Policy>> policysMap = Maps.newLinkedHashMap();
        //从application.properties中加载限流信息
        policysMap.putAll(rateLimitProperties.getPolicyList());
        //从db中加载限流信息
        policysMap.putAll(loadRateLimitPolicy());
        rateLimitProperties.setPolicyList(policysMap);
        log.info("=============加载动态限流:{}==============",rateLimitProperties.getPolicyList().size());
    }


    protected Map<String, List<RateLimitProperties.Policy>> loadRateLimitPolicy() {
        Map<String, List<RateLimitProperties.Policy>> policyMap = Maps.newLinkedHashMap();
        try {
            rateLimitApiList = gatewayRateLimitService.findRateLimitApiList();
            if (rateLimitApiList != null && rateLimitApiList.size() > 0) {
                for (GatewayRateLimitApisDto item : rateLimitApiList) {
                    List<RateLimitProperties.Policy> policyList = policyMap.get(item.getServiceId());
                    if (policyList == null) {
                        policyList = Lists.newArrayList();
                    }
                    RateLimitProperties.Policy policy = new RateLimitProperties.Policy();
                    long[] arry = getIntervalAndQuota(item.getIntervalUnit());
                    Long refreshInterval = arry[0];
                    Long quota = arry[1];
                    String url = getZuulPath(item.getServiceId(), item.getPath());
                    item.setPath(url);
                    policy.setLimit(item.getLimit());
                    policy.setRefreshInterval(refreshInterval);
                    policy.setQuota(quota);
                    String type = "url=".concat(item.getPath());
                    RateLimitProperties.Policy.MatchType matchType = converter.convert(type);
                    policy.getType().add(matchType);
                    policyList.add(policy);
                    policyMap.put(item.getServiceId(), policyList);
                }
            }
        } catch (Exception e) {
            log.error("加载动态限流错误:{}", e.getMessage());
        }
        return policyMap;
    }

    /**
     * 获取单位时间内刷新时长和请求总时长
     *
     * @param timeUnit
     * @return
     */
    private long[] getIntervalAndQuota(String timeUnit) {
        if (timeUnit.equals(TimeUnit.SECONDS.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_SECOND_TTL};
        } else if (timeUnit.equals(TimeUnit.MINUTES.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_MINUTE_TTL};
        } else if (timeUnit.equals(TimeUnit.HOURS.name())) {
            return new long[]{SECONDS_IN_HOUR, PERIOD_HOUR_TTL};
        } else if (timeUnit.equals(TimeUnit.DAYS.name())) {
            return new long[]{SECONDS_IN_DAY, PERIOD_DAY_TTL};
        } else {
            throw new java.lang.IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }


    public List<AccessAuthority> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<AccessAuthority> authorityList) {
        this.authorityList = authorityList;
    }

    public List<GatewayIpLimitApisDto> getIpBlackList() {
        return ipBlackList;
    }

    public void setIpBlackList(List<GatewayIpLimitApisDto> ipBlackList) {
        this.ipBlackList = ipBlackList;
    }

    public List<GatewayIpLimitApisDto> getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(List<GatewayIpLimitApisDto> ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public List<GatewayRateLimitApisDto> getRateLimitApiList() {
        return rateLimitApiList;
    }

    public void setRateLimitApiList(List<GatewayRateLimitApisDto> rateLimitApiList) {
        this.rateLimitApiList = rateLimitApiList;
    }

    public HashMap<String, Collection<ConfigAttribute>> getAllConfigAttribute() {
        return allConfigAttribute;
    }

    public void setAllConfigAttribute(HashMap<String, Collection<ConfigAttribute>> allConfigAttribute) {
        this.allConfigAttribute = allConfigAttribute;
    }
}
