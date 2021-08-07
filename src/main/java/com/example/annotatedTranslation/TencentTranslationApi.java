package com.example.annotatedTranslation;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;

/**
 * @author Yu Shaoqing
 * @date 2021/7/21/17:15
 */
public class TencentTranslationApi {
    public static final int Tencent_maxLength = 2000;

    /**
     *
     * @param jsonData
     */
    static String parseJSONWithGson(String jsonData){
        try{
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(jsonData);
            JsonObject root = element.getAsJsonObject();
            JsonElement trans = root.get("TargetText");
            //System.out.println("此次翻译结果为：" + translation);
            return trans.getAsString();
        }catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String queryTranslationAPI(String SourceText) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential("AKIDBAtMYT8WdT3OSFkzxe3C3dvxhAMHvIPF", "bvxiCSUFivqUryoMddMoyrSmpb6KHxLI");
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("tmt.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        TmtClient client = new TmtClient(cred, "ap-beijing", clientProfile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        TextTranslateRequest req = new TextTranslateRequest();
        if(SourceText.length() > Tencent_maxLength)
            SourceText = SourceText.substring(0,Tencent_maxLength - 1);
        // 设置要翻译的语句
        req.setSourceText(SourceText);
        req.setSource("auto");
        req.setTarget("zh");
        req.setProjectId(1234258L);
        // 返回的resp是一个TextTranslateResponse的实例，与请求对象对应
        TextTranslateResponse resp = client.TextTranslate(req);
        // 输出json格式的字符串回包
        String jsonstr = TextTranslateResponse.toJsonString(resp);
        //System.out.println(TextTranslateResponse.toJsonString(resp));
        return parseJSONWithGson(jsonstr);


    }


}
