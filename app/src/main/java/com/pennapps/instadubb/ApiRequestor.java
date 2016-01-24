package com.pennapps.instadubb;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Carlos on 1/24/2016.
 */
public class ApiRequestor extends AsyncTask<String, Void, String>{
    private static final String BASE_URL =
            "https://www.googleapis.com/language/translate/v2?key=AIzaSyCypqHfHOJwjn3g0wW40nmQi5iRIdLBSrk"; //xxx&q=yyy
    private static final String LANG_PARAM = "&target=";
    private static final String QUERY_PARAM = "&q=";

    private String mLang;

    public ApiRequestor(String langKey){
        mLang = langKey;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = null;
        try {
            url = BASE_URL + LANG_PARAM + URLEncoder.encode(mLang, "UTF-8") + QUERY_PARAM + URLEncoder.encode(strings[0], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = "Error";
        String targetResp = "\"translatedText\":";
        URL apiReq;

        try {
            apiReq = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) apiReq.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader stream = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            boolean found = false;

            while(!found && (line = stream.readLine()) != null){
                System.out.println(line);
                if(line.contains(targetResp)){
                    StringBuffer parsedLine = new StringBuffer(line.trim());
                    parsedLine.delete(0, targetResp.length());
                    result = parsedLine.toString().replaceAll("\"", "").trim();
                    result = result.substring(0, result.length()-1);
                    found = true;
                }
            }

            stream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
