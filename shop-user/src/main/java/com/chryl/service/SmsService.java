package com.chryl.service;

import com.chryl.po.ChrOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * rocketmq 消费者:短信业务
 * 实现RocketMQListener<T> :泛型就是消费的具体内容
 * Created by Chr.yl on 2020/6/25.
 *
 * @author Chr.yl
 */
@Slf4j
@Service                //消费者组名 ,消费的主题
@RocketMQMessageListener(consumerGroup = "user-group", topic = "order-topic")
public class SmsService implements RocketMQListener<ChrOrder> {

    //监听到消息后,消费逻辑
    @Override
    public void onMessage(ChrOrder chrOrder) {

        log.info("接收到了订单信息,发送短息:{}", chrOrder);
    }
}
