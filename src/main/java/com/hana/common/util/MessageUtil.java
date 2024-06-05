package com.hana.common.util;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

@Slf4j
public class MessageUtil {
    @Value("${message.secret}")
    static String secret;
    @Value("${message.key}")
    static String key;
    @Value("${message.phone}")
    static String phone;

    public static void Sendmsg(String randomkey, String tele){
        String api_key = key;
        String api_secret = secret;
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();
        String url = "링크" + "/camera?key=" + randomkey;
        String msg = "해당 페이지에서 인증을 완료해주세요.\n" + url;

        params.put("to", tele);
        //유지
        params.put("from", phone);
        //유지
        params.put("type", "SMS");
        params.put("text", msg);
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            log.info(obj.toString());
        } catch (CoolsmsException e) {
            log.info(e.getMessage());
            log.info(String.valueOf(e.getCode()));
        }
    }
}
