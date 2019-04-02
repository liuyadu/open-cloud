package com.github.lyd.msg.client.model;

import com.google.common.collect.Maps;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * http通知消息内容
 *
 * @author liuyadu
 */
public class HttpNotify extends Notify {
    private static final long serialVersionUID = 1566807113989212480L;
    /**
     * 通知路径
     */
    @NotNull(message = "通知路径不能为空")
    private String url;
    /**
     * 请求内容
     */
    private Map<String, String> data = Maps.newLinkedHashMap();
    /**
     * 通知类型
     */
    private String type;

    public HttpNotify() {
        super();
    }

    /**
     * 构建消息
     *
     * @param url  通知地址
     * @param type 通知业务类型
     * @param data 请求数据
     */
    public HttpNotify(String url, String type, Map<String, String> data) {
        this.url = url;
        this.data = data;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpNotifyMsg{");
        sb.append("url='").append(url).append('\'');
        sb.append(", data=").append(data);
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
