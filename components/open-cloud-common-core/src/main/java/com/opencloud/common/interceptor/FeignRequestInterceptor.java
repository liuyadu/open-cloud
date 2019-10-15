package com.opencloud.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 微服务之间feign调用请求头丢失的问题
 * 加入微服务之间传递的唯一标识,便于追踪
 * @author liuyadu
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    /**
     * 微服务之间传递的唯一标识
     */
    public static final String X_REQUEST_ID = "X-Request-Id";

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest httpServletRequest =   getHttpServletRequest();
        if(httpServletRequest!=null){
            Map<String, String> headers = getHeaders(httpServletRequest);
            // 传递所有请求头,防止部分丢失
            Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                template.header(entry.getKey(), entry.getValue());
            }
            // 微服务之间传递的唯一标识
            if (httpServletRequest.getHeader(X_REQUEST_ID)==null) {
                String sid = String.valueOf(UUID.randomUUID());
                template.header(X_REQUEST_ID, sid);
            }
            log.debug("FeignRequestInterceptor:{}", template.toString());
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        if(enumeration!=null){
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                String value = request.getHeader(key);
                map.put(key, value);
            }
        }
        return map;
    }

}
