package com.opencloud.gateway.spring.server.filter;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.google.common.collect.Maps;
import com.opencloud.base.client.model.entity.BaseApp;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenSignatureException;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.SignatureUtils;
import com.opencloud.gateway.spring.server.configuration.ApiProperties;
import com.opencloud.gateway.spring.server.exception.JsonSignatureDeniedHandler;
import com.opencloud.gateway.spring.server.filter.context.GatewayContext;
import com.opencloud.gateway.spring.server.service.feign.BaseAppServiceClient;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数字验签前置过滤器
 *
 * @author: liuyadu
 * @date: 2018/11/28 18:26
 * @description:
 */
public class PreSignatureFilter implements WebFilter {
    private JsonSignatureDeniedHandler signatureDeniedHandler;
    private BaseAppServiceClient baseAppServiceClient;
    private ApiProperties apiProperties;
    private static final AntPathMatcher pathMatch = new AntPathMatcher();
    private Set<String> signIgnores = new ConcurrentHashSet<>();

    public PreSignatureFilter(BaseAppServiceClient baseAppServiceClient, ApiProperties apiProperties, JsonSignatureDeniedHandler signatureDeniedHandler) {
        this.apiProperties = apiProperties;
        this.baseAppServiceClient = baseAppServiceClient;
        this.signatureDeniedHandler = signatureDeniedHandler;
        // 默认忽略签名
        signIgnores.add("/");
        signIgnores.add("/error");
        signIgnores.add("/favicon.ico");
        if (apiProperties != null) {
            if (apiProperties.getSignIgnores() != null) {
                signIgnores.addAll(apiProperties.getSignIgnores());
            }
            if (apiProperties.getApiDebug()) {
                signIgnores.add("/**/v2/api-docs/**");
                signIgnores.add("/**/swagger-resources/**");
                signIgnores.add("/webjars/**");
                signIgnores.add("/doc.html");
                signIgnores.add("/swagger-ui.html");
            }
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath();
        if (apiProperties.getCheckSign() && !notSign(requestPath)) {
            try {
                Map params = Maps.newHashMap();
                GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
                // 排除文件上传
                if (gatewayContext != null) {
                    params = gatewayContext.getAllRequestData().toSingleValueMap();
                }
                // 验证请求参数
                SignatureUtils.validateParams(params);
                //开始验证签名
                if (baseAppServiceClient != null) {
                    String appId = params.get(CommonConstants.SIGN_APP_ID_KEY).toString();
                    // 获取客户端信息
                    ResultBody<BaseApp> result = baseAppServiceClient.getApp(appId);
                    BaseApp app = result.getData();
                    if (app == null || app.getAppId() == null) {
                        return signatureDeniedHandler.handle(exchange, new OpenSignatureException("appId无效"));
                    }
                    // 服务器验证签名结果
                    if (!SignatureUtils.validateSign(params, app.getSecretKey())) {
                        return signatureDeniedHandler.handle(exchange, new OpenSignatureException("签名验证失败!"));
                    }
                }
            } catch (Exception ex) {
                return signatureDeniedHandler.handle(exchange, new OpenSignatureException(ex.getMessage()));
            }
        }
        return chain.filter(exchange);
    }


    protected static List<String> getIgnoreMatchers(String... antPatterns) {
        List<String> matchers = new CopyOnWriteArrayList();
        for (String path : antPatterns) {
            matchers.add(path);
        }
        return matchers;
    }

    protected boolean notSign(String requestPath) {
        if (apiProperties.getSignIgnores() == null) {
            return false;
        }
        for (String path : signIgnores) {
            if (pathMatch.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }
}
