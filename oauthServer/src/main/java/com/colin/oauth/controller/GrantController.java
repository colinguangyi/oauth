package com.colin.oauth.controller;

import java.util.Map;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * 由oauth封装时将认证信息放置在session中，此处需要@SessionAttributes
 * 以便获取认证信息
 */
@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {

    @RequestMapping("/custom/confirm_access")
    public String grantPage(Map<String, Object> model, Model viewModel){
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        viewModel.addAttribute("clientId", authorizationRequest.getClientId());
        return "grant";
    }
}
