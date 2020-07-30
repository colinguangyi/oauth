package com.colin.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer//oauth认证服务
@MapperScan("com.colin.mapper")//指定mybatis的mapper路径，此次几乎无用
public class OauthServer {
    public static void main(String[] args) {
        SpringApplication.run(OauthServer.class, args);
    }
}
