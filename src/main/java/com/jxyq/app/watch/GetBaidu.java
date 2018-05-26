package com.jxyq.app.watch;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetBaidu {
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }

    public static String httpPost(String jsonCellPos) throws IOException {
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=3fcc5bcaba3d8bd8e4abc6437db26d0c&location=" +
                jsonCellPos + "&output=json&pois=1";
        URL realUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();
            return new String(read(inputStream), "UTF-8");
        } else {
            return null;
        }
    }

    public static byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        boolean len = true;
        byte[] buffer = new byte[1024];

        int len1;
        while ((len1 = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len1);
        }

        outStream.close();
        inputStream.close();
        return outStream.toByteArray();
    }

    public static String getAddressByGPS(String lat, String lon) {
        String address = "";
        try {
            String resStr = httpPost(lat + "," + lon);
            JsonParser parser = new JsonParser();
            JsonObject resJson = parser.parse(resStr.trim()).getAsJsonObject();

            int status = resJson.get("status").getAsInt();
            JsonObject resultJson = resJson.get("result").getAsJsonObject();
            if (status == 0) {
                if (resultJson.has("formatted_address")) {
                    address = resultJson.get("formatted_address").getAsString();
                }

                JsonArray pois = null;
                if (resultJson.has("pois")) {
                    pois = resultJson.getAsJsonArray("pois");
                }
                if (pois != null && pois.size() > 0) {
                    JsonObject first_pois = pois.get(0).getAsJsonObject();
                    double distance = first_pois.get("distance").getAsDouble();
                    String name = first_pois.get("name").getAsString();
                    String direction = first_pois.get("direction").getAsString();
                    address = address + ",距离" + name + direction + "方向大约" + distance + "米";
                }

                if (pois != null && pois.size() > 1) {
                    JsonObject first_pois = pois.get(1).getAsJsonObject();
                    double distance = first_pois.get("distance").getAsDouble();
                    String name = first_pois.get("name").getAsString();
                    String direction = first_pois.get("direction").getAsString();
                    address = address + ",距离" + name + direction + "方向大约" + distance + "米";
                }
            }
            return address;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
