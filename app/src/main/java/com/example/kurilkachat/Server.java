package com.example.kurilkachat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Server {
    @POST("/post.php")
    Call<List<MessageServerResponse>> addMessage(@Query("id") String id, @Query("message") String message);
}
