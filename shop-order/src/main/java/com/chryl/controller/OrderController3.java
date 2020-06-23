package com.chryl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试网关限流
 * Created by Chr.yl on 2020/6/23.
 *
 * @author Chr.yl
 */
@RestController
@RequestMapping("/odd")
public class OrderController3 {

    //限流
    @GetMapping("/api1/aaa")
    public boolean show() {
        return true;
    }

    //限流
    @GetMapping("/api1/bbb")
    public boolean show2() {
        return true;
    }

    //限流
    @GetMapping("/api2/demo")
    public boolean show3() {
        return true;
    }

    //不限流
    @GetMapping("/api2/aaa")
    public boolean show4() {
        return true;
    }

}
