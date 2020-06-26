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
 * Created by Chr.yl on 2020/6/26.
 *
 * @author Chr.yl
 */
@Service
@RocketMQTransactionListener//(txProducerGroup="这里无法指定监听的哪个组,api更新了")
public class MqServiceListener implements RocketMQLocalTransactionListener {

    @Autowired
    private MqService mqService;
    @Autowired
    private TxLogDao txLogDao;


    //执行本地事务:半事务消息发送成功后 执行的该处,回调处理本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg,//对应半事务的Message
                                                                 Object arg//对应半事务的args
    ) {
        String txId = (String) msg.getHeaders().get("txId");
        try {
            //第三步:调用本地事务
            mqService.createOrder(txId, (ChrOrder) arg);
            //第四步:没有异常,成功
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //第五步:消息回查,如果本地事务出错,服务开启会第一时间进行回查
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        //因为本地事务,保存订单 和保存事务日志,都是同时成功或者同时失败
        //只要查得到事务日志,那么就查得到订单信息
        String txId = (String) msg.getHeaders().get("txId");
        ChrTxLog chrTxLog = txLogDao.findById(txId).get();
        if (chrTxLog != null) {
            //本地事务成功
            return RocketMQLocalTransactionState.COMMIT;
        } else {
            //失败
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
