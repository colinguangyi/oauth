package com.colin.oauth.config;

import com.colin.oauth.service.UserService;
import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Resource
    private UserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 配置基本用户信息及加密方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * 配置了一个表单登陆
     * 访问此服务时会需要先进行登陆
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
             //关闭打开的csrf保护
             csrf().disable().
             //配置一个表单登陆
             formLogin().
             //自定义登陆页面地址
             loginPage("/auth/login")
        ;
    }

}
