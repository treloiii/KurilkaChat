package com.example.kurilkachat;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import http.HttpHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncUploadFile extends AsyncTask<String,Boolean,String> {

    private HttpHelper httpHelper;
    private OkHttpClient client;
    private Context context;

    public void setHttpHelper(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... string) {
        publishProgress(true);
        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("token", string[0]);
            postData.put("name", string[1]);
            postData.put("message", string[2]);
            Request request = httpHelper.post(postData, null, "http://kurilkahttp.std-763.ist.mospolytech.ru/upload/img");
            Response response = client.newCall(request).execute();
            publishProgress(false);
            return response.body().string();
        }
        catch (Exception e){
            e.printStackTrace();
            publishProgress(false);
            return "error"+e;
        }

    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context,String.valueOf(values[0]),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
