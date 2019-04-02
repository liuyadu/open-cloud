package com.github.lyd.gateway.provider.listener;

import com.github.lyd.common.constants.MqConstants;
import com.github.lyd.common.utils.BeanConvertUtils;
import com.github.lyd.gateway.client.model.entity.GatewayAccessLogs;
import com.github.lyd.gateway.provider.mapper.GatewayLogsMapper;
import com.github.lyd.gateway.provider.service.IpRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

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
    private GatewayLogsMapper gatewayLogsMapper;

    /**
     * 临时存放减少io
     */
    @Autowired
    private IpRegionService ipRegionService;

    /**
     * 接收访问日志
     *
     * @param access
     */
    @RabbitListener(queues = MqConstants.QUEUE_ACCESS_LOGS)
    public void accessLogsQueue(@Payload Map access) {
        try {
            if (access != null) {
                GatewayAccessLogs logs = BeanConvertUtils.mapToObject(access, GatewayAccessLogs.class);
                if (logs != null && logs.getAccessId() != null) {
                    if (logs.getIp() != null) {
                        logs.setRegion(ipRegionService.getRegion(logs.getIp()));
                    }
                    logs.setUseTime(logs.getResponseTime().getTime() - logs.getRequestTime().getTime());
                    gatewayLogsMapper.insertSelective(logs);
                }
            }
        } catch (Exception e) {
            log.error("error:", e);
        }
    }
}
