package com.opencloud.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * Http与Servlet工具类.
 *
 * @author liuyadu
 */
public class WebUtils {

    private final static String staticSuffix = ".css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.htm,.html,.crx,.xpi,.exe,.ipa,.apk,.woff2,.ico,.swf,.ttf,.otf,.svg,.woff";
    /**
     * 静态文件后缀
     */
    private final static String[] staticFiles = StringUtils.split(staticSuffix, ",");

    /**
     * 动态映射URL后缀
     */
    private final static String urlSuffix = ".html";

    public static String[] getStaticFiles() {
        return staticFiles;
    }

    /**
     * 设置 Cookie（生成时间为1天）
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, 60 * 60 * 24);
    }

    /**
     * 设置 Cookie
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
        setCookie(response, name, value, path, 60 * 60 * 24);
    }

    /**
     * 设置 Cookie
     *
     * @param name   名称
     * @param value  值
     * @param maxAge 生存时间（单位秒）
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        setCookie(response, name, value, "/", maxAge);
    }

    /**
     * 设置 Cookie
     *
     * @param name   名称
     * @param value  值
     * @param maxAge 生存时间（单位秒）
     * @param path   路径
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        try {
            cookie.setValue(URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addCookie(cookie);
    }

    /**
     * 移除cookie
     *
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setValue(null);
        response.addCookie(cookie);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param name 名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, String name) {
        return getCookie(request, null, name, false);
    }

    /**
     * 获得指定Cookie的值，并删除。
     *
     * @param name 名称
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        return getCookie(request, response, name, true);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名字
     * @param isRemove 是否移除
     * @return 值
     */
    public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    try {
                        value = URLDecoder.decode(cookie.getValue(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (isRemove) {
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
        return value;
    }

    /**
     * 设置客户端缓存过期时间 的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        // Http 1.0 header, set model fix expires date.
        response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
        // Http 1.1 header, set model time after now.
        response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }


    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // Http 1.0 header
        response.setDateHeader(HttpHeaders.EXPIRES, 1L);
        response.addHeader(HttpHeaders.PRAGMA, "no-cache");
        // Http 1.1 header
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
                                               long lastModified) {
        long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader(HttpHeaders.ETAG, etag);
                return false;
            }
        }
        return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * <p>
     * 返回的结果的Parameter名已去除前缀.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersWith(ServletRequest request, String prefix) {
        Assert.notNull(request, "Request must not be null");
        Enumeration paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<String, Object>();
        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            if ("".equals(pre) || paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values == null || values.length == 0) {
                    values = new String[]{};
                    // Do nothing, no values found at all.
                } else if (values.length > 1) {
                    params.put(unprefixed, values);
                } else {
                    params.put(unprefixed, values[0]);
                }
            }
        }
        return params;
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    public static String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = cloneInputStream(request.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 复制输入流
     *
     * @param inputStream
     * @return</br>
     */
    public static InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    /**
     * 从request中获得参数，并返回可读的Map
     * application/x-www-form-urlencode
     * application/json
     * application/json;charset=UTF-8
     * multipart/form-data
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        String contentType = request.getHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE);
        Map<String, String> returnMap = new HashMap();
        if (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            // form-data表单
            MultipartResolver multipartResolver = SpringContextHolder.getBean(MultipartResolver.class);
            MultipartHttpServletRequest multiReq = multipartResolver.resolveMultipart(request);
            returnMap = conventMap(multiReq.getParameterMap());
        } else if (MediaType.APPLICATION_JSON_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)) {
            // json表单
            String body = getBodyString(request);
            if (StringUtils.isNotBlank(body)) {
                try {
                    returnMap = JSONObject.parseObject(body, Map.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 普通表单
            returnMap = conventMap(request.getParameterMap());
        }
        // 参数Map
        return returnMap;
    }

    private static Map conventMap(Map map) {
        Map<String, String> returnMap = new HashMap();
        // 返回值Map
        Iterator entries = map.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                if (values != null && values.length > 0) {
                    for (int i = 0; i < values.length; i++) {
                        value = values[i] + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                }
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }


    /**
     * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
     */
    public static String encodeParameterWithPrefix(Map<String, Object> params, String prefix) {
        StringBuilder queryStringBuilder = new StringBuilder();

        String pre = prefix;
        if (pre == null) {
            pre = "";
        }
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
            if (it.hasNext()) {
                queryStringBuilder.append("&");
            }
        }
        return queryStringBuilder.toString();
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     */
    public static String encodeHttpBasic(String userName, String password) {
        String encode = userName + ":" + password;
        return "Basic " + EncodeUtils.encodeBase64(encode.getBytes());
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString())) || (request.getHeader("Content-Type") != null && request.getHeader("Content-Type").startsWith("application/json"));
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 0) {
            String[] ips = ip.split(",");
            if (ips.length > 0) {
                ip = ips[0];
            }
        }
        return ip;
    }


    /**
     * 判断访问URI是否是静态文件请求
     *
     * @throws Exception
     */
    public static boolean isStaticFile(String uri) {
        return StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, new String[]{urlSuffix})
                && !StringUtils.endsWithAny(uri, new String[]{".jsp"}) && !StringUtils.endsWithAny(uri, new String[]{".java"});
    }

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    public static void writeJson(HttpServletResponse response, Object object) {
        writeJson(response, JSON.toJSONString(object), MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    public static void writeJson(HttpServletResponse response, String string, String type) {
        try {
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
        }
    }

    public static String getServerUrl(HttpServletRequest request) {
        String url = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + request.getContextPath();
        return url;
    }


    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, String> getHttpHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        if (request != null) {
            Enumeration<String> enumeration = request.getHeaderNames();
            if (enumeration != null) {
                while (enumeration.hasMoreElements()) {
                    String key = enumeration.nextElement();
                    String value = request.getHeader(key);
                    map.put(key, value);
                }
            }
        }

        return map;
    }
}
