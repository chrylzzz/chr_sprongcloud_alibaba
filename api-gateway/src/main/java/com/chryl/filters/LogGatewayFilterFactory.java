package com.chryl.filters;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义局部过滤器工厂类: 跟自定义的断言十分相似
 * Created by Chr.yl on 2020/6/23.
 *
 * @author Chr.yl
 */
//@Component
public class LogGatewayFilterFactory extends AbstractGatewayFilterFactory<LogGatewayFilterFactory.Config> {

    //改造
    public LogGatewayFilterFactory() {
        super(LogGatewayFilterFactory.Config.class);
    }

    //过滤器逻辑
    @Override
    public GatewayFilter apply(LogGatewayFilterFactory.Config config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
                if (config.isCacheLog()) {
                    System.out.println("缓存日志开启...");
                }
                if (config.isConsoleLog()) {
                    System.out.println("控制台日志开启...");
                }
                return gatewayFilterChain.filter(serverWebExchange);
            }
        };
    }

    //读取配置文件的参数 ,赋值到config类中
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("consoleLog", "cacheLog");
    }

    //配置类,接收配置参数
    @Data//get set
    @NoArgsConstructor//无参
    public static class Config {
        private boolean consoleLog;
        private boolean cacheLog;

    }

}
