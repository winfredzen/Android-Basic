package com.example.eventbus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.eventbus.event.FailureEvent;
import com.example.eventbus.event.MainEvent;
import com.example.eventbus.event.MainOrderEvent;
import com.example.eventbus.event.PostingEvent;
import com.example.eventbus.event.StickyMessageEvent;
import com.example.eventbus.event.SuccesssEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity  {

    public static final String HANDLE_EVENT_ACTION = "handle_event_action";

    public static final String STATUS_KEY = "status";

    private static final String TAG = "MainActivity";

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取action
            final String action = intent.getAction();
            if (HANDLE_EVENT_ACTION.equals(action)) {
                final boolean status = intent.getBooleanExtra(STATUS_KEY, false);
                if (status) {
                    setImageSrc(R.drawable.ic_happy);
                } else {
                    setImageSrc(R.drawable.ic_sad);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //注册
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消注册
        EventBus.getDefault().unregister(this);
    }

    //事件
    @Subscribe
    public void onSuccessEvent(SuccesssEvent event) {
        setImageSrc(R.drawable.ic_happy);
    }

    @Subscribe
    public void onFailureEvent(FailureEvent event) {
        setImageSrc(R.drawable.ic_sad);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onPostingEvent(PostingEvent event) {
        final String threadInfo = Thread.currentThread().toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setPublisherThreadInfo(event.threadInfo);
                setSubscriberThreadInfo(threadInfo);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(MainEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMainOrderEvent(MainOrderEvent event) {
        Log.d(TAG, "onMainOrderedEvent: enter @" + SystemClock.uptimeMillis()); //开机时间
        setPublisherThreadInfo(event.threadInfo);
        setSubscriberThreadInfo(Thread.currentThread().toString());
        Log.d(TAG, "onMainOrderedEvent: exit @" + SystemClock.uptimeMillis()); //开机时间
    }

    private void setPublisherThreadInfo(String threadInfo) {
        setTextView(R.id.publisherThreadTextView, threadInfo);
    }

    private void setSubscriberThreadInfo(String threadInfo) {
        setTextView(R.id.subscriberThreadTextView, threadInfo);
    }

    //应该在主线程中运行
    private void setTextView(int resId, String text) {
        final TextView textView = (TextView) findViewById(resId);
        textView.setText(text);
        textView.setAlpha(0.5f);
        textView.animate().alpha(1).start(); //动画
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("SubScriber");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                final PublisherDialogFragment fragment = new PublisherDialogFragment();
                fragment.setEventListener(new PublisherDialogFragment.OnEventListener() {
                    @Override
                    public void onSuccess() {
                        setImageSrc(R.drawable.ic_happy);
                    }

                    @Override
                    public void onFailure() {
                        setImageSrc(R.drawable.ic_sad);
                    }
                });
                fragment.show(getSupportFragmentManager(), "publiser");
            }
        });
    }

    //设置图片
    private void setImageSrc(int resId) {
        final ImageView imageView = (ImageView)findViewById(R.id.emotionImageView);
        imageView.setImageResource(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //启动另一个Activity
            Intent intent = new Intent(this, StickyActivity.class);

            //先发布
            EventBus.getDefault().postSticky(new StickyMessageEvent("stikcy message"));

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}