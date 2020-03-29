package com.example.starChasers;

import android.content.Intent;
import android.os.Handler;

import com.example.starChasers.base.BaseActivity;

public class WelcomeActivity extends BaseActivity  {

    private static int SPLASH_TIME_OUT = 2000;
    private static final int REQ_LOGIN = 1;

    @Override
    public void getLayout() {
        setContentView(R.layout.activity_welcome);
    }

    @Override
    public void findView() {
    }

    @Override
    public void initActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(WelcomeActivity.this, Loginpage.class); //MainActivity為主要檔案名稱
                WelcomeActivity.this.startActivityForResult(intent,REQ_LOGIN);

                // close this activity
                WelcomeActivity.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

