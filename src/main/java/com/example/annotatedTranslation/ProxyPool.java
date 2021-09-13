package com.example.annotatedTranslation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;

/**
 * @author Yu Shaoqing
 * @date 2021/9/13/12:24
 */
public class ProxyPool {
    private static RequestConfig reqConfig3 = null;
    private static SocketConfig socketConfig3 = null;
    public String get_proxy() throws Exception {
        String get_url = "http://127.0.0.1:5010/get";
        reqConfig3 = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(10000) // 设置连接超时时间
                .setSocketTimeout(10000) // 设置读取超时时间
                .setCircularRedirectsAllowed(true) // 允许多次重定向
                .build();

        socketConfig3 = SocketConfig.custom()
                .setSoKeepAlive(false)
                .setSoLinger(1)
                .setSoReuseAddress(true)
                .setSoTimeout(10000)
                .setTcpNoDelay(true).build();
        try {
            HttpGet httpGet = new HttpGet(get_url);
            String res_json = new ZhimaAPI().doRequest(httpGet);
            JsonParser jsonParser = new JsonParser();
            JsonObject root = jsonParser.parse(res_json).getAsJsonObject();
            return  root.getAsJsonPrimitive("proxy").getAsString();

        }catch (Exception e){
            e.printStackTrace();
            return "bad response";
        }
    }
}
