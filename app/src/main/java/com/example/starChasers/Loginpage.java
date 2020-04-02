package com.example.starChasers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.manager.Manager_main;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.google.gson.JsonObject;


public class Loginpage extends BaseActivity {
    private CommonTask isMemberTask;
    private TextView btnlogin,btnsign,tvFYP,etUserEmail,etPassword;
    private CheckBox cbRemember;
    private static final String TAG = "Loginpage";

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void findView() {
        btnlogin = findViewById(R.id.btnlogin);
        etUserEmail = findViewById(R.id.etUserEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnsign = findViewById(R.id.btnsign);
        tvFYP = findViewById(R.id.tvFYP);

//        tvFYP.setVisibility(View.GONE);//先將註冊按鈕及忘記密碼隱藏，若之後完成再解除
        btnsign.setVisibility(View.INVISIBLE);//先將註冊按鈕及忘記密碼隱藏，若之後完成再解除

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUserEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (user.length() <= 0 || password.length() <= 0) {
                    Util.showToast(Loginpage.this, "請輸入帳號及密碼");
                    return;
                }
                if (isUser(user, password)) {
                    //比對登錄帳號密碼後
                    //1.登錄資料紀錄進偏好設定檔
                    SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                    if (cbRemember.isChecked()) {
                        pref.edit().putBoolean("login", true).
                                putString("user", user).
                                putString("password", password).
                                apply();
                        setResult(RESULT_OK);
                    } else {
                        pref.edit().
                                putString("user", user).
                                putString("password", password).
                                apply();
                        setResult(RESULT_OK);
                    }
                    //2.切換到主頁面
                    Intent intent = new Intent();
                    intent.setClass(Loginpage.this, MainActivity.class);
                    startActivity(intent);
                    Loginpage.this.finish();
                } else if (isMaster(user,password)){
                    SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                    if (cbRemember.isChecked()) {
                        pref.edit().putBoolean("login", true).
                                putString("user", user).
                                putString("password", password).
                                apply();
                        setResult(RESULT_OK);
                    } else {
                        pref.edit().
                                putString("user", user).
                                putString("password", password).
                                apply();
                        setResult(RESULT_OK);
                    }
                    Intent intent = new Intent();
                    intent.setClass(Loginpage.this, Manager_main.class);
                    startActivity(intent);
                    Loginpage.this.finish();
                } else if (Util.confail){
                    Util.showToast(Loginpage.this,"連線逾時");
                    Util.confail=false;
                } else {
//                    Util.showToast(Loginpage.this, "無效的連線");
                    Util.showToast(Loginpage.this, "無效的帳號或密碼");
                }
            }
        });


        tvFYP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Loginpage.this, MainActivity.class);
                startActivity(intent);
                Loginpage.this.finish();
            }
        });

    }

    @Override
    public void initActivity() {
        setResult(RESULT_CANCELED);
    }


    protected void onStart() {
        //在onStart()中檢查偏好設定檔中的登錄狀態
        super.onStart();
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            String name = pref.getString("user", "");
            String password = pref.getString("password", "");
            if (isUser(name, password)) {
                setResult(RESULT_OK);
                Intent intent = new Intent();
                intent.setClass(Loginpage.this, MainActivity.class);
                startActivity(intent);
//                Loginpage.this.finish();
                System.exit(0);
            } else {
            }
        }
    }

    private boolean isUser(String mem_Email, String password) {
        //比對資料庫會員帳號密碼

        boolean isUser = false;

        if (Util.networkConnected(this)) {
            //指定連接的Servlet
//            String url = Util.WifiURL+"MemberServlet";
            String url = Util.LocalHostURL + "MemberServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("mem_Email", mem_Email);
            jsonObject.addProperty("password", password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();
                isUser = Boolean.valueOf(result);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isUser = false;
            }
        } else {
            Util.showToast(this, getString(R.string.unconnected));
        }
        return isUser;
//        return mem_No.equals("abc");
    }

    private boolean isMaster(String wm_Email, String password) {
        //比對資料庫網站管理員帳號密碼

        boolean isMaster = false;

        if (Util.networkConnected(this)) {
            String url = Util.LocalHostURL + "WmMasterServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMaster");
            jsonObject.addProperty("mem_Email", wm_Email);
            jsonObject.addProperty("password", password);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);
            try {
                String result = isMemberTask.execute().get();
                isMaster = Boolean.valueOf(result);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                isMaster = false;
            }
        } else {
            Util.showToast(this, getString(R.string.unconnected));
        }
        return isMaster;

    }


    protected void onStop() {
        super.onStop();
        if (isMemberTask != null) {
            isMemberTask.cancel(true);
        }
    }

}
