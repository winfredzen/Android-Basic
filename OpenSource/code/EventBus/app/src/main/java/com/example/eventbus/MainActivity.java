package com.example.eventbus;

import android.media.Image;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity  {

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}