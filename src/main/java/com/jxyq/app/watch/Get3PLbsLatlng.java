package com.jxyq.app.watch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jxyq.model.watch.LBSData;
import com.jxyq.model.watch.Location;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Get3PLbsLatlng {
    public Get3PLbsLatlng() {
    }

    public static String replaceBlank(String str) {
        String des = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            des = m.replaceAll("");
        }

        return des;
    }

    public static String httpPost(String jsonCellPos) throws IOException {
        String url = "http://minigps.org/as?" + jsonCellPos;
        URL realUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setDoOutput(false);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(jsonCellPos.length()));
        httpURLConnection.setRequestProperty("Content-Type", "Application/json");
        if (httpURLConnection.getResponseCode() == 200) {
            InputStream inputStream = httpURLConnection.getInputStream();
            String rs = new String(read(inputStream), "UTF-8");
            inputStream.close();
            httpURLConnection.disconnect();
            return rs;
        } else {
            return null;
        }
    }

    public static byte[] read(InputStream inputSream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        boolean len = true;
        byte[] buffer = new byte[1024];

        int len1;
        while ((len1 = inputSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len1);
        }

        outStream.close();
        inputSream.close();
        return outStream.toByteArray();
    }

    public static String encodeHex(int i) {
        return Integer.toHexString(i);
    }

    public static Location getLbsLoc(List<LBSData> lbs_list) {
        if (lbs_list == null || lbs_list.size() <= 0) return null;

        String params = "x=";
        int mcc = lbs_list.get(0).getMcc();
        int mnc = lbs_list.get(0).getMnc();
        params = params + encodeHex(mcc) + "-" + encodeHex(mnc);
        for (LBSData lbs : lbs_list) {
            params = params + "-" + encodeHex(lbs.getLac()) + "-" + encodeHex(lbs.getCell()) + "-" + encodeHex(lbs.getSignal());
        }
        return getLbsLoc(params);
    }

    public static Location getLbsLoc(String str) {
        String params = str + "&ta=1&p=1&mt=0&needaddress=0";
        try {
            String resStr = httpPost(params);
            JsonParser parser = new JsonParser();
            JsonObject resJson = parser.parse(resStr).getAsJsonObject();
            int status = resJson.get("status").getAsInt();

            if (status == 0) {
                double longitude = resJson.get("lon").getAsDouble();
                double latitude = resJson.get("lat").getAsDouble();
                Location loc = new Location(longitude, latitude);
                return loc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
