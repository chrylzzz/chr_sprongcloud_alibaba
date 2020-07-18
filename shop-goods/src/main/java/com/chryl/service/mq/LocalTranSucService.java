package com.chryl.service.mq;

import com.chryl.po.ChrOrder;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 半事务消息投递成功,可靠消息在此消费
 * Created by Chr.yl on 2020/7/18.
 *
 * @author Chr.yl
 */
@Service
@RocketMQMessageListener(consumerGroup = "goods_group", topic = "tx_order_topic")
public class LocalTranSucService implements RocketMQListener<ChrOrder> {

    @Override
    public void onMessage(ChrOrder chrOrder) {
        System.out.println(chrOrder);
    }
}
