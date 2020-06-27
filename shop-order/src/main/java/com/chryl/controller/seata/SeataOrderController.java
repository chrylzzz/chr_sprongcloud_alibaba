package com.chryl.controller.seata;

import com.chryl.po.ChrOrder;
import com.chryl.service.seata.SeataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * seata 分布式事务测试接口
 * Created by Chr.yl on 2020/6/27.
 *
 * @author Chr.yl
 */
@RestController
@RequestMapping("/seata")
public class SeataOrderController {

    @Autowired
    private SeataService seataService;

    @GetMapping("/get/{id}")
    public ChrOrder create(@PathVariable("id") Integer id) {
        return seataService.createSeataOrder(id);
    }

}
