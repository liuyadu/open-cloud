package com.github.lyd.msg.provider.dispatcher;

import com.github.lyd.msg.client.model.Notify;
import com.github.lyd.msg.provider.exchanger.MessageExchanger;
import com.github.lyd.msg.provider.task.NotifyTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author woodev
 */
@Component
@Slf4j
public class MessageDispatcher implements ApplicationContextAware{

    private Collection<MessageExchanger> exchangers;

    private ExecutorService executorService;

    public MessageDispatcher(){
        Integer availableProcessors = Runtime.getRuntime().availableProcessors();
        Integer numOfThreads = availableProcessors * 2;
        executorService = new ThreadPoolExecutor(numOfThreads,numOfThreads,0,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>());
        log.info("Init Notify ExecutorService , numOfThread : " + numOfThreads);
    }

    public void dispatch(Notify notification){
        if(notification != null && exchangers != null){
            exchangers.forEach((exchanger) -> {
                if(exchanger.support(notification)){
                    //添加到线程池进行处理
                    executorService.submit(new NotifyTask(exchanger,notification));
                }
            });
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, MessageExchanger> beansOfType = applicationContext.getBeansOfType(MessageExchanger.class);
        this.exchangers = beansOfType.values();
    }
}