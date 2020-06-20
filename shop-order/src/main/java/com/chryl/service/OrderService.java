package com.chryl.service;

import com.chryl.dao.OrderDao;
import com.chryl.po.ChrOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public void createOrder(ChrOrder chrOrder) {
        orderDao.save(chrOrder);

    }
}
