package com.test.testtask.data.network;

import com.test.testtask.data.entity.Post;
import com.test.testtask.data.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {

    @GET("posts")
    Call<List<Post>> getPostList();

    @GET("users/{userId}")
    Call<User> getUserInfo(@Path("userId") int userID);

}
