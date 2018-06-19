package com.farmease.app.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("login")

    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}
