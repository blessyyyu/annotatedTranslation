package com.example.annotatedTranslation;

import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;

import java.util.List;

/**
 * @author Yu Shaoqing
 * @date 2021/7/24/19:12
 */
public class GoogleTranslator {
    static String translationFromGoogle(String q){
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        if(q.length() > 3000){
            q = q.substring(0,2999);
        }
        querierTrans.setParams(LANG.AUTO, LANG.ZH,q);
        querierTrans.attach(new com.swjtu.trans.impl.GoogleTranslator());
        List<String> resultTrans = querierTrans.execute();
        StringBuilder builder = new StringBuilder();
        for (String str : resultTrans) {
            builder.append(str);
        }
        return builder.toString();
    }
}
