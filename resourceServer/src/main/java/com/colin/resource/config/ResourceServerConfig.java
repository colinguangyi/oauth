package com.colin.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    /**
     * 配置了 access_token 的校验地址、
     * client_id、
     * client_secret
     * 这三个信息，
     * 当用户来资源服务器请求资源时，会携带上一个 access_token，
     * 通过这里的配置，就能够校验出 token 是否正确等
     */
    @Bean
    public RemoteTokenServices tokenServices(){
        RemoteTokenServices services = new RemoteTokenServices();
        services.setCheckTokenEndpointUrl("http://192.168.1.227:1111/oauth/check_token");
        //经测试，此处传递的client信息只要在oauth_client_details中存在即可
        //后续可设计为本系统主账号信息做统一校验
        services.setClientId("colin0702");
        services.setClientSecret("1");
        return services;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res1").tokenServices(tokenServices());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //可直接访问的地址
                .antMatchers("/access/**").permitAll()
                //需要admin权限访问的地址
                .antMatchers("/admin/**").hasRole("admin")
                //其他访问都需要验证
                .anyRequest().authenticated()
                .and()
                //使支持跨域
                .cors()
        ;
    }
}
