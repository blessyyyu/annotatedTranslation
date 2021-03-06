package com.example.annotatedTranslation;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Yu Shaoqing
 * @date 2021/7/18/16:53
 */
@SpringBootTest
public class HttpEntityTest {
    @Test
    void httpTest(){
        try {
            HttpEntity entity = new StringEntity("这一个字符串实体", "UTF-8");
            //内容类型
            System.out.println(entity.getContentType());
            //内容的编码格式
            System.out.println(entity.getContentEncoding());
            //内容的长度
            System.out.println(entity.getContentLength());
            //把内容转成字符串
            System.out.println(EntityUtils.toString(entity));
            //内容转成字节数组
            System.out.println(EntityUtils.toByteArray(entity).length);
            //还有个直接获得流
            System.out.println(entity.getContent().toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
        } catch (IOException e) {
        }
    }
}
