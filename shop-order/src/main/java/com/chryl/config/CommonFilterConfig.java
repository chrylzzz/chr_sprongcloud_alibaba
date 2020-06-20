package com.chryl.config;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.6.3版本的sentinel默认关闭所有的url入口,也就是关闭了链路规则,
 * 因此链路限流不生效,但是1.7.0开始,可以进行手动的关闭该限制,
 * 开放链路限流,这里进行手动开启
 * springcloud alibaba 2.1.1版本之后,可以直接配置打开链路限流,更为简单,
 * spring.cloud.sentinel.web-context-unifyfalse即可关闭
 * 但是习时springcloud alibaba 2.1.1还未发布,所以只能手动关闭
 * Created by Chr.yl on 2020/6/20.
 *
 * @author Chr.yl
 */
//@Configuration
public class CommonFilterConfig {
    @Bean
    public FilterRegistrationBean sentinelFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new CommonFilter());
        registrationBean.addUrlPatterns("/*");
        //入口资源关闭聚合
        registrationBean.addInitParameter(CommonFilter.WEB_CONTEXT_UNIFY, "false");
        registrationBean.setName("sentinelFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
