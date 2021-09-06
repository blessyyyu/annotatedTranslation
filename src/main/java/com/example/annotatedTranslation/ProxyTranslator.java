package com.example.annotatedTranslation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ProxyTranslator {

	private static HttpHost proxy = null;

	private static RequestConfig reqConfig = null;
	private static SocketConfig socketConfig = null;

	public static void main(String[] args) throws Exception {
		int OneIpTransCount = 0;
		while(true){
			System.out.println(googleTransWithProxy("113.243.32.19",4243,"Tom Kershaw is a sports writer at The Independent, focusing primarily on football. He also led coverage of the Open Championship in Portrush and has reported from Wimbledon. He was a silver medallist in the young sports writer category at the 2019 and 2020 Sports Journalism Awards. "));
			OneIpTransCount += 1;
			System.out.println("the count = " + OneIpTransCount);
				Thread.sleep(300);
		}

	}

	public static String googleTransWithProxy(String ip, int port, String text) throws Exception {
		proxy = new HttpHost(ip, port, "http");
		socketConfig = SocketConfig.custom()
				.setSoKeepAlive(false)
				.setSoLinger(1)
				.setSoReuseAddress(true)
				.setSoTimeout(10000)
				.setTcpNoDelay(true).build();


		reqConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(5000) // 设置连接超时时间
				.setSocketTimeout(5000) // 设置读取超时时间
				.setExpectContinueEnabled(false).setProxy(new HttpHost(ip, port))
				.setCircularRedirectsAllowed(true) // 允许多次重定向
				.build();
		if(text.length() > 1000){
			text = text.substring(0,999);
		}
		return doGoogleTranslateSingle(text);
	}

	public static String doGoogleTranslateSingle(String text) throws Exception {
		// 目标地址
		String targetUrl = "https://translate.googleapis.com/translate_a/single";

		try {
			// 设置url参数 (可选)
			Map<String, String> urlParams = new HashMap<>();
			urlParams.put("client", "gtx");
			urlParams.put("sl", "auto");
			urlParams.put("tl", "zh-CN");
			urlParams.put("hl", "zh-CN");
			urlParams.put("dt", "t");
			urlParams.put("ie", "UTF-8");
			urlParams.put("oe", "UTF-8");
			urlParams.put("source", "btn");
			urlParams.put("ssel", "0");
			urlParams.put("tsel", "0");
			urlParams.put("kc", "0");
			urlParams.put("tk", token(text));
			urlParams.put("q", text);
			String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramsAdapter(urlParams), "UTF-8"));
			HttpGet httpGet = new HttpGet(targetUrl + "?" + paramStr);
			String result = doRequest(httpGet);
			return parses(result);
		} catch (Exception e) {
			throw e;
		}

	}


	/**
	 * 设置请求头
	 * 
	 * @param httpReq
	 */
	private static void setHeaders(HttpRequestBase httpReq) {
		httpReq.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
		httpReq.addHeader("Accept","*/*");
	}

	/**
	 * 执行请求
	 * 
	 * @param httpReq
	 * @return
	 */
	public static String doRequest(HttpRequestBase httpReq) throws IOException {
		String result = new String();
		httpReq.setConfig(reqConfig);

		try {
			// 设置请求头
			setHeaders(httpReq);
			
//			CloseableHttpClient httpClient = HttpClients.createDefault();

			CloseableHttpClient httpClient = HttpClientBuilder.create()
					.setDefaultSocketConfig(socketConfig).build();

			// 执行请求
			CloseableHttpResponse httpResp = httpClient.execute(httpReq);
			
			// 保存Cookie
			
			// 获取http code
			int statusCode = httpResp.getStatusLine().getStatusCode();
//			System.out.println(statusCode);
			
			HttpEntity entity = httpResp.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}

			httpResp.close();
			httpClient.close();
			httpReq.abort();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	/**
	 * 参数适配器，将系统定义的请求参数转换成HttpClient能够接受的参数类型
	 * 
	 * @param map
	 *            系统定义的请求参数
	 * @return HttpClient要求的参数类型
	 */
	private static List<NameValuePair> paramsAdapter(Map<String, String> map) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		for (Entry<String, String> entry : map.entrySet()) {
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return nvps;
	}

	private static String token(String text) throws Exception{
		String tk = "";
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		try {
			FileReader reader = new FileReader("./tk/Google.js");
			engine.eval(reader);

			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable)engine;
				tk = String.valueOf(invoke.invokeFunction("token", text));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return tk;
	}

	public static String parses(String text) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder builder = new StringBuilder();
		JsonNode node = mapper.readTree(text).get(0);
		for(int i = 0 ; i < node.size(); i++){
			String curNodeStr = node.get(i).get(0).toString();
			curNodeStr = curNodeStr.substring(1,curNodeStr.length() - 1);
			builder.append(curNodeStr);
		}
		return builder.toString();
	}

}