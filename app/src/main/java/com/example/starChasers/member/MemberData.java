package com.example.starChasers.member;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.starChasers.R;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.google.gson.JsonObject;

public class MemberData extends BaseActivity {

    private static final String TAG = "MemberData";
    private CommonTask getMemberDataByEmail;
    private String mem_Email;
    private SCMemberVO memberData;
    private TextView memd_No,
                    memd_Email,
                    memd_Name,
                    memd_Phone,
                    memd_Sex,
                    mem_Point,
                    mem_Birthday;
    private Button memd_btnEdit;
    private LinearLayout editMemDataLayout;
    private EditText memd_edName,
                     memd_edPhone,
                     memd_edPSW;
    private Button memd_Submit;
    private Bundle bundle ;

    @Override
    protected int getLayout() {
        return R.layout.activity_member_data;
    }

    @Override
    protected void findView(){
        mem_Birthday = findViewById(R.id.memd_Birthday);
        memd_No = findViewById(R.id.memd_No);
        memd_Email = findViewById(R.id.memd_Email);
        memd_Name = findViewById(R.id.memd_Name);
        memd_Phone = findViewById(R.id.memd_Phone);
        memd_Sex = findViewById(R.id.memd_Sex);
        mem_Point = findViewById(R.id.memd_Point);
        memd_btnEdit = findViewById(R.id.memd_btnEdit);
        memd_Submit = findViewById(R.id.memd_Submit);
        bundle = getIntent().getExtras();
    }

    @Override
    protected void initActivity() {
        getdata();
        editMemData();
    }

    private void getdata(){
        memd_No.append(bundle.getString("Mem_No"));
        memd_Email.append(bundle.getString("Mem_Email"));
        memd_Name.append(bundle.getString("Mem_Name"));
        mem_Birthday.append(bundle.getString("Mem_Birthday"));
        memd_Phone.append(bundle.getString("Mem_Phone"));
        memd_Sex.append(String.valueOf(bundle.getString("Mem_Sex")));
        mem_Point.append(String.valueOf(bundle.getInt("Mem_Point")));
    }
    private void editMemData() {
        memd_btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMemDataLayout = findViewById(R.id.editMemDataLayout);
                memd_edName = findViewById(R.id.memd_edName);
                memd_edPhone = findViewById(R.id.memd_edPhone);
                memd_edPSW = findViewById(R.id.memd_edPSW);
                editMemDataLayout.setVisibility(View.VISIBLE);
                memd_edName.setText(bundle.getString("Mem_Name"));
                memd_edPhone.setText(bundle.getString("Mem_Phone"));
                memd_edPSW.setText(bundle.getString("Mem_Psw"));
            }
        });
        memd_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mem_No = bundle.getString("Mem_No"); //取得會員編號作為Server的索引
                String mem_Name = memd_edName.getText().toString(); //取得輸入的資料
                String mem_Phone = memd_edPhone.getText().toString();
                String mem_PSW = memd_edPSW.getText().toString();
                String url = Util.LocalHostURL + "MemberServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "updateForMember");
                jsonObject.addProperty("mem_No", mem_No);
                if (mem_Name.trim().length()<=0 || mem_Name == null) {
                    Util.showToast(MemberData.this,"暱稱不得空白");
                }else if (mem_Phone.trim().length()<10 || mem_Phone == null || mem_Phone.trim().length()>10){
                    Util.showToast(MemberData.this,"電話長度不對");
                }else if (mem_PSW.trim().length()<=0 || mem_PSW == null){
                    Util.showToast(MemberData.this,"密碼不得空白");
                }else {
                    jsonObject.addProperty("mem_Name", mem_Name);
                    jsonObject.addProperty("mem_Phone", mem_Phone);//此處傳入的是區域變數
                    jsonObject.addProperty("mem_PSW", mem_PSW);
                    String jsonOut = jsonObject.toString();
                    String jsonRes = null;
                    getMemberDataByEmail = new CommonTask(url, jsonOut);
                    try {
                        jsonRes = getMemberDataByEmail.execute().get();
                    } catch (Exception e) {
                        Log.e("MemberData", "getMemberDataByEmail : " + e.toString());
                    }
                    Util.showToast(MemberData.this, jsonRes);
                    System.exit(0);
                }
            }
        });
    }


}
