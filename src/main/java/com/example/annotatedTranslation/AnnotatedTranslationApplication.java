package com.example.annotatedTranslation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class AnnotatedTranslationApplication {

	// 用来停止Spring boot项目
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
