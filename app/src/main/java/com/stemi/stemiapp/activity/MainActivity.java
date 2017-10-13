package com.stemi.stemiapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AppConstants,View.OnClickListener {
    private static final String TAG = "SignupResponse";
    ApiInterface apiInterface;
    AppSharedPreference appSharedPreferences;

    @BindView(R.id.et_email_login) EditText etEmail;
    @BindView(R.id.et_password_login) EditText etPassword;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.tv_not_registered) TextView tvNotRegistered;
    @BindView(R.id.tv_forgot_password) TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreferences = new AppSharedPreference(this);

        CommonUtils.setRobotoRegularFonts(MainActivity.this,tvForgotPassword);
        CommonUtils.setRobotoRegularFonts(MainActivity.this,tvNotRegistered);

        if(appSharedPreferences.getUserToken(USER_TOKEN) != null){
            startActivity(new Intent(MainActivity.this,TrackActivity.class));
            finish();
        }else {
            Log.e(TAG, "onCreate: TOKEN IS NULL" );
        }

        btnLogin.setOnClickListener(this);
        tvNotRegistered.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
    }


    public void callSignIn(){
        CommonUtils.showLoadingProgress(this);
        Call<SignUpResponseModel> call = apiInterface.callLoginApi(etEmail.getText().toString(),etPassword.getText().toString());
        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().contains("success")) {
                        CommonUtils.hideLoadingProgress();
                        SignUpResponseModel signUpResponseModel = response.body();
                        appSharedPreferences.addUserToken(USER_TOKEN, "" + signUpResponseModel.getToken());
                        appSharedPreferences.addLoginId(etEmail.getText().toString());
                        Log.e(TAG, "onResponse: Signup Response" + signUpResponseModel);
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        if(appSharedPreferences.getUserId() == null){
                            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                            new AppSharedPreference(MainActivity.this).setFirstTimeLaunch(IS_FIRST_TIME_LAUNCH,true);
                        }else {
                            startActivity(new Intent(MainActivity.this, TrackActivity.class));
                        }

                        /*if(appSharedPreferences.isFirstTimeLaunch(IS_FIRST_TIME_LAUNCH)){
                            startActivity(new Intent(MainActivity.this, TrackActivity.class));
                        }else {
                            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
                            new AppSharedPreference(MainActivity.this).setFirstTimeLaunch(IS_FIRST_TIME_LAUNCH,true);
                        }*/
                        finish();
                    }else {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                if(Validate()) {
                    callSignIn();
                }
                break;

            case R.id.tv_not_registered:
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                finish();
                break;

            case R.id.tv_forgot_password:
                startActivity(new Intent(MainActivity.this,ForgotPasswordActivity.class));
                finish();
        }
    }

    public boolean Validate(){
        Boolean valid = true;

        String emailId = etEmail.getText().toString();
        if (TextUtils.isEmpty(emailId)) {
            etEmail.setError("Required");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        
        return valid;
    }
}
