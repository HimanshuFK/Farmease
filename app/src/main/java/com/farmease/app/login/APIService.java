package com.farmease.app.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {
    @FormUrlEncoded
    @POST("login")

    Call<Result> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET()
    Call<Result> dummy(
            @Url() String value
    );

}
