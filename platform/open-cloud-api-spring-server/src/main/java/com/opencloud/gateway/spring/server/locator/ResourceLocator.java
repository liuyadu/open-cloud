package com.opencloud.gateway.spring.server.locator;

import com.google.common.collect.Lists;
import com.opencloud.base.client.model.AuthorityResource;
import com.opencloud.base.client.model.IpLimitApi;
import com.opencloud.common.event.RemoteRefreshRouteEvent;
import com.opencloud.gateway.spring.server.service.feign.BaseAuthorityServiceClient;
import com.opencloud.gateway.spring.server.service.feign.GatewayServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import reactor.cache.CacheFlux;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
     * 权限资源
     */
    private Flux<AuthorityResource> authorityResources;

    /**
     * ip黑名单
     */
    private Flux<IpLimitApi> ipBlacks;

    /**
     * ip白名单
     */
    private Flux<IpLimitApi> ipWhites;

    /**
     * 权限列表
     */
    private Map<String, Collection<ConfigAttribute>> configAttributes = new ConcurrentHashMap<>();
    /**
     * 缓存
     */
    private Map<String, Object> cache = new ConcurrentHashMap<>();


    private BaseAuthorityServiceClient baseAuthorityServiceClient;
    private GatewayServiceClient gatewayServiceClient;

    private RouteDefinitionLocator routeDefinitionLocator;

    public ResourceLocator() {
        authorityResources = CacheFlux.lookup(cache, "authorityResources", AuthorityResource.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipBlacks = CacheFlux.lookup(cache, "ipBlacks", IpLimitApi.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
        ipWhites = CacheFlux.lookup(cache, "ipWhites", IpLimitApi.class).onCacheMissResume(Flux.fromIterable(new ArrayList<>()));
    }


    public ResourceLocator(RouteDefinitionLocator routeDefinitionLocator, BaseAuthorityServiceClient baseAuthorityServiceClient, GatewayServiceClient gatewayServiceClient) {
        this();
        this.baseAuthorityServiceClient = baseAuthorityServiceClient;
        this.gatewayServiceClient = gatewayServiceClient;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * 清空缓存并刷新
     */
    public void refresh() {
        this.configAttributes.clear();
        this.cache.clear();
        this.authorityResources = loadAuthorityResources();
        this.ipBlacks = loadIpBlackList();
        this.ipWhites = loadIpWhiteList();
    }

    @Override
    public void onApplicationEvent(RemoteRefreshRouteEvent event) {
        refresh();
    }

    /**
     * 获取路由后的地址
     *
     * @return
     */
    protected String getFullPath(String serviceId, String path) {
        final String[] fullPath = {path.startsWith("/") ? path : "/" + path};
        routeDefinitionLocator.getRouteDefinitions()
                .filter(routeDefinition -> routeDefinition.getId().equals(serviceId))
                .subscribe(routeDefinition -> {
                            routeDefinition.getPredicates().stream()
                                    .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                                    .filter(predicateDefinition -> !predicateDefinition.getArgs().containsKey("_rateLimit"))
                                    .forEach(predicateDefinition -> {
                                        fullPath[0] = predicateDefinition.getArgs().get("pattern").replace("/**", path.startsWith("/") ? path : "/" + path);
                                    });
                        }
                );
        return fullPath[0];
    }

    /**
     * 加载授权列表
     */
    public Flux<AuthorityResource> loadAuthorityResources() {
        List<AuthorityResource> resources = Lists.newArrayList();
        Collection<ConfigAttribute> array;
        ConfigAttribute cfg;
        try {
            // 查询所有接口
            resources = baseAuthorityServiceClient.findAuthorityResource().getData();
            if (resources != null) {
                for (AuthorityResource item : resources) {
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
                log.info("=============加载动态权限:{}==============", resources.size());
            }
        } catch (Exception e) {
            log.error("加载动态权限错误:{}", e);
        }
        return Flux.fromIterable(resources);
    }

    /**
     * 加载IP黑名单
     */
    public Flux<IpLimitApi> loadIpBlackList() {
        List<IpLimitApi> list = Lists.newArrayList();
        try {
            list = gatewayServiceClient.getApiBlackList().getData();
            if (list != null) {
                for (IpLimitApi item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP黑名单:{}==============", list.size());
            }
        } catch (Exception e) {
            log.error("加载IP黑名单错误:{}", e);
        }
        return Flux.fromIterable(list);
    }

    /**
     * 加载IP白名单
     */
    public Flux<IpLimitApi> loadIpWhiteList() {
        List<IpLimitApi> list = Lists.newArrayList();
        try {
            list = gatewayServiceClient.getApiWhiteList().getData();
            if (list != null) {
                for (IpLimitApi item : list) {
                    item.setPath(getFullPath(item.getServiceId(), item.getPath()));
                }
                log.info("=============加载IP白名单:{}==============", list.size());
            }
        } catch (Exception e) {
            log.error("加载IP白名单错误:{}", e);
        }
        return Flux.fromIterable(list);
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
            throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
        }
    }

    public Flux<AuthorityResource> getAuthorityResources() {
        return authorityResources;
    }

    public void setAuthorityResources(Flux<AuthorityResource> authorityResources) {
        this.authorityResources = authorityResources;
    }

    public Flux<IpLimitApi> getIpBlacks() {
        return ipBlacks;
    }

    public void setIpBlacks(Flux<IpLimitApi> ipBlacks) {
        this.ipBlacks = ipBlacks;
    }

    public Flux<IpLimitApi> getIpWhites() {
        return ipWhites;
    }

    public void setIpWhites(Flux<IpLimitApi> ipWhites) {
        this.ipWhites = ipWhites;
    }

    public Map<String, Collection<ConfigAttribute>> getConfigAttributes() {
        return configAttributes;
    }

    public void setConfigAttributes(Map<String, Collection<ConfigAttribute>> configAttributes) {
        this.configAttributes = configAttributes;
    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public void setCache(Map<String, Object> cache) {
        this.cache = cache;
    }

    public BaseAuthorityServiceClient getBaseAuthorityServiceClient() {
        return baseAuthorityServiceClient;
    }

    public void setBaseAuthorityServiceClient(BaseAuthorityServiceClient baseAuthorityServiceClient) {
        this.baseAuthorityServiceClient = baseAuthorityServiceClient;
    }

    public GatewayServiceClient getGatewayServiceClient() {
        return gatewayServiceClient;
    }

    public void setGatewayServiceClient(GatewayServiceClient gatewayServiceClient) {
        this.gatewayServiceClient = gatewayServiceClient;
    }

    public RouteDefinitionLocator getRouteDefinitionLocator() {
        return routeDefinitionLocator;
    }

    public void setRouteDefinitionLocator(RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionLocator = routeDefinitionLocator;
    }
}
