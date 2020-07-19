package com.chryl.filters.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * gateway自定义全局过滤器: 必须实现 GlobalFilter,Ordered 两个接口
 * 作用: 统一鉴权过滤器
 * Created by Chr.yl on 2020/6/23.
 *
 * @author Chr.yl
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    //白名单
    public static String[] skipAuthUrls = {};

    /**
     * 统一鉴权逻辑
     * 根据客户端发送的请求 ,是否携带合法的token,有放行寻找微服务,没有则返回错误响应码
     * 这里可以调用user微服务,根据客户端传来的token进行验证,
     * 通过则放行,不通过则返回错误码
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();//访问的url
        //跳过不需要验证的路径
        if (Arrays.asList(skipAuthUrls).contains(url)) {
            return chain.filter(exchange);
        }
        //从请求头中取出token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
//        String token = request.getQueryParams().getFirst("token");//从参数中获得token
        if (StringUtils.isBlank(token) ||
                !StringUtils.equals("admin", token)) {//测试认证失败
            log.info("认证失败...");
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//            response.getHeaders().add("Content-Type", String.valueOf(MediaType.APPLICATION_JSON_UTF8));

            //组织响应json
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", "401");
            jsonObject.put("message", "没有权限");
            byte[] bytes = JSON.toJSONString(jsonObject).getBytes(StandardCharsets.UTF_8);

            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
        }
        return chain.filter(exchange);
    }

    //表示当前过滤器的优先级 ,越小 越高
    @Override
    public int getOrder() {
        return -100;
    }


    /**
     * 判断token是否在黑名单内
     *
     * @param token
     * @return
     */
//    private boolean isBlackToken(String token) {
//        assert token != null;
//        return stringRedisTemplate.hasKey(String.format(jwtBlacklistKeyFormat, token));
//    }
}
