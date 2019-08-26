package com.opencloud.gateway.zuul.server.locator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.properties.RateLimitProperties;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.StringToMatchTypeConverter;
import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.base.client.model.IpLimitApi;
import com.opencloud.base.client.model.RateLimitApi;
import com.opencloud.common.event.RemoteRefreshRouteEvent;
import com.opencloud.gateway.zuul.server.service.feign.BaseAuthorityServiceClient;
import com.opencloud.gateway.zuul.server.service.feign.GatewayServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 资源加载器
 *
 * @author liuyadu
 */
@Slf4j
public class ResourceLocator implements ApplicationListener<RemoteRefreshRouteEvent> {
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
    private Map<String, Collection<ConfigAttribute>> configAttributes = new ConcurrentHashMap();
    /**
     * 权限列表
     */
    private List<AuthorityResource> authorityResources;

    /**
     * IP黑名单
     */
    private List<IpLimitApi> ipBlacks;

    /**
     * Ip白名单
     */
    private List<IpLimitApi> ipWhites;

    /**
     * 流量限制
     */
    private List<RateLimitApi> rateLimitApis;

    private RateLimitProperties rateLimitProperties;
    private JdbcRouteLocator zuulRoutesLocator;
    private BaseAuthorityServiceClient baseAuthorityServiceClient;
    private GatewayServiceClient gatewayServiceClient;
    private StringToMatchTypeConverter converter;


    public ResourceLocator(JdbcRouteLocator zuulRoutesLocator, RateLimitProperties rateLimitProperties, BaseAuthorityServiceClient baseAuthorityServiceClient, GatewayServiceClient gatewayServiceClient) {
        this.ipBlacks = new CopyOnWriteArrayList();
        this.ipWhites = new CopyOnWriteArrayList();
        this.authorityResources = new CopyOnWriteArrayList();
        this.rateLimitApis = new CopyOnWriteArrayList();
        this.zuulRoutesLocator = zuulRoutesLocator;
        this.rateLimitProperties = rateLimitProperties;
        this.baseAuthorityServiceClient = baseAuthorityServiceClient;
        this.gatewayServiceClient = gatewayServiceClient;
        this.converter = new StringToMatchTypeConverter();
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        loadAuthority();
        loadIpBlacks();
        loadIpWhites();
        loadRateLimit();
    }

    /**
     * 获取路由后的完整地址
     *
     * @return
     */
    protected String getFullPath(String routeId, String path) {
        List<Route> routes = zuulRoutesLocator.getRoutes();
        if (routes != null && !routes.isEmpty()) {
            for (Route route : routes) {
                // 服务ID相同
                if (route.getId().equals(routeId)) {
                    return route.getPrefix().concat(path.startsWith("/") ? path : "/" + path);
                }
            }
        }
        return path;
    }

    /**
     * 加载授权列表
     */
    public void loadAuthority() {
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        HashMap<String, Collection<ConfigAttribute>> configAttributes = Maps.newHashMap();
        try {
            // 查询所有接口
            List<AuthorityResource> list = baseAuthorityServiceClient.findAuthorityResource().getData();
            if (list != null) {
                for (AuthorityResource item : list) {
                    String path = item.getPath();
                    if (path == null) {
                        continue;
                    }
                    String fullPath = getFullPath(item.getServiceId(), path);
                    item.setPath(fullPath);
                    array = configAttributes.get(fullPath);
                    if (array == null) {
                        array = new ArrayList<>();
                    }
                    if (!array.contains(item.getAuthority())) {
                        cfg = new SecurityConfig(item.getAuthority());
                        array.add(cfg);
                    }
                    configAttributes.put(fullPath, array);
                }
                this.configAttributes.clear();
                this.authorityResources.clear();
                this.configAttributes = configAttributes;
                this.authorityResources = list;
            }
            log.info("=============加载动态权限:{}==============", this.authorityResources.size());
        } catch (Exception e) {
            log.error("加载动态权限错误:{}", e);
        }
    }

