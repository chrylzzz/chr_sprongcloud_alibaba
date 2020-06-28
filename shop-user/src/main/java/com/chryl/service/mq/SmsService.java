package com.chryl.service.mq;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.chryl.dao.UserDao;
import com.chryl.po.ChrOrder;
import com.chryl.po.ChrUser;
import com.chryl.util.SmsUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * rocketmq 消费者:短信业务
 * 实现RocketMQListener<T> :泛型就是消费的具体内容
 * Created by Chr.yl on 2020/6/25.
 *
 * @author Chr.yl
 */
@Slf4j
@Service("shopSmsService")//SmsService对象名被alibaba使用了
@RocketMQMessageListener(
        consumerGroup = "user-group",//消费者组名
        topic = "order-topic"//,//消费的主题
//        consumeMode = ConsumeMode.ORDERLY,//消费模式,指定是否顺序消费: CONCURRENTLY(无序,同步消费,默认值) ;ORDERLY(顺序消费)
//        messageModel =  MessageModel.CLUSTERING//消息模式: BROADCASTING(广播模式) ,CLUSTERING(集群模式,默认值)
)
public class SmsService implements RocketMQListener<ChrOrder> {

    @Autowired
    private UserDao userDao;

    //监听到消息后,消费逻辑
    @Override
    public void onMessage(ChrOrder chrOrder) {

        log.info("接收到了订单信息,发送短息:{}", chrOrder);

        ChrUser chrUser = userDao.findById(chrOrder.getUid()).get();
        //获取手机号
        String phone = chrUser.getPhone();
        String signName = "浪维";
        String templateCode = "SMS_193509750";
        Param param = new Param();
        //生成验证码
        try {
            SmsUtil.sendSms(phone, signName, templateCode, JSON.toJSONString(param));
            log.info("短信发送成功");
        } catch (ClientException e) {
            e.printStackTrace();

        }
    }

    //短信参数内部类
    @Data
    class Param {
        private String code;

        public Param() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 6; i++) {//自定义短信码
                stringBuilder.append(new Random().nextInt(9) + 1);
            }
            this.code = stringBuilder.toString();
        }
    }

}
