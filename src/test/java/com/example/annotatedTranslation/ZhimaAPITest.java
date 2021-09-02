package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @author Yu Shaoqing
 * @date 2021/9/2/22:56
 */
public class ZhimaAPITest {
    @Test
    void zhimaApiTest(){
        ZhimaAPI zhimaAPI = new ZhimaAPI();
        DocTranslatedController.Ipmap = zhimaAPI.parseZhimaApi(zhimaAPI.getZhimaIp());
        for(Map.Entry<String , Integer> in : DocTranslatedController.Ipmap.entrySet()){
            System.out.println("ip = " + in.getKey() + "        port = " + in.getValue());
        }
    }
}
