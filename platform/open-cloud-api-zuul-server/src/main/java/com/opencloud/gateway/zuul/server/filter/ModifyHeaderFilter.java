package com.opencloud.gateway.zuul.server.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.opencloud.common.interceptor.FeignRequestInterceptor;

/**
 * 修改Zuul header
 * @author liuyadu
 */
public class ModifyHeaderFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(FeignRequestInterceptor.X_REQUEST_ID, ctx.getRequest().getHeader(FeignRequestInterceptor.X_REQUEST_ID)
        );
        return null;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
