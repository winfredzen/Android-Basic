package com.example.okhttpdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    public String url = "https://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);

    }


    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url(strings[0]).build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mTextView.setText(s);
        }
    }

}
