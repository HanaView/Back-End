package com.hana.common.util;

import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class MessageUtil {

    private static String secret;
    private static String key;
    private static String phone;

    @Value("${message.secret}")
    public void setSecret(String secret) {
        MessageUtil.secret = secret;
    }

    @Value("${message.key}")
    public void setKey(String key) {
        MessageUtil.key = key;
    }

    @Value("${message.phone}")
    public void setPhone(String phone) {
        MessageUtil.phone = phone;
    }

    public static void sendMsg(String randomkey, String tele) {
        String api_key = key;
        String api_secret = secret;

        log.info(secret);
        log.info(api_secret);

        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();
        String url = "https://front-end-liart-five.vercel.app/" + "auth/mobile/camera?key=" + randomkey;
//        String msg = "해당 페이지에서 인증을 완료해주세요.\n" + url;
        String msg = url;

        params.put("to", tele);
        params.put("from", phone);
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
