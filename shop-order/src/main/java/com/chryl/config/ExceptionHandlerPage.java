package com.chryl.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.cert.ocsp.RespData;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * sentinel自定义多种规则造成的降级 返回,来区分各种规则的降级
 * Created by Chr.yl on 2020/6/21.
 *
 * @author Chr.yl
 */
@Component
public class ExceptionHandlerPage implements UrlBlockHandler {

    @ExceptionHandler(value = BlockException.class)
    @Override
    public void blocked(HttpServletRequest request,
                        HttpServletResponse response, BlockException e) throws IOException {
        responseData responseData = new responseData();
        //BlockException 异常接口, 包含sentinel的五个规则的异常
        //FlowException 限流异常
        //DegradeException  降级异常
        //ParamFlowException    参数限流异常
        //AuthorityException    授权异常
        //SystemBlockException  系统负载异常
        if (e instanceof FlowException) {//限流的
            responseData = new responseData(-1, "限流异常");
        } else if (e instanceof DegradeException) {//降级
            responseData = new responseData(-2, "降级异常");
        } else if (e instanceof ParamFlowException) {
            responseData = new responseData(-3, "参数限流异常");
        } else if (e instanceof AuthorityException) {
            responseData = new responseData(-4, "授权异常");
        } else if (e instanceof SystemBlockException) {
            responseData = new responseData(-5, "系统负载异常");
        }
        response.setContentType("application/json;charset=utf-8");//防止中文乱码
        response.getWriter().write(JSON.toJSONString(responseData));//返回页面
        //这里返回页面,正常返回json就好
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class responseData {
    private int code;
    private String message;
}
