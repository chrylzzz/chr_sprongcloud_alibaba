package com.chryl.controller;

import com.chryl.po.ChrGoods;
import com.chryl.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 测试限流,降级,资源控制
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@RequestMapping("/test")
@RestController
public class OrderController2 {

    @Autowired
    private OrderService orderService;

    @GetMapping("/get/{id}")
    public ChrGoods getGoods(@PathVariable Integer id) {
        return orderService.get(id);
    }

    //测试资源
    @GetMapping("/get2/{id}")
    public ChrGoods getGoods2(@PathVariable Integer id) {
        //测试资源
//        return orderService.get3(id);
        //测试资源2
        return orderService.get4(String.valueOf(id));
    }

    //热点流控
    @GetMapping("/get3")
    public ChrGoods getGoods3(String name) {
        return orderService.get3(2);
    }

    //测试自定义断言
    @GetMapping("/get4")
    public String getAge(@RequestParam("age") String age) {
        return "success";
    }
}
