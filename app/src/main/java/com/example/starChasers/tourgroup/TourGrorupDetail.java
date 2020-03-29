package com.example.starChasers.tourgroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

public class TourGrorupDetail extends AppCompatActivity {
//tgd =Tour Grorup Detail
    private static final String TAG = "TourGrorupDetail";
    private CommonTask getTGDData;
    private ImageTask getTGDImg;
    private ImageView tgd_image;
    private TextView tgd_tgname;
    private TextView tgd_tgAcDate;
    private TextView tgd_tgEndDate;
    private TextView tgd_tgSignEnd;
    private TextView tgd_tgAddress;
    private TextView tgd_tgCondition;
    private TextView tgd_tgContent;
    private TextView tgd_tgNumLimit;
    private TextView tgd_tgNum;
    private Button tgd_btnSign;
    private Bundle bundle ;
    private int imageSize;
    private String mem_no;
    private Date date = new Date();
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.tour_group_detail_activity);

        //-----------------------------------------設定元件-----------------------------------
        findView();
        //---------------------------取出bundle資料並放入對應的元件中顯示-------------------
        bundle = getIntent().getExtras();//取得從TourGroupFragment送來的bundle
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        mem_no = pref.getString("mem_No","");
//        mem_no = bundle.getString("mem_no");
        String tg_numlimit = bundle.getString("tg_numlimit"), tg_num =bundle.getString("tg_num");
        tgd_tgname.append(bundle.getString("tg_name"));
        tgd_tgAcDate.append(bundle.getString("tg_activitydate"));
        tgd_tgSignEnd.append(bundle.getString("tg_signupend"));
        tgd_tgEndDate.append(bundle.getString("tg_enddate"));
        tgd_tgAddress.append(bundle.getString("tg_address"));
        tgd_tgCondition.append(bundle.getString("tg_condition"));
        tgd_tgContent.append(bundle.getString("tg_content"));
        tgd_tgNumLimit.append(tg_numlimit);
        tgd_tgNum.append(tg_num);
        //此處判斷按鈕能不能被點擊
        if( (bundle.getInt("tg_state")) != 1){
            tgd_btnSign.setBackground(getResources().getDrawable(R.drawable.bg_btn_gray));
            tgd_btnSign.setEnabled(false);
        }else if(date.getTime()> java.sql.Date.valueOf((bundle.getString("tg_signupend"))).getTime()){
            Util.setButtonState(tgd_btnSign,false,"報名截止",getResources().getDrawable(R.drawable.bg_btn_gray));
        }else if(mem_no.equals(bundle.getString("mem_no"))){
            tgd_btnSign.setText("審核報名");
        }else if (checkisMemExist()[0]&& checkisMemExist()[1] && checkisMemExist()[2]){
            Util.setButtonState(tgd_btnSign,false,"審核通過",getResources().getDrawable(R.drawable.bg_btn_gray));
        }else if (checkisMemExist()[0]&& checkisMemExist()[1]){
            Util.setButtonState(tgd_btnSign,false,"審核不通過",getResources().getDrawable(R.drawable.bg_btn_gray));
        }else if (checkisMemExist()[0]){
            Util.setButtonState(tgd_btnSign,false,"審核中",getResources().getDrawable(R.drawable.bg_btn_gray));
        }else if(tg_numlimit.equals(tg_num)){
            Util.setButtonState(tgd_btnSign,false,"人數已滿",getResources().getDrawable(R.drawable.bg_btn_gray));
        }
        getTGDData(); //取得揪團圖片
        signUpTG(); //報名按鈕
    }
    private void findView() {
        tgd_image = findViewById(R.id.tgd_image);
        tgd_tgname = findViewById(R.id.tgd_tgname);
        tgd_tgAcDate = findViewById(R.id.tgd_tgAcDate);
        tgd_tgEndDate = findViewById(R.id.tgd_tgEndDate);
        tgd_tgSignEnd = findViewById(R.id.tgd_tgSignEnd);
        tgd_tgAddress = findViewById(R.id.tgd_tgAddress);
        tgd_tgCondition = findViewById(R.id.tgd_tgCondition);
        tgd_tgContent = findViewById(R.id.tgd_tgContent);
        tgd_tgNumLimit = findViewById(R.id.tgd_tgNumLimit);
        tgd_tgNum = findViewById(R.id.tgd_tgNum);
        tgd_btnSign = findViewById(R.id.tgd_btnSign);
    }
    protected void getTGDData(){
        if (Util.networkConnected(this)) {
            String url = Util.LocalHostURL + "TourGroupServlet";
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            getTGDImg = new ImageTask(url, bundle.getString("tg_no"), imageSize, tgd_image);
            getTGDImg.execute();
        } else {
            Util.showToast(this, getString(R.string.unconnected));
        }
    }
    protected void signUpTG(){
        //團主身分和非團主身分的按鈕作用不同
        if(mem_no.equals(bundle.getString("mem_no"))){
            tgd_btnSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TourGrorupDetail.this,TourGroupReview.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("tg_no",bundle.getString("tg_no"));
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });
        }else{
            tgd_btnSign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsonObject jsonObject= new JsonObject();
                    String url = Util.LocalHostURL + "TourGroupSignUpServlert";
                    jsonObject.addProperty("action","addTsu");
                    jsonObject.addProperty("tg_no",bundle.getString("tg_no"));
                    jsonObject.addProperty("mem_no",mem_no);
//                    jsonObject.addProperty("tg_sn_state","0");
                    String jsonOut = jsonObject.toString();
                    String result =null;
                    getTGDData =null;
                    getTGDData = new CommonTask(url,jsonOut);
                    try {
                        result = getTGDData.execute().get();
                    }catch (Exception e){
                        Log.e(TAG,e.toString());
                    }
                    tgd_btnSign.setBackground(getResources().getDrawable(R.drawable.bg_btn_gray));
                    tgd_btnSign.setEnabled(false);
                    if("Full".equals(result)){
                        tgd_btnSign.setText("已額滿");
                        Util.showToast(TourGrorupDetail.this, result);
                    }else if ("Time of Tour Is Repeat".equals(result)){
                        Util.showToast(TourGrorupDetail.this, result);
                    }else {
                        tgd_btnSign.setText("已申請");
                        Util.showToast(TourGrorupDetail.this, result);
                    }
                }
            });
        }
    }
    protected boolean[] checkisMemExist(){ //回傳boolean[0]為是否已經提出申請，boolean[1]為是否已審核，boolean[0]，為是否通過
        final SharedPreferences pref =getSharedPreferences(Util.PREF_FILE,MODE_PRIVATE);
        mem_no = pref.getString("mem_No",""); //這是目前登錄的會員編號
        boolean[] signAndreview = null;
        String url = Util.LocalHostURL + "TourGroupSignUpServlert";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action","isMemExistAndIsSuccessReview");
        jsonObject.addProperty("tg_no",bundle.getString("tg_no"));
        jsonObject.addProperty("mem_no",mem_no);
        String jsonOut = jsonObject.toString();
        getTGDData =null;
        getTGDData = new CommonTask(url,jsonOut);
        try {
            String result = getTGDData.execute().get();
            signAndreview = new Gson().fromJson(result,boolean[].class);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
            return signAndreview;
    }

//    protected boolean checkTourGroupTime(){
//        boolean checkSuccess = false;
//        String url = Util.LocalHostURL + "TourGroupSignUpServlert";
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action","checkTourGroupTime");
//        jsonObject.addProperty("tg_no",bundle.getString("tg_no"));
//        jsonObject.addProperty("mem_no",mem_no);
//        String jsonOut = jsonObject.toString();
//        getTGDData =null;
//        getTGDData = new CommonTask(url,jsonOut);
//        try {
//            String result = getTGDData.execute().get();
//            checkSuccess = new Gson().fromJson(result,boolean.class);
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//
//
//
//        return checkSuccess;
//    }

}
