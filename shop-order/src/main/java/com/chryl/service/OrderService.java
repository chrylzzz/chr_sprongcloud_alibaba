package com.chryl.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.chryl.client.GoodsClient;
import com.chryl.resource.handler.MyBlockHandler;
import com.chryl.resource.handler.MyFallbackHandler;
import com.chryl.dao.OrderDao;
import com.chryl.po.ChrGoods;
import com.chryl.po.ChrOrder;
import com.chryl.resource.MyTestResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    MyTestResource myTestResource;

    //创建订单
    public void createOrder(ChrOrder chrOrder) {
        orderDao.save(chrOrder);
    }


    //无任何资源控制
    public ChrGoods get(Integer id) {
        ChrGoods chrGoods = goodsClient.getGoodsInfo(id);
        //这里feign发生容错
        if (chrGoods.getGoodsId() == -100) {//发生容错了,直接进行容错处理
            //feign容错逻辑

        }
        return chrGoods;
    }


    public ChrGoods get2(Integer id) {
        //资源控制
        myTestResource.message();
        ChrGoods goodsInfo = goodsClient.getGoodsInfo(id);
        return goodsInfo;
    }


    //定义外部的资源容错处理,这里进行外部类的方法指定容错
    @SentinelResource(value = "message3"
            , blockHandler = "show",
            fallback = "show",
            blockHandlerClass = MyBlockHandler.class,//定义外部处理BlockException的类进行统一处理,不用挨个写blockHandler处理
            fallbackClass = MyFallbackHandler.class//定义外部的处理Throwable的类统一处理,不用挨个写fallback处理
    )
    public ChrGoods get3(Integer id) {
        ChrGoods goodsInfo = goodsClient.getGoodsInfo(id);
//        if (1 == 1) {
//
//            throw new RuntimeException("ZZZZ");
//        }
        return goodsInfo;
    }


    //定义外部的资源容错处理,这里进行外部类的方法指定容错
    @SentinelResource(value = "message4"
            , blockHandler = "show2",
            fallback = "show2",
            blockHandlerClass = MyBlockHandler.class,//定义外部处理BlockException的类进行统一处理,不用挨个写blockHandler处理
            fallbackClass = MyFallbackHandler.class//定义外部的处理Throwable的类统一处理,不用挨个写fallback处理
    )
    public ChrGoods get4(String id) {
        ChrGoods goodsInfo = goodsClient.getGoodsInfo(Integer.valueOf(id));
        if (Integer.valueOf(id) == 1) {
            throw new RuntimeException("ZZZZ");
        }
        return goodsInfo;
    }
}
