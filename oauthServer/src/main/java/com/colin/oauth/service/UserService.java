package com.colin.oauth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 继承security中的service接口
 * 用来自定义当前系统用户信息获取
 */
public interface UserService extends UserDetailsService{
}
