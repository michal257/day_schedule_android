package com.project.dayshedule.dayshedule;

import android.content.Context;

import com.project.dayshedule.dayshedule.Enum.RequestType;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ConnectionAPI {

    String urlServer = "http://192.168.1.3/DaySheduleApi/Controllers/";
    //String urlServer = "http://mp66.linuxpl.info/DaySheduleApi/Controllers/";
    Context mContext;

    public ConnectionAPI(Context mContext) {
        this.mContext = mContext;
    }

    public String connectAPI(String urlFile, JSONObject jObject, RequestType reqType){
        String response = null;

        String json = "";

        if(jObject != null) {
            json = jObject.toString();
        }

        try{
            URL url = new URL(urlServer + urlFile);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(reqType.name());

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Encoding", "application/json");
            conn.setRequestProperty("Cookie", SharedService.getSessionId(mContext));

            if (reqType.name() != "GET"){
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
            }

            //read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = JSONParser.convertStreamToString(in);

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get("Set-Cookie");

            if (cookiesHeader != null) {
                for (String cookie : cookiesHeader) {
                    String test = cookie;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
