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

        /**
         * 理解: 为什么是半事务消息呢,因为半事务消息第一次被发送给服务端时,是不具有被投递的状态的,
         * 他会返回给消息的发送方,消息发送方如果返回一个COMMIT操作,表示发送方收到消息发送的二次通知,表示消息被发送成功,也就是消息被投递成功,俗称可靠消息
         * 只有返回投递成功,才具有被其他消费者消费的状态(可投递状态)
         */
        //第一步:发送半事务消息,既能回滚的消息
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
                "tx_producer_group",//txProducerGroup 事务生产者组名,与@RocketMQTransactionListener 的txProducerGroup对应
                "tx_order_topic",//主题
                MessageBuilder.withPayload(chrOrder).setHeader("txId", txId).build(),//Message 类型 消息,消息类型为消息加载体和消息头信息,消息头信息写入txLog事务日志记录
                chrOrder//参数
        );

        //新api:新api,新版本的jar运行出错
//        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(
//                //这里无法指定发送的 txProducerGroup, api更新了
//                "tx_order_topic",//主题
//                MessageBuilder.withPayload(chrOrder).setHeader("txId", txId).build(),//Message 类型 消息
//                chrOrder//参数
//        );
        if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {//第一次消息发送确认,(一次确认)
            //第二步:不代表半事务消息成功,还需要等待发送方的确认收到(COMMIT操作)
            //事务开始....
        } else {
            //发送失败:半事务消息投递失败
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
