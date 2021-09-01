package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author Yu Shaoqing
 * @date 2021/9/1/21:30
 */
public class ProxyTest {
    String url = "111.76.187.139";
    int port = 41986;
    @Test
    void test(){
        String url = "http://icanhazip.com/"; // 访问链接

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(getFactory());
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
    }

    public SimpleClientHttpRequestFactory getFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //单位为ms
        factory.setReadTimeout(10 * 1000);
        //单位为ms
        factory.setConnectTimeout(30 * 1000);
        // 代理的url网址或ip, port端口
        InetSocketAddress address = new InetSocketAddress(url, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);
        return factory;
    }
}
