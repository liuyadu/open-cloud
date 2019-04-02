package com.github.lyd.msg.provider.exchanger;


import com.github.lyd.msg.client.model.Notify;

/**
 * @author woodev
 */

public interface MessageExchanger {

    boolean support(Object message);

    boolean exchange(Notify notify);
}
