package com.example.annotatedTranslation;

import com.alibaba.fastjson.JSON;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yu Shaoqing
 * @date 2021/7/19/15:53
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocTranslatedControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void startTranlation() {
        String url = "http://localhost:8080/DocTranslated/setDocTranslated";

        DocEntity doc = new DocEntity();
        doc.setId(1);
        doc.setTitle("测试");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocEntity> entity = new HttpEntity<>(doc, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        assertEquals("success", response);

        restTemplate.postForObject("http://localhost:8080/docState/setOneInDocState",null,String.class);

        String setIsTranslated_url = "http://localhost:8080/DocTranslated/setIsTranslated?id={1}";
        ResponseEntity<String> response2 = restTemplate.getForEntity(setIsTranslated_url,String.class,doc.getId());

        assertEquals(response2.getBody(),"Set IsTranslated successfully.");

        String delete_url = "http://localhost:8080/DocTranslated/delete?id=" + doc.getId();
        restTemplate.delete(delete_url);
    }

}