package com.chryl.controller;

import com.chryl.po.ChrGoods;
import com.chryl.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //扣库存
    @PostMapping("/reduce")
    public void reduceInventory(@RequestParam("goodsId") Integer goodsId,
                                @RequestParam("number") Integer number) {
        goodsService.reduceInventory(goodsId, number);
    }
}
