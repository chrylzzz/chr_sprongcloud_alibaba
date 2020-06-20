package com.chryl.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Service
public class OrderService2 {

    //定义资源,value指定资源名字
    @SentinelResource("message")
    public String message() {
        return "message";
    }
}
