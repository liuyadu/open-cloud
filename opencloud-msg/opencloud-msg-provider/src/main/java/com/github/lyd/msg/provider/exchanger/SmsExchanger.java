package com.github.lyd.msg.provider.exchanger;

import com.github.lyd.msg.client.model.Notify;
import com.github.lyd.msg.client.model.SmsNotify;
import com.github.lyd.msg.provider.service.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author woodev
 */
@Slf4j
public class SmsExchanger implements MessageExchanger {

    private SmsSender smsSender;

    public SmsExchanger(SmsSender smsSender) {
        if (smsSender != null) {
            log.info("init sms sender");
        }
        this.smsSender = smsSender;
    }

    private String signName = "签名";

    @Override
    public boolean support(Object message) {
        return message.getClass().equals(SmsNotify.class);
    }

    @Override
    public boolean exchange(Notify notify) {
        Assert.notNull(smsSender, "短信接口没有初始化");
        SmsNotify smsNotify = (SmsNotify) notify;
        return smsSender.send(smsNotify);
    }
}
