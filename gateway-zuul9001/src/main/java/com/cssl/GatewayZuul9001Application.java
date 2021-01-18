package com.cssl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class GatewayZuul9001Application {

    public static void main(String[] args) {
        SpringApplication.run(GatewayZuul9001Application.class, args);
    }

}
