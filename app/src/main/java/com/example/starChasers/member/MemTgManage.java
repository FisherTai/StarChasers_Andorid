package com.example.starChasers.member;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.starChasers.R;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.tourgroup.TourGroupMyGroup;

public class MemTgManage extends BaseActivity {

    private Button memt_tgPart,memt_tgMaster;
    private String mem_no;

    @Override
    protected int getLayout() {
        return R.layout.activity_mem_tg_manage;
    }

    protected void findView(){
        memt_tgPart =findViewById(R.id.memt_tgPart);
        memt_tgMaster=findViewById(R.id.memt_tgMaster);
        SharedPreferences pref  = getSharedPreferences(Util.PREF_FILE,MODE_PRIVATE);
        mem_no = pref.getString("mem_No","");


        memt_tgPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(MemTgManage.this, TourGroupMyGroup.class);
                Bundle bundle = new Bundle();
                bundle.putString("mem_no",mem_no);
                bundle.putInt("isPart",1) ;
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        memt_tgMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(MemTgManage.this, TourGroupMyGroup.class);
                Bundle bundle = new Bundle();
                bundle.putString("mem_no",mem_no);
                bundle.putInt("isPart",0) ;
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initActivity() {

    }

}
