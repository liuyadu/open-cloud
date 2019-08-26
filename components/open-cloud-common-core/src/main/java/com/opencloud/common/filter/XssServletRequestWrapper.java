package com.opencloud.common.filter;


import com.opencloud.common.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * xss 过滤
 * body 缓存
 *
 * @author liuyadu
 */
public class XssServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;

    public XssServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        name = StringUtils.stripXss(name);
        String value = request.getParameter(name);
        if (!StringUtils.isEmpty(value)) {
            value = StringUtils.stripXss(value).trim();
        }
        return value;
    }

    @Override
    public String getHeader(String name) {
        name = StringUtils.stripXss(name);
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            value = StringUtils.stripXss(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        name = StringUtils.stripXss(name);
        String[] parameterValues = super.getParameterValues(name);
        if (parameterValues == null) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            String value = parameterValues[i];
            parameterValues[i] = StringUtils.stripXss(value).trim();
        }
        return parameterValues;
    }
}
