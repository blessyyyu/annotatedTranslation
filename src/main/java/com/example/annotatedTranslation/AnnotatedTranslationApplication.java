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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@SpringBootApplication
@EnableScheduling
public class AnnotatedTranslationApplication {
	public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		// 初始化Ipmap
		ZhimaAPI zhimaAPI = new ZhimaAPI();
		DocTranslatedController.Ipmap = zhimaAPI.parseZhimaApi(zhimaAPI.getZhimaIp());
		context = SpringApplication.run(AnnotatedTranslationApplication.class, args);
	}

	@Bean
	public static RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}



}
