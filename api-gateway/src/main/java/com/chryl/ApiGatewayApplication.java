package com.chryl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.time.LocalDateTime;

/**
 * Created by Chr.yl on 2020/6/22.
 *
 * @author Chr.yl
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
