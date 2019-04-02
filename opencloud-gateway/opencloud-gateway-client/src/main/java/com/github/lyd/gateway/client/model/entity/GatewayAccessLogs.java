package com.github.lyd.gateway.client.model.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 开放网关-访问日志
 * @author liuyadu
 */
@Table(name = "gateway_access_logs")
public class GatewayAccessLogs implements Serializable {
    /**
     * 访问ID
     */
    @Id
    @Column(name = "access_id")
    private Long accessId;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 响应状态
     */
    @Column(name = "http_status")
    private String httpStatus;

    /**
     * 请求时间
     */
    @Column(name = "request_time")
    private Date requestTime;

    /**
     * 响应时间
     */
    @Column(name = "response_time")
    private Date responseTime;

    /**
     * 耗时
     */
    @Column(name = "use_time")
    private Long useTime;

    /**
     * 请求数据
     */
    private String params;

    /**
     * 请求头
     */
    private String headers;

    @Column(name = "user_agent")
    private String userAgent;

    /**
     *区域
     */
    @Column(name = "region")
    private String region;

    /**
     * 认证用户信息
     */
    private String authentication;

    /**
     * 服务器IP
     */
    @Column(name = "server_ip")
    private String serverIp;

    /**
     * 服务名
     */
    @Column(name = "service_id")
    private String serviceId;

    /**
     * 错误信息
     */
    private String error;

    private static final long serialVersionUID = 1L;

    /**
     * 获取访问ID
     *
     * @return access_id - 访问ID
     */
    public Long getAccessId() {
        return accessId;
    }

    /**
     * 设置访问ID
     *
     * @param accessId 访问ID
     */
    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    /**
     * 获取访问路径
     *
     * @return path - 访问路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置访问路径
     *
     * @param path 访问路径
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * @return method
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * 获取请求IP
     *
     * @return ip - 请求IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置请求IP
     *
     * @param ip 请求IP
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取响应状态
     *
     * @return http_status - 响应状态
     */
    public String getHttpStatus() {
        return httpStatus;
    }

    /**
     * 设置响应状态
     *
     * @param httpStatus 响应状态
     */
    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus == null ? null : httpStatus.trim();
    }

    /**
     * 获取请求时间
     *
     * @return request_time - 请求时间
     */
    public Date getRequestTime() {
        return requestTime;
    }

    /**
     * 设置请求时间
     *
     * @param requestTime 请求时间
     */
    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * 获取响应时间
     *
     * @return response_time - 响应时间
     */
    public Date getResponseTime() {
        return responseTime;
    }

    /**
     * 设置响应时间
     *
     * @param responseTime 响应时间
     */
    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * 获取耗时
     *
     * @return use_time - 耗时
     */
    public Long getUseTime() {
        return useTime;
    }

    /**
     * 设置耗时
     *
     * @param useTime 耗时
     */
    public void setUseTime(Long useTime) {
        this.useTime = useTime;
    }

    /**
     * 获取请求数据
     *
     * @return params - 请求数据
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置请求数据
     *
     * @param params 请求数据
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * 获取请求头
     *
     * @return headers - 请求头
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * 设置请求头
     *
     * @param headers 请求头
     */
    public void setHeaders(String headers) {
        this.headers = headers == null ? null : headers.trim();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
