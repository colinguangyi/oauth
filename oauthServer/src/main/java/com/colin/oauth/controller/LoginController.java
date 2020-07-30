package com.colin.oauth.controller;

import com.colin.oauth.util.SysSecurityUtil;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * 路由到第三方页面
     */
    @GetMapping("/auth/login")
    public void loginPage(Model model, HttpServletResponse response){
        try {
            response.sendRedirect("http://192.168.1.227:1112/login/page");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三方登陆成功回调此接口
     */
    @GetMapping("/oauthServer/login")
    public String oauthLogin(String u,Model model){
        model.addAttribute("username", u);
        //此处为了方便，直接加密生成密码，与UserService中的验证相呼应
        //后续可以考虑不再设置
        model.addAttribute("pwd", SysSecurityUtil.generatePwd(u));
        return "login";
    }

}
