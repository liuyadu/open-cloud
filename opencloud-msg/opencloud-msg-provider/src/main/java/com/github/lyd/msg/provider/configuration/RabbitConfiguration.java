/*
 * MIT License
 *
 * Copyright (c) 2018 yadu.liu
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.github.lyd.msg.provider.configuration;

import com.github.lyd.msg.client.exchange.DelayExchangeBuilder;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mq配置
 * @author liuyadu
 */
@Configuration
public class RabbitConfiguration {
    /**
     * http异步通知队列
     */
    public final static  String HTTP_NOTIFY_QUEUE = "openCloud.notify.http.queue";

    /**
     * http异步通知队列路由key
     */
    public final static  String HTTP_NOTIFY_QUEUE_RK = "openCloud.notify.http.queue.rk";

    /**
     * 延时队列交换机
     * 注意这里的交换机类型：CustomExchange
     * 创建exchange时指定exchange_type为x-delayed-message
     * 添加参数，这里指定exchange类型arguments={"x-delayed-type": "fanout"}
     * 添加消息到队列时添加
     * headers={'x-delay': 8000}
     *
     * @return
     */
    @Bean
    public CustomExchange delayExchange() {
        return DelayExchangeBuilder.buildExchange();
    }

    /**
     * HTTP通知队列
     *
     * @return
     */
    @Bean
    public Queue httpNotifyQueue() {
        return new Queue(HTTP_NOTIFY_QUEUE, true);
    }

    /**
     * HTTP通知队列绑定延迟交换器
     *
     * @return
     */
    @Bean
    public Binding httpNotifyQueueBinding(Queue httpNotifyQueue, Exchange delayExchange) {
        return BindingBuilder.bind(httpNotifyQueue).to(delayExchange).with(HTTP_NOTIFY_QUEUE_RK).noargs();
    }
}
