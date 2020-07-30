package com.colin.oauth.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * HTTP请求默认超时时间
     */
    private static final int DEFAULT_TIME_OUT = 50000;

    /**
     * 进行表单提交
     * @param requestUrl 提交URL
     * @param params 参数
     */
    public static String doFormPost(String requestUrl, Map<String,String> params){
        String result = null;
        URL url = null;
        HttpURLConnection con = null;
        try{
            // 构建请求参数
            StringBuffer sb = new StringBuffer();
            if (params != null) {
                for (Map.Entry<String, String> e : params.entrySet()) {
                    sb.append(e.getKey());
                    sb.append("=");
                    sb.append(e.getValue());
                    sb.append("&");
                }
                sb.substring(0, sb.length() - 1);
            }

            // 发送请求
            url = new URL(requestUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(DEFAULT_TIME_OUT);
            con.setReadTimeout(DEFAULT_TIME_OUT);
            // 设置请求方式（GET/POST）
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();

            // 读取返回
            InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();

            result = buffer.toString();

        }catch (Exception e){
            logger.error("doFormPost_error:" + e.getMessage(), e);
        }finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }
}
