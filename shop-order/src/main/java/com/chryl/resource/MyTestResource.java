package com.chryl.resource;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * --@SentinelResource 注解的解析:这个注解是对sentinel资源的流控,这与对服务的流控不同
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
@Slf4j
@Component
public class MyTestResource {

    /**
     * 注意定义这些方法的时候:参数和返回值一定要一样,但是最后一个参数可以是 BlockException Throwable 的参数
     * blockHandler :当资源内部发生了 BlockException 异常之后,要走的逻辑(就是sentinel定义的五个异常之后要走的按需求自定义逻辑),---只处理sentinel的5个路由异常
     * fallback     :当资源内部发生了 Throwable 应该进入的方法---处理不是sentinel的异常
     * blockHandlerClass    :指定外部的BlockException异常时执行的逻辑
     * fallbackClass    :指定外部的非BlockException异常时执行的逻辑
     */
    //定义资源,value指定资源名字,注意定义资源,在sentinel-dashboard进行配置
    @SentinelResource(
            value = "message",//资源名,既在sentinel中的标识
            /**
             * 有问题,通过测试,blockHandlerClass和fallbackClass这是引入了外部类来定义容错,而如果不写blockHandler和fallback指定方法则会找不到方法,
             * 所以需要都写上,虽然看视频是写了外部的Class就不要写本类定义的了,但是测试并不是这样的,
             * 应该是指定哪个外部类的哪个具体的fallback或blockHandler的方法
            */
            //本类直接定义
            blockHandler = "myBlockHandlerMethod",//自定义一个blockHandler方法,处理BlockException,注意,参数和返回值必须和定义所在的方法一样
            fallback = "myFallbackMethod"//自定义处理Throwable异常
            //定义外部类统一处理
//            blockHandlerClass = MyBlockHandler.class,//定义外部处理BlockException的类进行统一处理,不用挨个写blockHandler处理
//            fallbackClass = MyFallbackHandler.class//定义外部的处理Throwable的类统一处理,不用挨个写fallback处理
    )
    public String message() {
        return "message";
    }

    /**
     * blockHandler方法定义规则:
     * 这就是定义的blockHandler//注意返回值和参数与原方法一致
     * 但是允许在参数最后加入一个 BlockException 参数 ,用来接收方法发生的异常
     *
     * @return
     */
    public String myBlockHandlerMethod(BlockException e) {

        //自定义异常处理逻辑
        log.info("触发了 myBlockHandlerMethod :{}", e);
        return "触发了 myBlockHandlerMethod";
    }


    /**
     * fallback方法定义规则:
     * 这就是定义的fallback//注意返回值和参数与原方法一致
     * 但是允许在参数最后加入一个 Throwable 参数 ,用来接收方法发生的异常
     *
     * @return
     */
    public String myFallbackMethod(Throwable t) {

        //自定义异常处理逻辑
        log.info("触发了 fallback :{}", t);
        return "触发了 fallback";
    }

//------------
    //@SentinelResource:blockHandler和fallback连个属性就相当于下面的tc逻辑
//        try {
//
//    } catch (BlockException e) {
//        e.printStackTrace();
//    }catch (Throwable t) {
//        e.printStackTrace();
//    }
}
