package com.github.lyd.base.provider.listener;

import com.github.lyd.base.client.model.entity.BaseResourceApi;
import com.github.lyd.base.provider.service.BaseResourceApiService;
import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.http.OpenRestTemplate;
import com.github.lyd.common.utils.BeanConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.Map;

/**
 * mq消息接收者
 *
 * @author liuyadu
 */
@Configuration
@Slf4j
public class MessageHandler {
    @Autowired
    private BaseResourceApiService baseResourceApiService;
    @Autowired
    private OpenRestTemplate restTemplate;

    /**
     * 接收API资源扫描消息
     *
     * @param list
     */
    @RabbitListener(queues = MqConstants.QUEUE_SCAN_API_RESOURCE)
    public void ScanApiResourceQueue(@Payload List<Map> list) {
        try {
            if (list != null && list.size() > 0) {
                log.info("【apiResourceQueue监听到消息】");
                for (Map map : list) {
                    try {
                        BaseResourceApi api = BeanConvertUtils.mapToObject(map, BaseResourceApi.class);
                        BaseResourceApi save = baseResourceApiService.getApi(api.getApiCode(), api.getServiceId());
                        if (save == null) {
                            api.setIsOpen(0);
                            api.setIsAuth(1);
                            api.setIsPersist(1);
                            baseResourceApiService.addApi(api);
                        } else {
                            api.setIsOpen(null);
                            api.setApiId(save.getApiId());
                            baseResourceApiService.updateApi(api);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("添加资源error:", e.getMessage());
                    }
                }
                restTemplate.refreshGateway();
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }
}
