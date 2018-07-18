package com.farmease.app.services;

import com.farmease.app.bean.BeanBookedSlot;
import com.farmease.app.bean.BeanCart;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.bean.BeanHome;
import com.farmease.app.bean.BeanLogin;
import com.farmease.app.bean.BeanProduct;
import com.farmease.app.bean.BeanProducts;
import com.farmease.app.bean.BeanSearch;
import com.farmease.app.bean.BeanSubCategory;
import com.farmease.app.bean.BeanUserDetail;
import com.farmease.app.bean.BeanUserReview;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface APIService {

    @FormUrlEncoded
    @POST("login")
    Call<BeanLogin> userLogin(
            @Field("email") String email,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("signInWithFB")
    Call<BeanLogin> userFB(
            @Field("fb_id") String fb_id,
            @Field("device") String device,
            @Field("deviceToken") String deviceToken);

    @FormUrlEncoded
    @POST("signInWithGmail")
    Call<BeanLogin> userGmail(
            @Field("email") String email);

    @GET()
    Call<BeanLogin> dummy(
            @Url() String value);

    @FormUrlEncoded
    @POST("signup")
    Call<BeanCommon> userSignup(
      @Field("email") String email,@Field("password")String password,
      @Field("firstName")String fName,@Field("lastName")String lName,@Field("mobile")String mobile,
      @Field("device")String device,@Field("deviceToken")String deviceToken);

    @GET()
    Call<BeanCategory> category(
            @Url() String value);

    @GET()
    Call<BeanSubCategory> subcategory(
            @Url() String value);

    @FormUrlEncoded
    @POST("products")
    Call<BeanProducts> products(
            @Field("userLat") String lat,
            @Field("userLong") String lng,
            @Field("categoryId") String catId,
            @Field("subcategoryId") String subcatId,
            @Field("role") String role);

    @GET()
    Call<BeanProduct> product(
            @Url() String value);

    @GET()
    Call<BeanUserReview> productReviews(
            @Url() String value);

    @GET()
    Call<BeanProducts> productDetails(
            @Url() String value);

    @GET()
    Call<BeanUserDetail> userDetail(
            @Url() String value);

    
    @POST()
    Call<BeanCommon> logout(
      @Url() String value);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<BeanCommon> updateForgetPassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("otp") String otp);

    @FormUrlEncoded
    @POST("forgetPassword")
    Call<BeanCommon> forgetPassword(
            @Field("email") String email);

    @FormUrlEncoded
    @POST("changePassword")
    Call<BeanCommon> changePassword(
            @Field("email") String email,
            @Field("password") String password,
            @Field("newPassword") String newPassword);
    @GET()
    Call<BeanCommon> verifyOtp(
            @Url() String value);

    @PUT()
    Call<BeanCommon> updateUser(
            @Url() String value);

    @FormUrlEncoded
    @POST("product/checkAvailability")
    Call<BeanCommon> checkAvailablity(
      @Field("id")int id,
      @Field("date")String date
    );

    @GET()
    Call<BeanBookedSlot> bookedSlot(
            @Url() String value);

    @FormUrlEncoded
    @POST("cart/add")
    Call<BeanCommon> cartAdd(
            @Field("id")int id,
            @Field("hours")float hours,
            @Field("per_hour_rate") float per_hour,
            @Field("toHour") String toHour,
            @Field("fromHour")String fromHour,
            @Field("operator")String operator,
            @Field("tractor")String tractor,
            @Field("amount") String amount,
            @Field("date")String date,
            @Field("userId") String userId,
            @Field("deviceToken") String token
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "cart/delete", hasBody = true)
    Call<BeanCommon> deleteCart(
            @Field("id")int id,
            @Field("userId")String userId,
            @Field("deviceToken")String topken);


    @FormUrlEncoded
    @POST("home")
    Call<BeanHome> home(
            @Field("userLat") String lat,
            @Field("userLong") String lng,
            @Field("role") String role);

    @FormUrlEncoded
    @POST("carts/get")
    Call<BeanCart> cart(
            @Field("userId") String userId,
            @Field("deviceToken") String token);

    @FormUrlEncoded
    @POST("products/typeBased")
    Call<BeanProducts> productList(
            @Field("lat") String lat,
            @Field("long") String lng,
            @Field("type") String role);

    @FormUrlEncoded
    @POST("product/search")
    Call<BeanSearch> search(
            @Field("userLat") String userLat,
            @Field("userLong") String userLong,
            @Field("key")String key
    );
}
