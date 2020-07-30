package com.colin.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HellowController {

    /**
     * 受限制访问，用户权限不限
     */
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * 受限制访问，只允许admin权限用户访问
     */
    @GetMapping("/admin/hello")
    public String adminHello(){
        return "adminHello";
    }

    /**
     * 不受限制
     */
    @GetMapping("/access/hello")
    public String accessHello(){
        return "accessHello";
    }

}
