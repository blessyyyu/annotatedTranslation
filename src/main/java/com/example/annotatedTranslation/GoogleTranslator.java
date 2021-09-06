package com.example.annotatedTranslation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;




public final class GoogleTranslator  {
    private static final String url = "https://translate.googleapis.com/translate_a/single";

    private static Map<String,String> formData = new HashMap<>();
    private static Map<LANG,String> langMap = new HashMap<>();


    public static void setLangSupport() {
        langMap.put(LANG.ZH, "zh-CN");
        langMap.put(LANG.AUTO, "auto");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "ja");
        langMap.put(LANG.RU, "ru");
    }


    public static void setFormData(LANG from, LANG to, String text) {
        formData.put("client", "gtx");
        formData.put("sl", langMap.get(from));
        formData.put("tl", langMap.get(to));
        formData.put("hl", "zh-CN");
        formData.put("dt", "t");
        formData.put("ie", "UTF-8");
        formData.put("oe", "UTF-8");
        formData.put("source", "btn");
        formData.put("ssel", "0");
        formData.put("tsel", "0");
        formData.put("kc", "0");
        formData.put("tk", token(text));
        formData.put("q", text);
    }


    public static String query(String url) throws Exception {
        URIBuilder uri = new URIBuilder(url);
        for (String key : formData.keySet()) {
            String value = formData.get(key);
            uri.addParameter(key, value);
        }
        return Util.getResponse(uri.toString());
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

    public static String TranslateSingle(String text){
        setLangSupport();
        setFormData(LANG.AUTO,LANG.ZH,text);
        if(text.length() > 3000){
            text = text.substring(0, 2999);
        }
        String result = null;
        try {
            result = parses(query(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) throws Exception {
        setLangSupport();
        int OneIpTransCount = 0;
        while(true){
            String q = "Tom Kershaw is a sports writer at The Independent, focusing primarily on football. He also led coverage of the Open Championship in Portrush and has reported from Wimbledon. He was a silver medallist in the young sports writer category at the 2019 and 2020 Sports Journalism Awards. ";
            setFormData(LANG.AUTO,LANG.ZH,q);
            String result = parses(query(url));
            System.out.println(result);
            OneIpTransCount += 1;
            System.out.println("the count =" + OneIpTransCount);
            Thread.sleep(1000);
        }

    }
}
