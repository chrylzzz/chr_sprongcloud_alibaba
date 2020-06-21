package com.chryl.client.fallback;

import com.chryl.client.GoodsClient;
import com.chryl.po.ChrGoods;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 这就是容错类而且还可以查看异常信息的容错类,你说厉害吧
 * 实现方法:FallbackFactory<T>
 * 需要实现FallbackFactory,泛型为那个类的容错类,就是泛型
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
@Component
@Slf4j
public class GoodsClientFallbackFactory implements FallbackFactory<GoodsClient> {
    //throwable参数就是feign调用过程中出错的信息
    @Override
    public GoodsClient create(Throwable throwable) {
        //容错逻辑
        return new GoodsClient() {
            @Override
            public ChrGoods getGoodsInfo(Integer id) {
                //打印异常信息
                log.info("异常信息为: {}", throwable);
                //容错逻辑
                ChrGoods chrGoods = new ChrGoods();
                chrGoods.setGoodsId(-100);
                chrGoods.setGoodsName("出错,进行容错");
                return chrGoods;
            }
        };
    }
}
