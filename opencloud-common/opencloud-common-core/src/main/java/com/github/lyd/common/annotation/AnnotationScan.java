package com.github.lyd.common.annotation;

import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.utils.EncryptUtils;
import com.github.lyd.common.utils.StringUtils;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 自定义注解扫描
 *
 * @author liuyadu
 */
@Slf4j
public class AnnotationScan implements ApplicationListener<ApplicationReadyEvent> {
    private AmqpTemplate amqpTemplate;

    public AnnotationScan(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    /**
     * 初始化方法
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> resourceServer = applicationContext.getBeansWithAnnotation(EnableResourceServer.class);
        if (resourceServer == null || resourceServer.isEmpty()) {
            // 只扫描资源服务器
            return;
        }
        amqpTemplate = applicationContext.getBean(RabbitTemplate.class);
        Environment env = applicationContext.getEnvironment();
        String serviceId = env.getProperty("spring.application.name", "application");
        log.info("ApplicationReadyEvent:{}", serviceId);
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            if (method.getMethod().getDeclaringClass().getAnnotation(RestController.class) == null) {
                // 只扫描RestController
                continue;
            }
            if (method.getMethodAnnotation(ApiIgnore.class) != null) {
                // 忽略的接口不扫描
                continue;
            }
            Set<MediaType> mediaTypeSet = info.getProducesCondition().getProducibleMediaTypes();
            for (MethodParameter params : method.getMethodParameters()) {
                if (params.hasParameterAnnotation(RequestBody.class)) {
                    mediaTypeSet.add(MediaType.APPLICATION_JSON_UTF8);
                    break;
                }

            }
            String mediaTypes = getMediaTypes(mediaTypeSet);
            // 请求类型
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            String methods = getMethods(methodsCondition.getMethods());
            // 请求路径
            PatternsRequestCondition p = info.getPatternsCondition();
            String urls = getUrls(p.getPatterns());
            Map<String, String> api = Maps.newHashMap();
            // 类名
            String className = method.getMethod().getDeclaringClass().getName();
            // 方法名
            String methodName = method.getMethod().getName();
            String fullName = className + "." + methodName;
            // md5码
            String md5 = EncryptUtils.md5Hex(fullName);
            String name = "";
            String desc = "";

            ApiOperation apiOperation = method.getMethodAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                name = apiOperation.value();
                desc = apiOperation.notes();
            }
            name = StringUtils.isBlank(name) ? fullName : name;
            api.put("apiName", name);
            api.put("apiCode", md5);
            api.put("apiDesc", desc);
            api.put("path", urls);
            api.put("className", className);
            api.put("methodName", methodName);
            api.put("md5", md5);
            api.put("requestMethod", methods);
            api.put("serviceId", serviceId);
            api.put("contentType", mediaTypes);
            list.add(api);
        }
        if (amqpTemplate != null) {
            // 发送mq扫描消息
            amqpTemplate.convertAndSend(MqConstants.QUEUE_SCAN_API_RESOURCE, list);
        }
    }


    private String getMediaTypes(Set<MediaType> mediaTypes) {
        StringBuilder sbf = new StringBuilder();
        for (MediaType mediaType : mediaTypes) {
            sbf.append(mediaType.toString()).append(",");
        }
        if (mediaTypes.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getMethods(Set<RequestMethod> requestMethods) {
        StringBuilder sbf = new StringBuilder();
        for (RequestMethod requestMethod : requestMethods) {
            sbf.append(requestMethod.toString()).append(",");
        }
        if (requestMethods.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getUrls(Set<String> urls) {
        StringBuilder sbf = new StringBuilder();
        for (String url : urls) {
            sbf.append(url).append(",");
        }
        if (urls.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }


}
