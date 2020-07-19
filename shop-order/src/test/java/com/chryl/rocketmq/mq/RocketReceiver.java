package com.chryl.rocketmq.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * Created by Chr.yl on 2020/6/25.
 *
 * @author Chr.yl
 */
public class RocketReceiver {
    //接收 消息
    public static void main(String[] args) throws MQClientException {
        //创建消费者,指定消费者 组,
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("chryl-group");
        //为消费者设置 nameserver
        consumer.setNamesrvAddr("192.168.228.128:9876");

        //设置订阅的 主题和标签
        consumer.subscribe("myTopic", "*");
        //设置回调函数,编写接收消息后的处理方法
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //处理获取到的消息
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //消费逻辑
                System.out.println("message---: " + list);
                System.out.println("延迟时间:" + (System.currentTimeMillis() - list.get(0).getStoreTimestamp()));
                //消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();

    }
}
