package com.github.lyd.msg.provider.controller;

import com.github.lyd.common.model.ResultBody;
import com.github.lyd.msg.client.api.EmailRemoteApi;
import com.github.lyd.msg.client.model.EmailNotify;
import com.github.lyd.msg.provider.dispatcher.MessageDispatcher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodev
 */
@RestController
@Api(value = "邮件", tags = "邮件")
public class EmailController implements EmailRemoteApi {

    @Autowired
    private MessageDispatcher dispatcher;

    @ApiOperation(value = "发送邮件",notes = "发送邮件")
    @PostMapping("/email")
    @Override
    public ResultBody<String> sendEmail(
            @RequestParam(value = "to", required = true) String to,
            @RequestParam(value = "title", required = true) String title,
            @RequestParam(value = "content", required = true) String content
    ) {
        EmailNotify mailMessage = new EmailNotify();
        mailMessage.setTo(to);
        mailMessage.setTitle(title);
        mailMessage.setContent(content);
        this.dispatcher.dispatch(mailMessage);
        return ResultBody.success("");
    }



}
