package com.example.annotatedTranslation;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tmt.v20180321.TmtClient;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateRequest;
import com.tencentcloudapi.tmt.v20180321.models.TextTranslateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Yu Shaoqing
 * @date 2021/7/21/17:03
 */
public class TecentTranslationTest {
    @Test
    void TranslationTest(){
        try{
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
            req.setSourceText("hello darkness my old friend");
            req.setSource("auto");
            req.setTarget("zh");
            req.setProjectId(1234258L);
            // 返回的resp是一个TextTranslateResponse的实例，与请求对象对应
            TextTranslateResponse resp = client.TextTranslate(req);
            // 输出json格式的字符串回包
            String jsonstr = TextTranslateResponse.toJsonString(resp);
            System.out.println(TencentTranslationApi.parseJSONWithGson(jsonstr));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
