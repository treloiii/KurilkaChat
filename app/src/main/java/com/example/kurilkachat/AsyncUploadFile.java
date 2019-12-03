package com.example.kurilkachat;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import http.HttpHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncUploadFile extends AsyncTask<String,Void,String> {

    private HttpHelper httpHelper;
    private OkHttpClient client;

    public void setHttpHelper(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }
    @Override
    protected String doInBackground(String... string) {
        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("token", string[0]);
            postData.put("name", string[1]);
            postData.put("message", string[2]);
            Request request = httpHelper.post(postData, null, "http://kurilkahttp.std-763.ist.mospolytech.ru/upload/img");
            Response response = client.newCall(request).execute();
            return response.body().string();
        }
        catch (Exception e){
            e.printStackTrace();
            return "error"+e;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
