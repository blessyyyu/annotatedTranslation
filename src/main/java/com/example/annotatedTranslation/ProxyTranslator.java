package com.example.annotatedTranslation;

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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ProxyTranslator {
	// 代理服务器
	static String proxyHost;
	static Integer proxyPort;

	private static HttpHost proxy = null;

	private static RequestConfig reqConfig = null;

	public static void main(String[] args) {
		System.out.println(doGoogleTranslateSingle("Tom Kershaw is a sports writer at The Independent, focusing primarily on football. He also led coverage of the Open Championship in Portrush and has reported from Wimbledon. He was a silver medallist in the young sports writer category at the 2019 and 2020 Sports Journalism Awards. "));
	}

	public static String googleTransWithProxy(String ip, int port, String text){
		proxy = new HttpHost(ip, port, "http");
		reqConfig = RequestConfig.custom().setConnectionRequestTimeout(5000).setConnectTimeout(10000) // 设置连接超时时间
				.setSocketTimeout(10000) // 设置读取超时时间
				.setExpectContinueEnabled(false).setProxy(new HttpHost(ip, port))
				.setCircularRedirectsAllowed(true) // 允许多次重定向
				.build();
		return doGoogleTranslateSingle(text);
	}

	public static String doGoogleTranslateSingle(String text) {
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
			e.printStackTrace();
			return "bad response";
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
	public static String doRequest(HttpRequestBase httpReq) {
		String result = new String();
		httpReq.setConfig(reqConfig);
		try {
			// 设置请求头
			setHeaders(httpReq);
			
			CloseableHttpClient httpClient = HttpClients.createDefault();
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
			return null;
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

	private static String token(String text) {
		String tk = "";
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
		try {
			FileReader reader = new FileReader("./tk/Google.js");
			engine.eval(reader);

			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable)engine;
				tk = String.valueOf(invoke.invokeFunction("token", text));
			}
		} catch (Exception e) {
			e.printStackTrace();
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