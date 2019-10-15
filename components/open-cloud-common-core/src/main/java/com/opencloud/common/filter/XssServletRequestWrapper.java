package com.opencloud.common.filter;


import com.opencloud.common.utils.StringUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * xss 过滤
 * body 缓存
 *
 * @author liuyadu
 */
public class XssServletRequestWrapper extends HttpServletRequestWrapper {
    private HttpServletRequest request;
    private final byte[] body;


    public XssServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.request = request;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), baos);
        this.body = baos.toByteArray();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
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
