package com.chryl.service.mq;

import com.chryl.dao.OrderDao;
import com.chryl.dao.TxLogDao;
import com.chryl.po.ChrOrder;
import com.chryl.po.ChrTxLog;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;


/**
 * 测试事务消息! 发送半事务消息,定义本地事务
 * Created by Chr.yl on 2020/6/26.
 *
 * @author Chr.yl
 */
@Service
public class MqService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private TxLogDao txLogDao;


    //外部使用该方法创建订单
    public void createOrderBefore(ChrOrder chrOrder) {
        //该 id 需要 贯穿传递
        String txId = UUID.randomUUID().toString().replaceAll("-", "");

        //第一步:发送半事务消息
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                "tx_producer_group",//txProducerGroup 事务生产者组名,与@RocketMQTransactionListener 的txProducerGroup对应
                "tx_order_topic",//主题
                MessageBuilder.withPayload(chrOrder).setHeader("txId", txId).build(),//Message 类型 消息
                chrOrder//参数
        );

        //新api
//        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
//                //这里无法指定发送的 txProducerGroup, api更新了
//                "tx_order_topic",//主题
//                MessageBuilder.withPayload(chrOrder).setHeader("txId", txId).build(),//Message 类型 消息
//                chrOrder//参数
//        );
        if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
            //第二步:发送半事务成功
        } else {
            //发送失败
        }
    }


    //本地事务:只为内部使用:保存订单和事务日志放在一个 本地事务中,只要保存订单成功,那么一定就有本地事务日志
    @Transactional
    public void createOrder(String txId, ChrOrder chrOrder) {
        //保存订单
        orderDao.save(chrOrder);
        //事务日志记录
        ChrTxLog chrTxLog = new ChrTxLog();
        chrTxLog.setTxId(txId);
        chrTxLog.setDate(new Date());
        txLogDao.save(chrTxLog);
    }

}
