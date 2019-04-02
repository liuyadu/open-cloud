package com.github.lyd.msg.client.api;

import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 推送通知
 * @author woodev
 */
public interface SmsRemoteApi {
    /**
     * 短信通知
     *
     * @param phoneNumber
     * @param templateCode
     * @param signName
     * @param params
     * @return
     */
    @ApiOperation("发送短信")
    @PostMapping("/sms")
    ResultBody<String> sendSms(
            @RequestParam(value = "phoneNumber", required = true) String phoneNumber,
            @RequestParam(value = "templateCode", required = true) String templateCode,
            @RequestParam(value = "signName", required = false) String signName,
            @RequestParam(value = "params", required = false) String params
    );

}
