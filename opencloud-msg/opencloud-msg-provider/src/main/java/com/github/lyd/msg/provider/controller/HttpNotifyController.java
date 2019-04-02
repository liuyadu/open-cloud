package com.github.lyd.msg.provider.controller;

import com.github.lyd.common.model.ResultBody;
import com.github.lyd.msg.client.api.HttpNotifyRemoteApi;
import com.github.lyd.msg.client.model.HttpNotify;
import com.github.lyd.msg.provider.service.DelayMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author woodev
 */
@RestController
@Api(value = "异步通知", tags = "异步通知")
public class HttpNotifyController implements HttpNotifyRemoteApi {

    @Autowired
    private DelayMessageService delayMessageService;

    @ApiOperation(value = "发送HTTP异步通知",notes = "发送HTTP异步通知")
    @PostMapping("/http/notify")
    @Override
    public ResultBody<String> sendHttpNotify(
           @Valid @RequestBody HttpNotify httpNotify
    ) {
        try {
            delayMessageService.httpNotify(httpNotify.getUrl(), httpNotify.getType(), httpNotify.getData());
            return ResultBody.success("");
        } catch (Exception e) {
            return ResultBody.failed(e.getMessage());
        }
    }


}
