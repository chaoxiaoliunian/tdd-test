package org.example;

/**
 * @author qishaojun
 */
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DingTalkUtil {

    private static OkHttpClient mClient;
    private static String url;

    //初始化客户端
    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10L, TimeUnit.SECONDS);
        builder.readTimeout(10L, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(200);
        dispatcher.setMaxRequests(200);
        builder.dispatcher(dispatcher);
        mClient = builder.build();
        try {
            url = getSign();
        } catch (Exception e) {
            log.error("获取签名失败！",e);
        }
    }

    /**
     * 通用 POST 请求方法  依赖 OKhttp3
     * @param message 所要发送的消息
     * @return 发送状态回执
     */
    public static String postWithJson(String message) {
        JSONObject jsonObject = new JSONObject();
        //固定参数
        jsonObject.put("msgtype", "text");
        JSONObject content = new JSONObject();
        //此处message是你想要发送到钉钉的信息
        content.put("content", message);
        jsonObject.put("text", content);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
        Request request = new Request.Builder().url(url).post(body).build();
        try {
            Response response = mClient.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("消息发送失败！",e);
        }
        return null;
    }

    /**
     * 获取签名
     * @return 返回签名
     */
    private static String getSign() throws Exception {
        String baseUrl = "https://openplatform-pro.dce.com.cn/robot/send?access_token=";
        String token = "71be91c93969dc36a428569340c0614a18321d2f77c379c87629c52290f6de39";
        String secret = "SECc1e5e9ae041f459a5f44d716250e831e7c681d3c4f2e3aac646dee571676f832";
        long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        return baseUrl + token + "&timestamp=" + timestamp + "&sign=" +
                URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
    }

}

