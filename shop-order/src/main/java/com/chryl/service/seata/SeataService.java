package com.chryl.service.seata;

import com.alibaba.fastjson.JSON;
import com.chryl.client.GoodsClient;
import com.chryl.dao.OrderDao;
import com.chryl.po.ChrGoods;
import com.chryl.po.ChrOrder;
import com.chryl.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Chr.yl on 2020/6/27.
 *
 * @author Chr.yl
 */
@Slf4j
@Service
public class SeataService {

    @Autowired
    GoodsClient goodsClient;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @GlobalTransactional//seata 分布式事务
    public ChrOrder createSeataOrder(Integer id) {
        log.info("查询商品信息");
        //获取商品信息
        ChrGoods goodsInfo = goodsClient.getGoodsInfo(id);
        ChrOrder chrOrder = new ChrOrder();
        if (goodsInfo == null) {
            chrOrder.setGoodsname("fail");
            return chrOrder;
        }
        //生成订单
        log.info("商品信息为:{}", JSON.toJSONString(goodsInfo));
        chrOrder.setUid(1);
        chrOrder.setUsername("chryl");
        chrOrder.setGoodsname(goodsInfo.getGoodsName());
        chrOrder.setOid(goodsInfo.getGoodsId());
        chrOrder.setNumber(1);
        chrOrder.setGoodsprice(new BigDecimal(goodsInfo.getGoodsPrice()));
        //下单
        orderDao.save(chrOrder);

        //减少库存
        goodsClient.reduceInventory(id, chrOrder.getNumber());


        //下单成功,消息发送到mq(投递消息):根据topic和消息内容发送
        rocketMQTemplate.convertAndSend("order-topic", chrOrder);

        return chrOrder;

    }

}
