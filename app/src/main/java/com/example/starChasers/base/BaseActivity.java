package com.example.starChasers.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        getLayout();
        findView();
        initActivity();
    }

    protected abstract int getLayout(); //用來取得Content
    protected abstract void findView();
    protected abstract void initActivity();

    @Override
    public void onClick(View v) {

    }
}
