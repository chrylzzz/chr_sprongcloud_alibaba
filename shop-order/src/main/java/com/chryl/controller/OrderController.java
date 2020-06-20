package com.chryl.controller;

import com.alibaba.fastjson.JSONObject;
import com.chryl.client.GoodsClient;
import com.chryl.service.OrderService;
import com.chryl.po.ChrGoods;
import com.chryl.po.ChrOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private OrderService orderService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private GoodsClient goodsClient;

    //下单,保存订单
    @PostMapping("/goods/{goodsid}")
    public ChrOrder order(@PathVariable Integer goodsid) {

        /**
         * restTemplate原始调用
         */
//        ChrGoods chrGoods = restTemplate.postForObject("http://localhost:8081/goods/get/" + goodsid, null, ChrGoods.class);

        //nacos根据服务名字获取服务的信息,自定义负载均衡
        /**
         * 自定义负载均衡
         */
//        List<ServiceInstance> instances = discoveryClient.getInstances("service-goods");
//        int i = new Random().nextInt(instances.size());//参数是x,就是在x之内的数字随机选择
//        ServiceInstance serviceInstance = instances.get(i);
//        ChrGoods chrGoods =
//                restTemplate.postForObject(
//                        "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/goods/get/" + goodsid,
//                        null, ChrGoods.class);

        /**
         * ribbon 负载均衡调用,直接写 服务名称即可
         */
//        ChrGoods chrGoods =
//                restTemplate.postForObject(
//                        "http://service-goods/goods/get/" + goodsid,
//                        null, ChrGoods.class);


        /**
         * 使用feign 调用远程服务
         */
        ChrGoods chrGoods = goodsClient.getGoodsInfo(goodsid);

        ChrOrder chrOrder = new ChrOrder();
        chrOrder.setUid(1);
        chrOrder.setUsername("admin");

        chrOrder.setGoodsid(chrGoods.getGoodsId());
        chrOrder.setGoodsprice(new BigDecimal(chrGoods.getGoodsPrice()));
        chrOrder.setNumber(1);
        chrOrder.setGoodsname(chrGoods.getGoodsName());


        orderService.createOrder(chrOrder);
        return chrOrder;
    }

}
