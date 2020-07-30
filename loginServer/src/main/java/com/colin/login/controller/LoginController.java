package com.colin.login.controller;

import java.util.HashSet;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    /**
     * 与oauthServer中用户校验一致，此处写死待校验用户
     * 实际项目中需要根据自身业务处理
     */
    private static HashSet<String> USERS = new HashSet<>();
    static {
        USERS.add("colin1");
        USERS.add("colin2");
    }

    /**
     * 跳转登陆页面
     */
    @GetMapping("/login/page")
    public String loginPage(Model model){
        return "login";
    }

    /**
     * 模拟登陆
     */
    @PostMapping("/login")
    public String toLogin(Model model,String username, String password, HttpServletResponse response){
        try {
            //模拟登陆逻辑
            if(!USERS.contains(username)){
                model.addAttribute("error", "用户不存在");
                return "login";
            }
            if(!"1".equals(password)){
                model.addAttribute("error", "用户名密码错误");
                return "login";
            }
            //登陆成功，返回oauthServer并携带登陆成功后的用户信息
            response.sendRedirect("http://192.168.1.227:1111/oauthServer/login?u="+username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "login";
    }

}
