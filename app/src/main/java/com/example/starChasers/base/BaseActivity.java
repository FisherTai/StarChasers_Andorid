package com.example.starChasers.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayout();
        findView();
        initActivity();
    }

    public abstract void getLayout();
    public abstract void findView();
    public abstract void initActivity();

    @Override
    public void onClick(View v) {

    }
}
