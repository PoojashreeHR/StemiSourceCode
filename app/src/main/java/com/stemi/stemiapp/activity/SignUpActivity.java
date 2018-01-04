package com.stemi.stemiapp.activity;

import android.content.Intent;
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
import com.stemi.stemiapp.activity.LoginView.MainActivity;
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

public class SignUpActivity extends AppCompatActivity implements AppConstants, View.OnClickListener {

    @BindView(R.id.et_email_registration) EditText etRegEmail;
    @BindView(R.id.et_password_registration) EditText etRegPassword;
    @BindView(R.id.et_confirmPassword) EditText etConfirmPassword;
    @BindView(R.id.bt_signUp) Button btSignUp;
    @BindView(R.id.tv_already_reg) TextView tvAlreadyReg;

    AppSharedPreference appSharedPreference;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        CommonUtils.setRobotoRegularFonts(SignUpActivity.this,tvAlreadyReg);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreference = new AppSharedPreference(this);

        btSignUp.setOnClickListener(this);
        tvAlreadyReg.setOnClickListener(this);

    }

    public void callSignUp(){
        CommonUtils.showLoadingProgress(this);
        Call<SignUpResponseModel> call = apiInterface.callSignUpApi(etRegEmail.getText().toString(),etRegPassword.getText().toString());
        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().contains("success")) {
                        CommonUtils.hideLoadingProgress();
                        SignUpResponseModel signUpResponseModel = response.body();
                        appSharedPreference.addUserToken(USER_TOKEN, "" + signUpResponseModel.getToken());
                        appSharedPreference.addLoginId(etRegEmail.getText().toString());
                        Log.e("", "onResponse: Signup Response" + signUpResponseModel);
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, RegistrationActivity.class));
                        finish();
                    }else {
                        CommonUtils.hideLoadingProgress();
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<SignUpResponseModel> call, Throwable t) {
                CommonUtils.hideLoadingProgress();
                Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_signUp:
                if (validateAllFields()) {
                    if (etConfirmPassword.getText().toString().equals(etRegPassword.getText().toString())) {
                        if (etRegPassword.getText().toString().contains("\\s")
                                || etRegPassword.getText().toString().contains(" ")) {
                            Toast.makeText(this, "Spaces are not allowed in password", Toast.LENGTH_SHORT).show();
                        } else {
                            callSignUp();
                        }
                    } else
                        Toast.makeText(this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_already_reg:
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                finish();
        }
    }

    private boolean validateAllFields() {
        boolean valid = true;

        String email = etRegEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Required");
            valid = false;
        } else {
            etRegEmail.setError(null);
        }

        String password = etRegPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Required");
            valid = false;
        } else if(password.length() < 6 || password.length() > 12){
            etRegPassword.setError("must be 6 characters long");
           // Toast.makeText(this, "must be 6 characters long", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String confirmPAssword = etConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(confirmPAssword)) {
            etConfirmPassword.setError("Required");
            valid = false;
        } else {
            etConfirmPassword.setError(null);
        }

        return valid;
    }
}
