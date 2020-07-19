package com.chryl.rocketmq.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

/**
 * Created by Chr.yl on 2020/6/25.
 *
 * @author Chr.yl
 */
public class RocketSender {
    //发送消息
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        //创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("chryl-group");
        //生产者设置name server
        producer.setNamesrvAddr("192.168.228.128:9876");

        //启动生产者
        producer.start();
        //创建消息对象: 主题 内容 标签
        Message message = new Message("myTopic", "myTag", ("test rocket message").getBytes());
        //设置延时登记
        message.setDelayTimeLevel(2);
        //发送消息
        SendResult sendResult = producer.send(message, 10000);
        System.out.println("发送结果:"+sendResult);

        TimeUnit.SECONDS.sleep(1);
        //关闭生产者
        producer.shutdown();
    }
}
