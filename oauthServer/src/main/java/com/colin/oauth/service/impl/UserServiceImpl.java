package com.colin.oauth.service.impl;

import com.colin.oauth.entity.Grant;
import com.colin.oauth.entity.SysUser;
import com.colin.oauth.enums.GrantEnum;
import com.colin.oauth.service.UserService;
import com.colin.oauth.util.SysSecurityUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 为方便校验，此处写死待校验用户
     * 实际项目中需要根据自身业务处理
     */
    private static HashSet<String> USERS = new HashSet<>();
    static {
        USERS.add("colin1");
        USERS.add("colin2");
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        //可从其他服务（如loginServer底层服务）获取用户信息进行校验
        //也可以从本地数据库读取进行验证
        if(!USERS.contains(s)){
            throw new UsernameNotFoundException("用户不存在");
        }
        SysUser user = new SysUser();
        user.setUsername(s);

        //此处SysSecurityUtil定义了一套前后一致的加密规则，省去密码再次读取
        //也可直接从第三方获取密码信息，再使用passwordEncoder进行编码
        user.setPassword(passwordEncoder.encode(SysSecurityUtil.generatePwd(s)));
        Grant grant = new Grant();
        grant.setAuthorities(GrantEnum.USER.name());

        List<Grant> list = new ArrayList<>();
        list.add(grant);
        user.setGrants(list);

        return user;
    }
}
