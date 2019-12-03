package com.example.kurilkachat;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import com.bumptech.glide.Glide;

import java.io.File;

import http.HttpHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AsyncFileTempUpload extends AsyncTask<File,Boolean,String> {

    private OkHttpClient client;
    private HttpHelper httpHelper;
    private MainActivity activity;
    private Bitmap bitmap;
    private File sendFile;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public void setHttpHelper(HttpHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected String doInBackground(File... files) {
        publishProgress(false);
        sendFile=files[0];
        try {
            Request request=httpHelper.post(null,files[0],"http://kurilkahttp.std-763.ist.mospolytech.ru/upload/img/temp");
            Response response = client.newCall(request).execute();
            publishProgress(true);
            return response.body().string();
        }
        catch (Exception e){
            e.printStackTrace();
            publishProgress(true);
            return "error";
        }
    }

    @Override
    protected void onProgressUpdate(Boolean... values) {
        if(!values[0]) {
            activity.attachProgress.setVisibility(View.VISIBLE);
            activity.attachProgress.setIndeterminate(true);
        }
        else {
            activity.attachProgress.setVisibility(View.INVISIBLE);
            if(bitmap!=null)
                activity.imgView.setImageBitmap(bitmap);
            else{
                Glide.with(activity).load(sendFile).into(activity.imgView);
            }
            activity.imgView.setVisibility(View.VISIBLE);

        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        activity.deleteToken=s;
        activity.btnSend.setEnabled(true);
    }
}
