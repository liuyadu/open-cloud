package com.github.lyd.msg.client.api;

import com.github.lyd.common.model.ResultBody;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 发送邮件接口
 * @author woodev
 */
public interface EmailRemoteApi {

    /**
     * 邮件通知
     *
     * @param to
     * @param title
     * @param content
     * @return
     */
    @ApiOperation("邮件通知")
    @PostMapping("/email")
    ResultBody<String> sendEmail(
            @RequestParam(value = "to", required = true) String to,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "content", required = true) String content
    );


}
