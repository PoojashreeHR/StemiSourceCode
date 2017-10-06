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
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;
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

public class ResetPasswordActivity extends AppCompatActivity implements AppConstants {

    private static final String TAG = "ResetPasswordActivity" ;
    @BindView(R.id.reset_submit)Button resetSubmit;
    @BindView(R.id.et_receive_password)EditText receivePassword;
    @BindView(R.id.et_new_password)EditText newPassword;
    @BindView(R.id.et_confirm_new_password)EditText confirmNewPassword;
    AppSharedPreference appSharedPreferences;

    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreferences = new AppSharedPreference(this);

        resetSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForgotPasswordApi();
            }
        });

    }


    public void callForgotPasswordApi(){
        CommonUtils.showLoadingProgress(this);
        Log.e(TAG, "callForgotPasswordApi: "+ appSharedPreferences.getUserToken(USER_TOKEN));
        Call<StatusMessageResponse> call = apiInterface.callResetPassword(appSharedPreferences.getUserToken(USER_TOKEN),
                newPassword.getText().toString(),receivePassword.getText().toString());
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().contains("success")) {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(ResetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
                        finish();
                    }else {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(ResetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {
                CommonUtils.hideLoadingProgress();

            }
        });
    }
}
