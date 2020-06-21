package com.chryl.client.fallback;

import com.chryl.client.GoodsClient;
import com.chryl.po.ChrGoods;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * feign 容错类 , 实现feign的client接口,并实现其中的所有方法,如果远程调用出现问题,就会进入当前feign调用容错类的同名方法
 * ----虽然啊! 实现了容错类,但是出现容错之后,也没有异常信息,非常不方便,接下来我们实现容错信息的查看
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
@Component
public class GoodsClientFallback implements GoodsClient {

    @Override
    public ChrGoods getGoodsInfo(Integer id) {
        //容错逻辑
        ChrGoods chrGoods = new ChrGoods();
        chrGoods.setGoodsId(-100);
        chrGoods.setGoodsName("出错,进行容错");
        return chrGoods;
    }
}
