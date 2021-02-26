package com.example.loadimagedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView mIv;

    private Handler hander = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 200:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    mIv.setImageBitmap(bitmap);
                    break;
                default:
                    mIv.setImageResource(R.mipmap.loader_error);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.iv);
    }

    public void onLoadImageClick(View view) {
//        loadUrlImage("http://img4.mukewang.com/6013c1a9092857c002720144.png");
//        loadUrlImage("http://res.lgdsunday.club/big_img.jpg");

//        glideloadImage("http://res.lgdsunday.club/big_img.jpg");
        glideAppLoadUrlImage("http://res.lgdsunday.club/big_img.jpg");
    }


    private void glideloadImage(String img) {

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.loader_error)
                .circleCrop();

        Glide.with(this)
                .load(img)
                .apply(options)
                .into(mIv);
    }

    /**
     * 加载网络图片
     * @param img
     */
    private void loadUrlImage(String img) {

        mIv.setImageResource(R.mipmap.loading);

        new Thread() {
            @Override
            public void run() {
                super.run();

                Message message = new Message();

                try {
                    URL url = new URL(img);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    int code = httpURLConnection.getResponseCode();
                    if (code == 200) {
                        //获取数据流
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        message.obj = bitmap;
                        message.what = 200;

                    } else {
                        message.what = code;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what = -1;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what = -1;
                } finally {
                    hander.sendMessage(message); //发送消息
                }

            }
        }.start();
    }

    private void glideAppLoadUrlImage(String url) {
        GlideApp.with(this)
                .load(url)
                .injectOptions()
                .into(mIv);
    }

}