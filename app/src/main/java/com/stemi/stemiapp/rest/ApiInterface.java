package com.stemi.stemiapp.rest;

import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Pooja on 17-08-2017.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("signup")
    Call<SignUpResponseModel> callSignUpApi(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<SignUpResponseModel> callLoginApi(@Field("email") String email, @Field("password") String password);

    @GET("getTipOfTheDay")
    Call<StatusMessageResponse> callTipOfTheDAy(@Query("date") String date, @Query("token") String token);

}
