package com.stemi.stemiapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AppConstants {
    private static final String TAG = "SignupResponse";
    ApiInterface apiInterface;
    AppSharedPreference appSharedPreferences;

    @BindView(R.id.et_email_login)
    EditText etEmail;
    @BindView(R.id.et_password_login)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreferences = new AppSharedPreference(this);

        if(appSharedPreferences.getUserToken(USER_TOKEN) != null){
            startActivity(new Intent(MainActivity.this,TrackActivity.class));
            finish();
        }else {
            Log.e(TAG, "onCreate: TOKEN IS NULL" );
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSignUp();
            }
        });

        /**
         GET List Resources
         **/

    }


    public void callSignUp(){
        Call<SignUpResponseModel> call = apiInterface.callLoginApi(etEmail.getText().toString(),etPassword.getText().toString());
        call.enqueue(new Callback<SignUpResponseModel>() {
            @Override
            public void onResponse(Call<SignUpResponseModel> call, Response<SignUpResponseModel> response) {
                if (response.isSuccessful()) {
                    SignUpResponseModel signUpResponseModel = response.body();
                    appSharedPreferences.addUserToken(USER_TOKEN, "" + signUpResponseModel.getToken());
                    Log.e(TAG, "onResponse: Signup Response" + signUpResponseModel);
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,TrackActivity.class));
                }
            }

            @Override
            public void onFailure(Call<SignUpResponseModel> call, Throwable t) {

            }
        });
    }
}
