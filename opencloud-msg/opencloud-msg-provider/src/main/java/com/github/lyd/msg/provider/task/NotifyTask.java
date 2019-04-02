package com.github.lyd.msg.provider.task;

import com.github.lyd.msg.client.model.Notify;
import com.github.lyd.msg.provider.exchanger.MessageExchanger;

import java.util.concurrent.Callable;

/**
 * @author woodev
 */
public class NotifyTask implements Callable<Boolean> {

    private MessageExchanger exchanger;

    private Notify notify;

    public NotifyTask(MessageExchanger exchanger, Notify notify){
        this.exchanger = exchanger;
        this.notify = notify;
    }

    @Override
    public Boolean call() throws Exception {
        return exchanger.exchange(notify);
    }
}
