package com.chryl.controller;

import com.alibaba.fastjson.JSON;
import com.chryl.po.ChrGoods;
import com.chryl.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //查询商品
    @PostMapping("/get/{id}")
    public ChrGoods getGoodsInfo(@PathVariable("id") Integer id) {
        ChrGoods chrGoods = goodsService.findById(id);
        log.info("run~");
        return chrGoods;
    }
}
