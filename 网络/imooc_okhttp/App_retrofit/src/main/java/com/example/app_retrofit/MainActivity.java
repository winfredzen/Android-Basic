package com.example.app_retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_retrofit.bean.Result;
import com.example.app_retrofit.net.retrofit.IApi;
import com.example.app_retrofit.net.retrofit.RetrofitImpl;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button mBtnGet;
    private Button mBtnPost;
    private Button mBtnPostMultiPart;
    private Button mBtnJson;
    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();

    }

    private void initViews() {
        mBtnGet = (Button) findViewById(R.id.btn_get);
        mBtnPost = (Button) findViewById(R.id.btn_post);
        mBtnPostMultiPart = (Button) findViewById(R.id.btn_post_multipart);
        mBtnJson = (Button) findViewById(R.id.btn_post_json);
        mTvContent = (TextView) findViewById(R.id.tv_content);
    }

    private void initEvents() {
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发情网络请求

                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                api.get("wz").enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {

                        Result result = response.body();
                        mTvContent.setText(result.errorCode + ", " + result.errorMsg + ", " + result.data);

                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                api.post("wz").enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        mTvContent.setText(result.errorCode + ", " + result.errorMsg + ", " + result.data);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mBtnPostMultiPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                MediaType mediaType = MediaType.get("text/plain; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, "wz");

                api.postMultiPart(requestBody).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        mTvContent.setText(result.errorCode + ", " + result.errorMsg + ", " + result.data);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mBtnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                IApi api = RetrofitImpl.getRetrofit().create(IApi.class);
                MediaType mediaType = MediaType.get("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType, "{\"name\" : \"wz\"}");

                api.postJson(requestBody).enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        Result result = response.body();
                        mTvContent.setText(result.errorCode + ", " + result.errorMsg + ", " + result.data.headers + ", " + result.data.json);
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}












