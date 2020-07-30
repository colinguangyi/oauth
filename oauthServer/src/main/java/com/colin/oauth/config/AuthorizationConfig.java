package com.colin.oauth.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter{
    @Resource
    private DataSource dataSource;

    /**
     * 指定一种加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 生成的 Token 要往哪里存储，
     * 我们可以存在 Redis中，也可以存在内存中，
     * 也可以结合 JWT 等等，
     *
     * 最终使用jdbc存储，数据可读性更强些
     * 存储表：oauth_access_token,oauth_refresh_token
     */
    @Bean
    public TokenStore tokenStore(){
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 配置授权码的存储
     * 由于一次性，暂放在当前内存中
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new InMemoryAuthorizationCodeServices();
    }

    /**
     *  授权信息保存策略
     *  当前保存至JDBC
     *  存储表：oauth_approvals
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 覆盖默认ClientDetailsService的实现
     * 将客户端校验信息存入数据库
     * 存储表：oauth_client_details
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(){
        JdbcClientDetailsService detailsService = new JdbcClientDetailsService(dataSource);
        detailsService.setPasswordEncoder(passwordEncoder());
        return detailsService;
    }

    /**
     * 配置令牌的存储与认证
     */
    @Bean
    public AuthorizationServerTokenServices authorizationServerTokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(jdbcClientDetailsService());
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore());
        //设置access_token失效时间
        services.setAccessTokenValiditySeconds(60*60*2);
        //设置refresh_token失效时间
        services.setRefreshTokenValiditySeconds(60*60*3);
        return services;
    }

    /**
     * 用来配置令牌端点的安全约束，也就是这个端点谁能访问，谁不能访问
     * checkTokenAccess 是指一个 Token 校验的端点，
     * 这个端点我们设置为可以直接访问
     * 在后面，当资源服务器收到 Token 之后，需要去校验Token的合法性，就会访问这个端点
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 用来配置客户端的详细信息
     * 配置校验客户端
     * 这里我们分别配置了客户端的 id，secret、资源 id、授权类型、授权范围以及重定向 uri
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //改为jdbc模式
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 配置令牌的访问端点和令牌服务
     * OAuth2的主配置信息
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .approvalStore(approvalStore())
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(authorizationServerTokenServices());
        //自定义授权页面配置
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
    }

}
