package com.opencloud.gateway.zuul.server.filter;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.opencloud.base.client.model.entity.BaseApp;
import com.opencloud.common.constants.CommonConstants;
import com.opencloud.common.exception.OpenSignatureException;
import com.opencloud.common.model.ResultBody;
import com.opencloud.common.utils.SignatureUtils;
import com.opencloud.common.utils.WebUtils;
import com.opencloud.gateway.zuul.server.configuration.ApiProperties;
import com.opencloud.gateway.zuul.server.exception.JsonSignatureDeniedHandler;
import com.opencloud.gateway.zuul.server.service.feign.BaseAppServiceClient;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 数字验签前置过滤器
 *
 * @author: liuyadu
 * @date: 2018/11/28 18:26
 * @description:
 */
public class PreSignatureFilter extends OncePerRequestFilter {
    private JsonSignatureDeniedHandler signatureDeniedHandler;
    private BaseAppServiceClient baseAppServiceClient;
    private ApiProperties apiProperties;
    private static final AntPathMatcher pathMatch = new AntPathMatcher();
    private Set<String> signIgnores = new ConcurrentHashSet<>();

    public PreSignatureFilter(BaseAppServiceClient baseAppServiceClient, ApiProperties apiProperties,JsonSignatureDeniedHandler jsonSignatureDeniedHandler) {
        this.baseAppServiceClient = baseAppServiceClient;
        this.apiProperties = apiProperties;
        this.signatureDeniedHandler =  jsonSignatureDeniedHandler;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        System.out.println(request.getHeaderNames());
        if (apiProperties.getCheckSign() && !notSign(requestPath)) {
            try {
                Map params = WebUtils.getParameterMap(request);
                // 验证请求参数
                SignatureUtils.validateParams(params);
                //开始验证签名
                if (baseAppServiceClient != null) {
                    String appId = params.get(CommonConstants.SIGN_APP_ID_KEY).toString();
                    // 获取客户端信息
                    ResultBody<BaseApp> result = baseAppServiceClient.getApp(appId);
                    BaseApp app = result.getData();
                    if (app == null || app.getAppId()==null) {
                        throw new OpenSignatureException("appId无效");
                    }
                    // 服务器验证签名结果
                    if (!SignatureUtils.validateSign(params, app.getSecretKey())) {
                        throw new OpenSignatureException("签名验证失败!");
                    }
                }
            } catch (Exception ex) {
                signatureDeniedHandler.handle(request, response, ex);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    protected boolean notSign(String requestPath) {
        if(apiProperties.getSignIgnores()==null){
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
