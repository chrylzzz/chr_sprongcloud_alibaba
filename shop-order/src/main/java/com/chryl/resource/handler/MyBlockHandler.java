package com.chryl.resource.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * 可以自定义BlockException处理的类,不用再@SentinelResource处单独定义方法,采用引用外部blockExceptionClass的方式统一处理
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
@Slf4j
public class MyBlockHandler {

    //注意必须是static
    public static String myBlockHandlerMethod(BlockException e) {

        //自定义异常处理逻辑
        log.info("触发了 myBlockHandlerMethod :{}", e);
        return "触发了 myBlockHandlerMethod";
    }

}
