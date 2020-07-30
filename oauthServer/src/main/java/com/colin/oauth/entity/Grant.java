package com.colin.oauth.entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * 实现security封装的GrantedAuthority权限对象进行扩展，
 * 便于自定义用户权限操作
 */
public class Grant implements GrantedAuthority {

    //用户权限
    private String authorities;

    @Override
    public String getAuthority() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }
}
