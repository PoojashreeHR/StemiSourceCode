package com.stemi.stemiapp.rest;

import com.stemi.stemiapp.model.apiModels.Hospital;
import com.stemi.stemiapp.model.apiModels.NearestHospitalResponse;
import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;

import java.util.List;

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

    @FormUrlEncoded
    @POST("changepassword")
    Call<StatusMessageResponse> callResetPassword( @Field("token") String token,
                                                    @Field("newPassword") String newPassword,
                                                    @Field("oldPassword") String oldPassword );

    @FormUrlEncoded
    @POST("forgetpassword")
    Call<StatusMessageResponse> callForgotPassword(@Field("email") String email);

    @GET("getNearestStemiHospital")
    Call<NearestHospitalResponse> callNearestHsopitalsApi(@Query("token") String token, @Query("lat") double latitude, @Query("lon") double longitude);

    @FormUrlEncoded
    @POST("uploadUserData")
    Call<StatusMessageResponse> callUploadDataApi(
            @Field("token") String token,
            @Field("id") String hospitalId,
            @Field("name") String name,
            @Field("age") String age,
            @Field("gender") String gender,
            @Field("phoneno") String phoneNo,
            @Field("address") String address,
            @Field("heightInCms") String heightInCms,
            @Field("weightInKgs") Double weightInKgs,
            @Field("waistInch") String waistInch,
            @Field("isSmoker") String isSmoker,
            @Field("hadHeartAttackBefore") String hadHeartAttackBefore,
            @Field("haveDiabetes") String haveDiabetes,
            @Field("haveHighBP") String haveHighBP,
            @Field("haveHighCholesterol") String haveHighCholesterol,
            @Field("hadParalyticStroke") String hadParalyticStroke,
            @Field("haveBronchialAsthma") String haveBronchialAsthma,
            @Field("familyHadHeartAttack") String familyHadHeartAttack
    );
}
