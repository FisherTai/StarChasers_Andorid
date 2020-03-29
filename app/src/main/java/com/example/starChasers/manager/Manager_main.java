package com.example.starChasers.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.starChasers.Loginpage;
import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Manager_main extends AppCompatActivity {
    final private String TAG ="Manager_main";
    private Button      btnlogout;
    private ImageView   ivwm_img;
    private ImageView   ivmasScanner;
    private ImageView   master_chat;
    private TextView    tvwm_name;
    private TextView    tvwm_no;
    private TextView    tvwm_obd;
    private TextView    tvwm_email;
    private String      master_Email;
    private WMVO        masterData;
    private int imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_master_activity);
        findView();
        getMasterData();
        updateUI();
    }

    protected void findView() {
        btnlogout=findViewById(R.id.btnlogout);
        ivwm_img=findViewById(R.id.wm_img);
        ivmasScanner=findViewById(R.id.masScanner);
        tvwm_name=findViewById(R.id.wm_name);
        tvwm_no=findViewById(R.id.wm_no);
        tvwm_obd=findViewById(R.id.wm_obd);
        tvwm_email=findViewById(R.id.wm_email);
        master_chat = findViewById(R.id.master_chat);

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                pref.edit().putBoolean("login", false).apply();
                Intent intent = new Intent(Manager_main.this, Loginpage.class);
                startActivityForResult(intent, 1);
                finish();
            }
        });
        ivmasScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Manager_main.this, Manager_rantorder.class);
                startActivity(intent);
            }
        });
        master_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Manager_main.this,ManagerChatListActivity.class);
                intent.putExtra("wm_no",masterData.getWmno());
                intent.putExtra("wm_name",masterData.getWmname());
                startActivity(intent);
            }
        });

    }

    private void updateUI() {
        //顯示會員大頭貼及會員名稱
//        if (masterData.getWmimg() != null) {
//            Bitmap bitmap = BitmapFactory.decodeByteArray(masterData.getWmimg(), 0, masterData.getWmimg().length);
//            ivwm_img.setImageBitmap(bitmap);
        String url = Util.LocalHostURL + "WmMasterServlet";
        imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        ImageTask mem_imageTask = new ImageTask(url, masterData.getWmno(), imageSize, ivwm_img);
        mem_imageTask.execute();
//        } else {
//            ivwm_img.setImageResource(R.drawable.membericon);
//        }
        tvwm_name.setText(masterData.getWmname());
        tvwm_no.setText(masterData.getWmno());
        tvwm_obd.setText(masterData.getWmobd().toString());
        tvwm_email.setText(masterData.getWmemail());
    }

    private void getMasterData() {
        //用來取得會員在資料庫中的資料
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        master_Email = pref.getString("user", ""); //獲得偏好記錄檔中的會員email
        String url = Util.LocalHostURL + "WmMasterServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getMasterByEmail");
        jsonObject.addProperty("wm_email", master_Email);
        String jsonOut = jsonObject.toString();
        CommonTask getData = new CommonTask(url, jsonOut);
        masterData = null;
        try {
            String result = getData.execute().get();
            masterData = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, WMVO.class);
            pref.edit().putString("wm_no", masterData.getWmno()).apply();
            Log.d("SharedPreferences","wm_no="+masterData.getWmno());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
