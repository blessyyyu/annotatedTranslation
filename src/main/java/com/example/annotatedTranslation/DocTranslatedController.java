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

import javax.print.Doc;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
    public final int YouDao_maxContentLength = 5000;
    public final int Tencent_maxLength = 2000;
    private static final Logger log = LoggerFactory.getLogger(DocTranslatedController.class);
    int translatedCount = 0;
    public static boolean isWork = true;
    long translatedNumberCharacters = 0;
    @Autowired
    RestTemplate restTemplate;

    @Scheduled(fixedDelay = 500)
    public void startTranlation() {
        if(isWork){
            //languageId: 3 -> English;  4 -> ru ; 5 -> Ja
            int currentLanguageId = 5;
            String url = String.format(baseUrl,currentLanguageId,0);
            DocEntity doc = restTemplate.getForObject(url, DocEntity.class);
            try{
                String title = doc.getTitle();
                String translatedTitle = GoogleTranslator.translationFromGoogle(title);
                translatedNumberCharacters += translatedTitle.length();
                DocEntity translateDoc = new DocEntity();
                translateDoc.setId(doc.getId());
                translateDoc.setTitle(translatedTitle);
                String abst = doc.getAbst();
                String translatedAbst = GoogleTranslator.translationFromGoogle(abst);
                translatedNumberCharacters += translatedAbst.length();
                translateDoc.setAbst(translatedAbst);
                String content = doc.getContent();

                String translatedContent = GoogleTranslator.translationFromGoogle(content);
                translatedNumberCharacters += translatedContent.length();

                translateDoc.setContent(translatedContent);
//      用HTTPEntity的头文件包裹一下这个translateDoc
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<DocEntity> entity = new HttpEntity<>(translateDoc,headers);
                restTemplate.postForObject("http://localhost:8083/DocTranslated/add", entity, String.class);

                String setIsTranslated_url = "http://localhost:8083/DocTranslated/setIsTranslatedByIds?ids=" + translateDoc.getId() +"&isTranslated=1";
                restTemplate.getForObject(setIsTranslated_url,String.class);
                translatedCount+=1;
                log.info("id = " + translateDoc.getId() + " Translate and SetIsTranslatedLabel successfully." + translatedCount +
                        " docs have been translated");
            }catch(Exception e)
            {
                log.info("## translation error; the Exception is  ##" + e);
//                long consumingTime = System.nanoTime() - startTime;
//                System.out.println("一共花费" + consumingTime/1000000000 + "秒\n" + "一共翻译字符:" + translatedNumberCharacters);
                isWork = false;
                throw e;
            }
        }else{
            log.info("isWork = false, please stop the program.");
        }

    }



}
