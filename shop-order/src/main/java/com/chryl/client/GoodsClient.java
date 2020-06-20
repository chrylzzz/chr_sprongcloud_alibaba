package com.chryl.client;

import com.chryl.po.ChrGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@FeignClient(value = "service-goods")//value指定服务名称
public interface GoodsClient {

    //@FeignClient的value + @PostMapping("/goods/get/{id}")  就是一个完整的路径
    @PostMapping("/goods/get/{id}")
    ChrGoods getGoodsInfo(@PathVariable("id") Integer id);
}
