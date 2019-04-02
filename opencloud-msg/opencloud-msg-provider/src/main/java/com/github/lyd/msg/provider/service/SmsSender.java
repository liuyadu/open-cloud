package com.github.lyd.msg.provider.service;

import com.github.lyd.msg.client.model.SmsNotify;

/**
 * @author woodev
 */
public interface SmsSender {

	/**
	 * 发送短信
	 * @param parameter
	 * @return
	 */
	Boolean send(SmsNotify parameter);
}
