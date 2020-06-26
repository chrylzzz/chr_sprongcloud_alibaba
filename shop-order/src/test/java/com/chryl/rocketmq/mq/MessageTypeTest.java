package com.chryl.rocketmq.mq;

import com.chryl.OrderApplication;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试消息类型:普通消息,顺序消息
 * Created by Chr.yl on 2020/6/26.
 *
 * @author Chr.yl
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class MessageTypeTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 普通消息
     */
    //同步消息,不许返回再往下执行
    @Test
    public void show() {
        //参数1:topic ,参数2:消息体,参数3:超时时间
        //可以在topic后加上冒号 : , 就代表 tag标签的内容
        //同步发送,必须返回内容才能往下执行,否则阻塞
        SendResult sendResult =
                rocketMQTemplate.syncSend("test-topic-1:tag", "test-1-content", 10000);
        System.out.println(sendResult);

    }

    //异步,发送方发送了数据之后,通过回调来响应,接着发送下一个数据,一般使用在耗时较长的的场景
    @Test
    public void show2() {
        //参数1:topic
        //参数2:消息体
        //参数3:回调
        rocketMQTemplate.asyncSend("test-topic-1:tag", "test-1-content", new SendCallback() {
            //成功响应的回调
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }

            //异常回调
            @Override
            public void onException(Throwable throwable) {
                System.out.println(throwable);
            }
        });
        //上面的async 方法执行完之后,会继续执行打印 ====   ,然后再 执行 onSuccess() 或onException() 方法
        System.out.println("============");
    }


    //单项发送 ,只发送 不应答,只发送.发完不管了, 使用在耗时短,可靠性不高的场景,日志收集
    @Test
    public void show3() {
        rocketMQTemplate.sendOneWay("test-topic-1:tag", "test-1-content");
    }


    /**
     * 顺序消息,在普通消息的每个api中都提供了Orderly方法,来决定发送的顺序性!!!!!!
     */
    //单项顺序消息
    @Test
    public void show4() {
        //第三个参数,保证不重复即可,决定发送到那个队列,取hash 判断在那个队列
        rocketMQTemplate.sendOneWayOrderly("test-topic-1:tag", "test-1-content", "xxx");
    }



}
