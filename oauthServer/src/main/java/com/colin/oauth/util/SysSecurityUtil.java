package com.colin.oauth.util;

import org.springframework.util.DigestUtils;

public class SysSecurityUtil {

    public static String generatePwd(String username) {
        try{
            String sec = username + "_colin0702";
            return DigestUtils.md5DigestAsHex(sec.getBytes());
        }catch (Exception e){
            LogUtil.utilLog().error("generatePwd_error:", e);
        }
        return "errorPwd";
    }
}
