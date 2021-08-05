package com.example.annotatedTranslation;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class AnnotatedTranslationApplication {

	public String baseUrl = "http://localhost:8080/DocTranslated/findFirstByLanguageIdAndIsTranslated" +
			"?languageId=%s" +
			"&isTranslated=%s";


	public static void main(String[] args) {
		SpringApplication.run(AnnotatedTranslationApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}


	public Map<String,Integer> setHttpParams(int languageId,int isTranslated){
		Map<String, Integer> params = new HashMap<>();
		params.put("languageId",languageId);
		params.put("isTranslated",isTranslated);
		return params;
	}


}
