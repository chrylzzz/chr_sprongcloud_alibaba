package com.chryl.service;

import com.chryl.dao.GoodsDao;
import com.chryl.po.ChrGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    public ChrGoods findById(Integer id) {
        return goodsDao.findById(id).get();
    }


    @Transactional
    public void reduceInventory(Integer goodsId, Integer number) {
        ChrGoods chrGoods = goodsDao.findById(goodsId).get();
        if (chrGoods == null) {
            //没有该商品
        }
        //扣库存
        chrGoods.setGoodsStock(chrGoods.getGoodsStock() - number);

        //模拟异常
        int i = 1 / 0;

        goodsDao.save(chrGoods);
    }
}
