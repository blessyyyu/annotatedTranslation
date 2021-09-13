package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

/**
 * @author Yu Shaoqing
 * @date 2021/9/1/22:08
 */
public class TestGrammar {
    @Test
    void test() throws Exception {
        ProxyPool proxyPool = new ProxyPool();
        System.out.println(proxyPool.get_proxy());

    }
}
