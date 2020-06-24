package com.chryl.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 两种网关限流方式:
 * route维度和自定义API分组维度
 * gateway-sentinel:配置网关限流,基于route(路由维度)的方式也很灵活,只不过有点繁琐,但是自定义API维度更加灵活(细粒度)
 * 注意:
 * 两种网关限流方式,只要把限流参数更换,就可以实现两种限流方式的切换
 * Created by Chr.yl on 2020/6/23.
 * 网关先
 *
 * @author Chr.yl
 */
@Slf4j
@Configuration
public class GatewayConfiguration {
    //---------------------------------------route维度-------------------------------------------------------------
    private final List<ViewResolver> viewResolvers;

    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    //初始化限流过滤器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    //配置初始化的限流参数 ,就是限流的sentinel配置
//    @PostConstruct
//    public void initGatewayRules() {
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        //配置需要限流的微服务
//        rules.add(
//                new GatewayFlowRule("order_route")//资源名称,对应 配置文件的路由的id属性
//                        .setCount(1)//限流阈值
//                        .setIntervalSec(4)//统计时间窗口,单位秒,默认是1秒
//        );
//        GatewayRuleManager.loadRules(rules);
//    }

    //配置限流的异常处理器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    //自定义限流异常页面
    @PostConstruct
    public void initBlockHandlers() {
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                String path = serverWebExchange.getRequest().getURI().getPath();//被限流的接口
                Map map = new HashMap();
                map.put("code", 0);
                map.put("message", "接口被限流");
                map.put("path", path);
                log.info("接口被限流了...");
                return ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)//json
                        .body(BodyInserters.fromObject(map));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);

    }

    //------------------------------------------自定义API分组维度:粒度更细--------------------------------------------

    //配置初始化的限流参数 ,就是限流的sentinel配置
    @PostConstruct
    public void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        //配置需要限流的微服务
        rules.add(
                new GatewayFlowRule("odd_api1")//这里的资源为ApiDefinition定义的资源名字
                        .setCount(1)//阈值
                        .setIntervalSec(5)//时间窗口
        );
        rules.add(
                new GatewayFlowRule("odd_api2")
                        .setCount(1)
                        .setIntervalSec(5)
        );
        GatewayRuleManager.loadRules(rules);
    }

    //自定义API分组
    @PostConstruct
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();
        ApiDefinition api1 = new ApiDefinition("odd_api1")//定义分组
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{//设置规则
                    //以 /servic-order/odd/api1 开头的请求
                    add(new ApiPathPredicateItem()
                            .setPattern("/servic-order/odd/api1/**")//哪个地址,这里的地址是配置文件的url的地址,注意这里的地址,需要加上predicates里的路由断言
                            .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX)//路由策略,就是以**前面的为开头
                    );
                    //add(); //可以设置多个规则
                }});
        ApiDefinition api2 = new ApiDefinition("odd_api2")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    //以 /servic-order/odd/api2/demo 完全匹配的路径
                    add(new ApiPathPredicateItem()
                            .setPattern("/servic-order/odd/api2/demo")////哪个地址,这里的地址是配置文件的url的地址,注意这里的地址,需要加上predicates里的路由断言
                    );
                }});
        definitions.add(api1);
        definitions.add(api2);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }

}
