package com.hana.common.util;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class OCR {
    private static final Logger log = LoggerFactory.getLogger(OCR.class);

    public static JSONObject getResult(String imgname){
        JSONObject obj = null;

        String apiURL = "https://gojlwur9ps.apigw.ntruss.com/custom/v1/30143/eb33e617a5df7970a058156ee8b6efab7adb27e66aab34f6dc3105fc95c4ca4b/infer";
        String secretKey = "UERwR29qVHBiVVRXU2RhbHVibEZOb0t2bWVmbmxxZ1E=";
        String imageFile = imgname;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setReadTimeout(30000);
            con.setRequestMethod("POST");
            String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.add(image);
            json.put("images", images);
            String postParams = json.toString();

            con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            long start = System.currentTimeMillis();
            File file = new File(imageFile);
            writeMultiPart(wr, postParams, file, boundary);
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JSONParser parser = new JSONParser();
            obj = (JSONObject) parser.parse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return obj;
    }

    private static void writeMultiPart(OutputStream out, String jsonMessage, File file, String boundary) throws
            IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage);
        sb.append("\r\n");

        out.write(sb.toString().getBytes("UTF-8"));
        out.flush();

        if (file != null && file.isFile()) {
            out.write(("--" + boundary + "\r\n").getBytes("UTF-8"));
            StringBuilder fileString = new StringBuilder();
            fileString
                    .append("Content-Disposition:form-data; name=\"file\"; filename=");
            fileString.append("\"" + file.getName() + "\"\r\n");
            fileString.append("Content-Type: application/octet-stream\r\n\r\n");
            out.write(fileString.toString().getBytes("UTF-8"));
            out.flush();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int count;
                while ((count = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.write("\r\n".getBytes());
            }

            out.write(("--" + boundary + "--\r\n").getBytes("UTF-8"));
        }
        out.flush();
    }

    private static String JSONString;

    public static Map getData(JSONObject obj){
        Map<String,String> map = new HashMap<>();
        JSONArray images = (JSONArray) obj.get("images");
        JSONObject jo1 = (JSONObject) images.get(0);

        JSONString = jo1.toJSONString();
        JSONArray fields = (JSONArray) jo1.get("fields");


        JSONObject username_obj = (JSONObject) fields.get(0);
        String inferText = (String)username_obj.get("inferText");
        int index = inferText.indexOf('(');
        String username = (index != -1) ? inferText.substring(0, index) : inferText;

        JSONObject usernum_obj = (JSONObject) fields.get(1);
        String usernum = (String)usernum_obj.get("inferText");
        String fixUsernum = usernum.substring(0,6) + usernum.substring(7);

        JSONObject useraddress_obj = (JSONObject) fields.get(2);
        String useraddress = (String)useraddress_obj.get("inferText");
        useraddress.replaceAll("\n", "");

        JSONObject userdate_obj = (JSONObject) fields.get(3);
        String userdate = (String)userdate_obj.get("inferText");


        map.put("username", username);
        map.put("usernum", fixUsernum);
        map.put("useraddress", useraddress);
        map.put("userdate", userdate);
        log.info(username + usernum + useraddress + userdate);

        return map;
    }
}
