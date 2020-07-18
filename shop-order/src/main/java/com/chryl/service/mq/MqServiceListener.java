package com.chryl.service.mq;


import com.chryl.dao.TxLogDao;
import com.chryl.po.ChrOrder;
import com.chryl.po.ChrTxLog;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * 测试 监听事务消息(监听半事务消息,一旦监听到半事务消息,立刻拉取): 接收半事务消息,执行本地事务,出错回查机制
 * 用来监听下单 消息
 * Created by Chr.yl on 2020/6/26.
 *
 * @author Chr.yl
 */
@Service
@RocketMQTransactionListener(txProducerGroup = "tx_producer_group")//与sendMessageInTransaction()中的txProducerGroup对应
//@RocketMQTransactionListener//(txProducerGroup="这里无法指定监听的哪个组,api更新了")//新api
public class MqServiceListener implements RocketMQLocalTransactionListener {//实现事务消息

    @Autowired
    private MqService mqService;
    @Autowired
    private TxLogDao txLogDao;


    //执行本地事务:半事务消息发送成功后 执行的该处,回调处理本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg,//对应半事务的Message<?> message
                                                                 Object arg//对应半事务的Object args
    ) {
        String txId = (String) msg.getHeaders().get("txId");//将事务id存入message的header中
        try {
            //第三步:调用本地事务
            mqService.createOrder(txId, (ChrOrder) arg);
            //第四步:没有异常,成功,那么该半事务消息就会是可投递状态
            return RocketMQLocalTransactionState.COMMIT;//第二次消息发送确认(二次确认),修改暂不能投递的状态为可有地状态
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;//没有投递的状态,通知rocketmq回滚事务消息(暂时存储,三天以后删除)
        }
    }

    /**
     * 理解: 这里的消息回查,是针对第二次消息确认的,如果消息一直没有收到二次确认 COMMIT 操作,那么就会执行消息回查
     */
    //第五步:消息回查,如果本地事务出错,服务开启会第一时间进行回查,如果启动了回查,就去从该消息里查询txLog事务日志
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        //因为本地事务,保存订单 和保存事务日志,都是同时成功或者同时失败
        //只要查得到事务日志,那么就查得到订单信息
        String txId = (String) msg.getHeaders().get("txId");//只要将信息放到消息中,微服务中断,只要rocketmq服务器没有挂机,就能得到之前存储的消息
        ChrTxLog chrTxLog = null;
        try {
            chrTxLog = txLogDao.findById(txId).get();
            if (chrTxLog != null) {
                //本地事务成功,进行二次通知
                return RocketMQLocalTransactionState.COMMIT;
            } else {
                //失败
                return RocketMQLocalTransactionState.ROLLBACK;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //失败
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }
}
