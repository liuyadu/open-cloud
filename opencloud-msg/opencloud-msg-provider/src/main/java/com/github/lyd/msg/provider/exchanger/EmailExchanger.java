package com.github.lyd.msg.provider.exchanger;


import com.github.lyd.msg.client.model.EmailNotify;
import com.github.lyd.msg.client.model.Notify;
import com.github.lyd.msg.provider.service.EmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author woodev
 */
@Slf4j
public class EmailExchanger implements MessageExchanger {

    private EmailSender mailSender;
    public EmailExchanger(EmailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean support(Object notification) {
        return notification.getClass().equals(EmailNotify.class);
    }

    @Override
    public boolean exchange(Notify notify) {
        Assert.notNull(mailSender, "邮件接口没有初始化");
        EmailNotify mailMessage = (EmailNotify) notify;
        mailSender.sendHtmlMail(mailMessage);
        return true;
    }
}
