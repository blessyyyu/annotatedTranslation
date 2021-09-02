package com.example.annotatedTranslation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Yu Shaoqing
 * @date 2021/7/18/20:15
 */
@RestController
@Component
public class DocTranslatedController {

    public String baseUrl = "http://localhost:8083/DocTranslated/findFirstByLanguageIdAndIsTranslated" +
            "?languageId=%s" +
            "&isTranslated=%s";
    public String countNumUrl = "http://localhost:8083/DocTranslated/count?languageId=%s&isTranslated=0";

    int remainCount;
    public static Map<String, Integer> Ipmap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(DocTranslatedController.class);
    int translatedCount = 0;
    public static boolean isWork = true;
    public static boolean isFirstTask = true;
    long startTime;
    String [] keys;

    String curIp;
    int curPort;

    @Autowired
    RestTemplate rest_Template;
    @Scheduled(fixedDelay = 1000)
    public void startTranlation() {
        if(isWork){
            if(isFirstTask){
//              从Ipmap里随机获得一个ip和port
                getRandomIp();
                log.info("start Translation! ");
                isFirstTask = false;
            }
            //languageId: 3 -> English;  4 -> ru ; 5 -> Ja
            int currentLanguageId = 4;
            String url = String.format(baseUrl,currentLanguageId,0);
            String counturl = String.format(countNumUrl,currentLanguageId);
            DocEntity doc = rest_Template.getForObject(url, DocEntity.class);
            try{
                String title = doc.getTitle();
                String translatedTitle = ProxyTranslator.googleTransWithProxy(curIp,curPort,title);

                DocEntity translateDoc = new DocEntity();
                translateDoc.setDocId(doc.getDocId());
                translateDoc.setTitle(translatedTitle);
                String abst = doc.getAbst();
                getRandomIp();
                String translatedAbst = ProxyTranslator.googleTransWithProxy(curIp,curPort,abst);


                translateDoc.setAbst(translatedAbst);
                String content = doc.getContent();

                String translatedContent = ProxyTranslator.googleTransWithProxy(curIp,curPort,content);


                translateDoc.setContent(translatedContent);

                if(translatedTitle.length() != 0 ){
                    //      用HTTPEntity的头文件包裹一下这个translateDoc
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<DocEntity> entity = new HttpEntity<>(translateDoc,headers);
                    rest_Template.postForObject("http://localhost:8083/DocTranslated/add", entity, String.class);
                    translatedCount+=1;
                    remainCount = rest_Template.getForObject(counturl,Integer.class);
                    log.info("docId = " + translateDoc.getDocId() + " Translate and SetIsTranslatedLabel successfully." + translatedCount +
                            " docs have been translated\n" + "remain:" + remainCount  + "docs");
                }
            }catch(Exception e)
            {
                log.info("the current ip:port" + curIp + ":" + curPort +
                        "\nplease change the Proxy and delete the unvalid ip in the map");
                Ipmap.remove(curIp);
                if(Ipmap.size() == 0){
                    ZhimaAPI zhimaAPI = new ZhimaAPI();
                    Ipmap = zhimaAPI.parseZhimaApi(zhimaAPI.getZhimaIp());
                }
                isWork = false;
                e.printStackTrace();
            }
        }else{
            if(remainCount != 0){
                isWork = true;
                // 切换proxy
                getRandomIp();
            }else{
                log.info(" remainCount = 0, please stop the program!");
                AnnotatedTranslationApplication.context.close();
            }

        }

    }

    void getRandomIp(){
        // 从Ipmap里随机获得一个ip和port
        keys = Ipmap.keySet().toArray(new String[0]);
        Random random = new Random();
        String randomIp = keys[random.nextInt(keys.length)];
        int randomPort = Ipmap.get(randomIp);
        curIp = randomIp;
        curPort = randomPort;
    }


}
