package com.opencloud.task.server.job;

import com.alibaba.fastjson.JSONObject;
import com.opencloud.common.security.http.OpenRestTemplate;
import com.opencloud.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * 微服务远程调用任务
 *
 * @author liuyadu
 */
@Slf4j
public class HttpExecuteJob implements Job {
    @Autowired
    private OpenRestTemplate openRestTemplate;

    /**
     * 负载均衡
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws RuntimeException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String serviceId = dataMap.getString("serviceId");
        String method = dataMap.getString("method");
        method = StringUtils.isBlank(method) ? "POST" : method;
        String path = dataMap.getString("path");
        String contentType = dataMap.getString("contentType");
        contentType = StringUtils.isBlank(contentType) ? MediaType.APPLICATION_FORM_URLENCODED_VALUE : contentType;
        String body = dataMap.getString("body");
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        // 获取服务实例
        if (serviceInstance == null) {
            throw new RuntimeException(String.format("%s服务暂不可用", serviceId));
        }
        String url = String.format("%s%s", serviceInstance.getUri(), path);
        HttpHeaders headers = new HttpHeaders();
        HttpMethod httpMethod = HttpMethod.resolve(method.toUpperCase());
        HttpEntity requestEntity = null;
        headers.setContentType(MediaType.parseMediaType(contentType));
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            // json格式
            requestEntity = new HttpEntity(body, headers);
        } else {
            // 表单形式
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap();
            if (StringUtils.isNotBlank(body)) {
                Map data = JSONObject.parseObject(body, Map.class);
                params.putAll(data);
                requestEntity = new HttpEntity(params, headers);
            }
        }
        log.debug("==> url[{}] method[{}] data=[{}]", url, httpMethod, requestEntity);
        ResponseEntity<String> result = openRestTemplate.exchange(url, httpMethod, requestEntity, String.class);
        System.out.println(result.getBody());
    }
}
