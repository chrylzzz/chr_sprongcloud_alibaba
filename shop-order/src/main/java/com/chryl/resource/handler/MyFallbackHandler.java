package com.chryl.resource.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * sentinel资源容错:
 * --@SentinelResource可以自定义 fallback 处理的类,
 * 不用再每个方法进行定义,采用引用外部blockExceptionClass的方式统一处理
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
@Slf4j
public class MyFallbackHandler {
    public String myFallbackMethod(Throwable t) {

        //自定义异常处理逻辑
        log.info("触发了 fallback :{}", t);
        return "触发了 fallback";
    }
}
