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

/**
 * @author Yu Shaoqing
 * @date 2021/7/18/20:15
 */
@RestController
@Component
public class DocTranslatedController {
//    public String baseUrl = "http://localhost:8080/DocTranslated/findFirstByLanguageIdAndIsTranslated" +
//            "?languageId=%s" +
//            "&isTranslated=%s";
    public String baseUrl = "http://47.104.172.146:8083/DocTranslated/findFirstByLanguageIdAndIsTranslated" +
            "?languageId=%s" +
            "&isTranslated=%s";
    public final int YouDao_maxContentLength = 5000;

    private static final Logger log = LoggerFactory.getLogger(DocTranslatedController.class);
    int translatedCount = 0;
    public static boolean isWork = true;
    public static boolean isFirstTask = true;
    long translatedNumberCharacters = 0;
    long startTime;
    @Autowired
    RestTemplate restTemplate;
    @Scheduled(fixedDelay = 1000)
    public void startTranlation() throws IOException {
        if(isWork){
            if(isFirstTask){
               startTime = System.nanoTime();
                Date day=new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

               log.info(df.format(day) + " start Translation! ");
               isFirstTask = false;
            }
            //languageId: 3 -> English;  4 -> ru ; 5 -> Ja
            int currentLanguageId = 4;
            String url = String.format(baseUrl,currentLanguageId,0);
            DocEntity doc = restTemplate.getForObject(url, DocEntity.class);
            try{
                String title = doc.getTitle();
                String translatedTitle = GoogleTranslator.translationFromGoogle(title);
//                String translatedTitle = TencentTranslationApi.queryTranslationAPI(title);
                translatedNumberCharacters += title.length();
                DocEntity translateDoc = new DocEntity();
                translateDoc.setDocId(doc.getDocId());
                translateDoc.setTitle(translatedTitle);
                String abst = doc.getAbst();
                String translatedAbst = GoogleTranslator.translationFromGoogle(abst);
//                String translatedAbst = TencentTranslationApi.queryTranslationAPI(abst);

                translatedNumberCharacters += abst.length();
                translateDoc.setAbst(translatedAbst);
                String content = doc.getContent();

                String translatedContent = GoogleTranslator.translationFromGoogle(content);
//                String translatedContent = TencentTranslationApi.queryTranslationAPI(content);
                translatedNumberCharacters += content.length();

                translateDoc.setContent(translatedContent);
//      用HTTPEntity的头文件包裹一下这个translateDoc
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<DocEntity> entity = new HttpEntity<>(translateDoc,headers);
                restTemplate.postForObject("http://47.104.172.146:8083/DocTranslated/add", entity, String.class);

//                String setIsTranslated_url = "http://localhost:8080/DocTranslated/setIsTranslatedByIds?ids=" + translateDoc.getId() +"&isTranslated=1";
//                restTemplate.getForObject(setIsTranslated_url,String.class);
                translatedCount+=1;
                log.info("docId = " + translateDoc.getDocId() + " Translate and SetIsTranslatedLabel successfully." + translatedCount +
                        " docs have been translated");
            }catch(Exception e)
            {
                log.info("## translation error; the Exception is  ##" + e);
                long consumingTime = System.nanoTime() - startTime;
                File writeMessage = new File(".\\Count_and_Time.txt");
                if(!writeMessage.exists()){
                    writeMessage.createNewFile();
                }

                BufferedWriter out = new BufferedWriter(new FileWriter(writeMessage, true));
                Date day=new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                out.write("\n" + df.format(day) + "\t 一共花费" + consumingTime/1000000000 + "秒\t" + "一共翻译字符:" + translatedNumberCharacters);
                out.close();
                log.info(df.format(day) + "一共花费" + consumingTime/1000000000 + "秒\n" + "一共翻译字符:" + translatedNumberCharacters);
                isWork = false;
            }
        }else{
            log.info("isWork = false, please stop the program.");
        }

    }



}
