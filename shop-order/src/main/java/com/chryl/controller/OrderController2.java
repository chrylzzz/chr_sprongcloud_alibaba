package com.chryl.controller;

import com.chryl.client.GoodsClient;
import com.chryl.po.ChrGoods;
import com.chryl.po.ChrOrder;
import com.chryl.service.OrderService;
import com.chryl.service.OrderService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * 测试并发,模拟高并发
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@RequestMapping("/test")
@RestController
public class OrderController2 {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsClient goodsClient;

    //注入资源
    @Autowired
    private OrderService2 orderService2;


    @PostMapping("/goods/{goodsid}")
    public ChrOrder order(@PathVariable Integer goodsid) {
        ChrGoods chrGoods = goodsClient.getGoodsInfo(goodsid);

        /**
         * 模拟 商品调用需要2s的时间
         */
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChrOrder chrOrder = new ChrOrder();
        chrOrder.setUid(1);
        chrOrder.setUsername("admin");

        chrOrder.setGoodsid(chrGoods.getGoodsId());
        chrOrder.setGoodsprice(new BigDecimal(chrGoods.getGoodsPrice()));
        chrOrder.setNumber(1);
        chrOrder.setGoodsname(chrGoods.getGoodsName());


        //防止垃圾数据
        //orderService.createOrder(chrOrder);

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chrOrder;
    }

    //测试高并发
    @GetMapping("/message1")
    public String message() {
        orderService2.message();
        return "测试高并发11";
    }

    @GetMapping("/message2")
    public String message2() {
        orderService2.message();
        return "测试高并发2222222";
    }
}
