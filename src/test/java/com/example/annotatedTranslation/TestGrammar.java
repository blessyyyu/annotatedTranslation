package com.example.annotatedTranslation;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author Yu Shaoqing
 * @date 2021/9/1/22:08
 */
public class TestGrammar {
    @Test
    void test(){
        Map<String, Integer> map = new HashMap<>();
        map.put("1",1);
        map.put("2",2);
        map.put("3",3);
        map.put("4",4);
        String [] keys = map.keySet().toArray(new String[0]);
        Random random = new Random();

        String randomKey = keys[random.nextInt(keys.length)];

        Integer randomValue = map.get(randomKey);
        System.out.println(randomKey);
    }
}
