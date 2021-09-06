package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

/**
 * @author Yu Shaoqing
 * @date 2021/9/6/10:35
 */
public class ValidIPTest {
    @Test
    void test(){
        String ip = "175.173.221.234";
        String port = "4210";
        int type = 0;

        IPBean ipBean = new IPBean(ip,Integer.parseInt(port),type);
        if(Util.isValid(ipBean))
        {
            System.out.println("valid");
        }else{
            System.out.println("illegal");
        }

    }
}
