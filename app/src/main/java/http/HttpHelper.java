package http;

import androidx.annotation.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpHelper {

    public HttpHelper() {
    }


    public Request post(@Nullable Map<String,String> dataPart, @Nullable File file,String url){
        RequestBody req;
        MultipartBody.Builder builder= new MultipartBody.Builder();
        if(dataPart!=null) {
            for (Map.Entry<String, String> entry : dataPart.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if(file!=null){
            try {
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse(Files.probeContentType(file.toPath())), file));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        req=builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(req)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();
        return request;
    }
}
