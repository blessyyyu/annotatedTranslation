package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yu Shaoqing
 * @date 2021/9/15/22:53
 */
class ProxyPoolTest {

    @Test
    void get_proxy() throws Exception {
        ProxyPool proxyPool = new ProxyPool();
        System.out.println(proxyPool.get_proxy());
    }
}