    /**
     * 加载IP黑名单
     */
    public void loadIpBlacks() {
        try {
            List<IpLimitApi> list = gatewayServiceClient.getApiBlackList().getData();
            if (list != null) {
                for (IpLimitApi item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                this.ipBlacks.clear();
                this.ipBlacks.addAll(list);
            }
            log.info("=============加载IP黑名单:{}==============", this.ipBlacks.size());
        } catch (Exception e) {
            log.error("加载IP黑名单错误:{}", e);
        }
    }

    /**
     * 加载IP白名单
     */
    public void loadIpWhites() {
        try {
            List<IpLimitApi> list = gatewayServiceClient.getApiWhiteList().getData();
            if (list != null) {
                for (IpLimitApi item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                this.ipWhites.clear();
                this.ipWhites.addAll(list);
            }
            log.info("=============加载IP白名单:{}==============", ipWhites.size());
        } catch (Exception e) {
            log.error("加载IP白名单错误:{}", e);
        }
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
        //从db中加载限流信息
        policysMap.putAll(loadRateLimitPolicy());
        rateLimitProperties.setPolicyList(policysMap);
    }


    /**
     * 加载并转换限流策略
     *
     * @return
     */
    protected Map<String, List<RateLimitProperties.Policy>> loadRateLimitPolicy() {
        Map<String, List<RateLimitProperties.Policy>> policyMap = Maps.newLinkedHashMap();
        try {
            List<RateLimitApi> list = gatewayServiceClient.getApiRateLimitList().getData();
            if (list != null) {
                for (RateLimitApi item : list) {
                    List<RateLimitProperties.Policy> policyList = policyMap.get(item.getServiceId());
                    if (policyList == null) {
                        policyList = Lists.newArrayList();
                    }
                    RateLimitProperties.Policy policy = new RateLimitProperties.Policy();
                    long[] arry = getIntervalAndQuota(item.getIntervalUnit());
                    Long refreshInterval = arry[0];
                    Long quota = arry[1];
                    policy.setLimit(item.getLimitQuota());
                    policy.setRefreshInterval(refreshInterval);
                    policy.setQuota(quota);
                    String type = "url=".concat(item.getPath());
                    RateLimitProperties.Policy.MatchType matchType = converter.convert(type);
                    policy.getType().add(matchType);
                    policyList.add(policy);
                    policyMap.put(item.getServiceId(), policyList);
                }
                this.rateLimitApis.clear();
                this.rateLimitApis.addAll(rateLimitApis);
            }
            log.info("=============加载动态限流:{}==============", rateLimitProperties.getPolicyList().size());
        } catch (Exception e) {
            log.error("加载动态限流错误:{}", e);
        }
        return policyMap;
    }

    /**
     * 获取单位时间内刷新时长和请求总时长
     *
     * @param timeUnit
     * @return
     */
    public static long[] getIntervalAndQuota(String timeUnit) {
        if (timeUnit.equalsIgnoreCase(TimeUnit.SECONDS.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_SECOND_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.MINUTES.name())) {
            return new long[]{SECONDS_IN_MINUTE, PERIOD_MINUTE_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.HOURS.name())) {
            return new long[]{SECONDS_IN_HOUR, PERIOD_HOUR_TTL};
        } else if (timeUnit.equalsIgnoreCase(TimeUnit.DAYS.name())) {
            return new long[]{SECONDS_IN_DAY, PERIOD_DAY_TTL};
        } else {
            throw new java.lang.IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

    public List<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(List<AuthorityResource> authorityResources) {
        this.authorityResources = authorityResources;
    }

    public List<IpLimitApi> getIpBlacks() {
        return ipBlacks;
    }

    public void setIpBlacks(List<IpLimitApi> ipBlacks) {
        this.ipBlacks = ipBlacks;
    }

    public List<IpLimitApi> getIpWhites() {
        return ipWhites;
    }

    public void setIpWhites(List<IpLimitApi> ipWhites) {
        this.ipWhites = ipWhites;
    }

    public List<RateLimitApi> getRateLimitApis() {
        return rateLimitApis;
    }

    public void setRateLimitApis(List<RateLimitApi> rateLimitApis) {
        this.rateLimitApis = rateLimitApis;
    }

    public Map<String, Collection<ConfigAttribute>> getConfigAttributes() {
        return configAttributes;
    }

    public void setConfigAttributes(Map<String, Collection<ConfigAttribute>> configAttributes) {
        this.configAttributes = configAttributes;
    }

    /**
     * 远程刷新事件
     *
     * @param gatewayRemoteRefreshRouteEvent
     */
    @Override
    public void onApplicationEvent(RemoteRefreshRouteEvent gatewayRemoteRefreshRouteEvent) {
        refresh();
    }
}
