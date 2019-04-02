package com.github.lyd.msg.client.api;

import com.github.lyd.common.model.ResultBody;
import com.github.lyd.msg.client.model.HttpNotify;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 推送通知
 * @author woodev
 */
public interface HttpNotifyRemoteApi {

    /**
     * HTTP异步通知
     *
     * @param httpNotify
     * @return
     */
    @ApiOperation("HTTP异步通知")
    @PostMapping("/http/notify")
    ResultBody<String> sendHttpNotify(
            @RequestBody HttpNotify httpNotify
    );
}
