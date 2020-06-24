package com.chryl.predicates;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义断言工厂 : 用来限制 age 18-60 才能访问  , 自己书写时,参照人家的格式即可 如:BeforeRoutePredicateFactory
 * 注意:
 * 1.自定义的断言构造器 Xxx + RoutePredicateFactory 名字必须是这个格式 , Xxx为配置文件定义的名字 ,否则不与识别
 * 2.必须继承extends AbstractRoutePredicateFactory<T>   泛型 就是配置类的Config类(该类里的Config类为静态)
 * Created by Chr.yl on 2020/6/23.
 *
 * @author Chr.yl
 */
//@Component
public class AgeRoutePredicateFactory extends AbstractRoutePredicateFactory<AgeRoutePredicateFactory.Config> {

    //构造函数
    public AgeRoutePredicateFactory() {
        super(AgeRoutePredicateFactory.Config.class);
    }

    //设置配置类与配置文件的关系:读取配置文件中的参数值,并赋值到配置类中的属性上
    public List<String> shortcutFieldOrder() {
        //这里的参数要与配置文件的参数顺序保持一致
        return Arrays.asList("minAge", "maxAge");
    }

    // 断言逻辑
    public Predicate<ServerWebExchange> apply(AgeRoutePredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                //获取参数,例如 request,session
                String ageStr = serverWebExchange.getRequest().getQueryParams().getFirst("age");
                //判断是不是null
                if (StringUtils.isNoneBlank(ageStr)) {
                    Integer age = Integer.valueOf(ageStr);
                    if (age < config.getMaxAge() && age > config.getMinAge()) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        };
    }

    //配置类 ,用于接收配置文件中的对应参数
    @Data//get set
    @NoArgsConstructor//无参构造
    public static class Config {
        private int minAge;//18
        private int maxAge;//60
    }
}
