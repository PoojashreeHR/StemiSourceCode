package com.stemi.stemiapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "ForgotActivity" ;
    @BindView(R.id.et_forgotEmail) EditText forgotEmail;
    @BindView(R.id.btn_submit) Button btnSubmit;
    @BindView(R.id.tv_forgot_not_registered) TextView notRegistered;

    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        btnSubmit.setOnClickListener(this);
        notRegistered.setOnClickListener(this);
    }

    public void callForgotPasswordApi(){
        CommonUtils.showLoadingProgress(this);
        Log.e(TAG, "callForgotPasswordApi: "+ forgotEmail.getText().toString());
        Call<StatusMessageResponse> call = apiInterface.callForgotPassword(forgotEmail.getText().toString());
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().contains("success")) {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
                        finish();
                    }else {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                CommonUtils.hideLoadingProgress();

            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_submit:
                callForgotPasswordApi();
                break;
            case R.id.tv_forgot_not_registered:
                startActivity(new Intent(ForgotPasswordActivity.this,SignUpActivity.class));
                finish();
                break;
        }
    }
}
