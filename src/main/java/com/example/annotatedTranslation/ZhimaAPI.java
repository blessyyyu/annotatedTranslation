package com.example.annotatedTranslation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yu Shaoqing
 * @date 2021/9/1/17:51
 */
public class ZhimaAPI {
    static private Map<String,String> GetIpParams = new HashMap<>();
    private static RequestConfig reqConfig = null;
    String targetUrl = "http://webapi.http.zhimacangku.com/getip";

    public String getZhimaIp() {
        reqConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(10000) // 设置连接超时时间
                .setSocketTimeout(10000) // 设置读取超时时间
                .setCircularRedirectsAllowed(true) // 允许多次重定向
                .build();
        setIpParams();
        try {
            String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramsAdapter(GetIpParams), "UTF-8"));
            HttpGet httpGet = new HttpGet(targetUrl + "?" + paramStr);
            String result = doRequest(httpGet);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return "bad response";
        }
    }


    public void setIpParams() {
//      num: 提取数量
        GetIpParams.put("num", "20");
//      type: 1 TXT, 2: json 3: html
        GetIpParams.put("type", "2");
//        city 0 默认全国
        GetIpParams.put("city", "0");
//        yys： 运营商 0 不限
        GetIpParams.put("yys", "0");
//        port: 端口号
        GetIpParams.put("port", "1");
//        pack: 套餐
        GetIpParams.put("pack", "176722");
//        ts: 是否现实ip过期时间， 1： 显示， 2： 不显示
        GetIpParams.put("ts", "1");
//        ys: 是否显示运营商
        GetIpParams.put("ys", "0");
//        cs: 是否显示位置
        GetIpParams.put("cs", "1");
//        lb: 分隔符
        GetIpParams.put("lb", "1");
//        sb: 自定义分隔符
        GetIpParams.put("sb", "0");
//        pb: 端口位数
        GetIpParams.put("pb", "4");
//        mr: 去重选择 1： 360天去重
        GetIpParams.put("mr", "1");



    }


    /**
     * 参数适配器，将系统定义的请求参数转换成HttpClient能够接受的参数类型
     *
     * @param map
     *            系统定义的请求参数
     * @return HttpClient要求的参数类型
     */
    private List<NameValuePair> paramsAdapter(Map<String, String> map) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return nvps;
    }


    /**
     * 设置请求头
     *
     * @param httpReq
     */
    private void setHeaders(HttpRequestBase httpReq) {
        httpReq.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
        httpReq.addHeader("xxx", "xxx");
    }

    /**
     * 执行请求
     *
     * @param httpReq
     * @return
     */
    public String doRequest(HttpRequestBase httpReq) {
        String result = new String();
        httpReq.setConfig(reqConfig);
        try {
            // 设置请求头
            setHeaders(httpReq);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 执行请求
            CloseableHttpResponse httpResp = httpClient.execute(httpReq);

            // 保存Cookie

            // 获取http code
            int statusCode = httpResp.getStatusLine().getStatusCode();
            System.out.println(statusCode);

            HttpEntity entity = httpResp.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }

            httpResp.close();
            httpClient.close();
            httpReq.abort();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public Map<String, Integer> parseZhimaApi(String jsonData){
        Map<String, Integer> Ip_port = new HashMap<>();
        try{
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(jsonData).getAsJsonObject();
            JsonArray Ips = root.getAsJsonArray("data");
            for(JsonElement ip : Ips){
                JsonObject curIp_port = ip.getAsJsonObject();
                String cur_ip = curIp_port.getAsJsonPrimitive("ip").getAsString();
                int port = curIp_port.getAsJsonPrimitive("port").getAsInt();
                Ip_port.put(cur_ip,port);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return Ip_port;
    }
}
