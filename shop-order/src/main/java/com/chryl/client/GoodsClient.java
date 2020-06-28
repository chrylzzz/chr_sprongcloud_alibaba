package com.chryl.client;

import com.chryl.client.fallback.GoodsClientFallback;
import com.chryl.client.fallback.GoodsClientFallbackFactory;
import com.chryl.po.ChrGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign 配置了fallback seata就 不认为服务发生错误
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@FeignClient(value = "service-goods"//value指定服务名称
//        ,//如果同时设置了feign的容错类和sentinel的资源容错类,资源的被sentinel控制,接口的被feign控制,各司其职
//        fallback = GoodsClientFallback.class//fallback为该类的容错类,但是无法得到feign调用的出错信息
//        ,
//        fallbackFactory = GoodsClientFallbackFactory.class//feign调用的容错类,可以得到出错信息,但是用了这个,就不要用fallback了
)
public interface GoodsClient {

    //@FeignClient的value + @PostMapping("/goods/get/{id}")  就是一个完整的路径
    @PostMapping("/goods/get/{id}")
    ChrGoods getGoodsInfo(@PathVariable("id") Integer id);


    @PostMapping("/goods/reduce")
    void reduceInventory(@RequestParam("goodsId") Integer goodsId,
                         @RequestParam("number") Integer number);
}
