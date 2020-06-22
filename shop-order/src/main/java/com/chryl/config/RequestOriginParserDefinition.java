package com.chryl.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权规则的配置:
 * 如何区分来源,区分什么来源可以访问服务,这里根据服务名称来区分
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
//@Component
//public class RequestOriginParserDefinition implements RequestOriginParser {
//
//    //通过req域获取来源表示,交给流控应用进行匹配
//    @Override
//    public String parseOrigin(HttpServletRequest httpServletRequest) {
//        String serviceName = httpServletRequest.getParameter("serviceName");
//        if (StringUtils.isEmpty(serviceName)) {
//            //不允许授权访问
//            throw new RuntimeException("service name is empty");
//        }
//        //允许授权
//        return serviceName;
//    }
//}
