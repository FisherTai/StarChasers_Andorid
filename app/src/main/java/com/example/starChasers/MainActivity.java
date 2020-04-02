package com.example.starChasers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.starChasers.Spot.SpotFragment;
import com.example.starChasers.Spot.SpotFragmentMyCollention;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.live.LiveWeb;
import com.example.starChasers.member.MemberFragment;
import com.example.starChasers.member.SCMemberVO;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.tourgroup.TourGroupCreate;
import com.example.starChasers.tourgroup.TourGroupFragment;
import com.example.starChasers.tourgroup.TourGroupMyGroup;
import com.example.starChasers.web_master_chat.WebMasterChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    SharedPreferences pref;
    private String mem_Email;
    private CommonTask getData;
    private SCMemberVO memberData;
    private Toolbar myToolBar;
    private BottomNavigationView bnv;
    private String mem_no;

    protected void findView(){
        bnv = findViewById(R.id.bottomNavigationView);
        myToolBar = findViewById(R.id.toolbar2);
    }

    @Override
    protected void initActivity() {
        final SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        final Intent intent = new Intent();
        final Bundle bundle = new Bundle();
        getMemberData();
        Log.d("prefXpref", pref.getString("mem_No", ""));
        Log.d("mem_noXpref", mem_no);
        bundle.putSerializable("memberData", memberData);
        memberData = (SCMemberVO)bundle.getSerializable("memberData");
        //預設開啟畫面為MemberFargment，Toobar為隱藏
        MemberFragment mf = new MemberFragment();
        mf.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, mf).commit();
        myToolBar.setVisibility(View.INVISIBLE);
        bundle.putSerializable("memberData", memberData);
//設定點擊BottomNavigation切換Fragment
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.item_tab1:
                        selectedFragment = new MemberFragment();
                        selectedFragment.setArguments(bundle);
                        myToolBar.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.item_tab2:
                        selectedFragment = new TourGroupFragment();
                        myToolBar.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_tab3:
                        Intent intent = new Intent(MainActivity.this, LiveWeb.class);
                        startActivity(intent);
                        break;
                    case R.id.item_tab4:
                        selectedFragment = new SpotFragment();
                        myToolBar.setVisibility(View.VISIBLE);
                        break;
                    case R.id.item_tab5:
                        selectedFragment = new WebMasterChatFragment();
                        selectedFragment.setArguments(bundle);
                        myToolBar.setVisibility(View.VISIBLE);
                        break;
                }
                if(selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, selectedFragment).commit();
                }
                return true;
            }
        });

        //設定頂端Bar的按鈕
        setSupportActionBar(myToolBar);
        myToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String spcate_no = null;
                int tgState_no = 0;
                Fragment sf;
                switch (item.getItemId()) {
                    case R.id.tb_tgMyCreatGroup:
                        intent.setClass(MainActivity.this, TourGroupMyGroup.class);
                        bundle.putString("mem_no", mem_no);
                        bundle.putInt("isPart", 0);  //傳送0，會顯示自己創立的揪團
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.tb_tgMyAddGroup:
                        intent.setClass(MainActivity.this, TourGroupMyGroup.class);
                        bundle.putString("mem_no", mem_no);
                        bundle.putInt("isPart", 1); //傳送1，會顯示自己加入的揪團
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.tb_tgCreat:
                        intent.setClass(MainActivity.this, TourGroupCreate.class);
                        bundle.putString("mem_no", mem_no);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 31 : //用動態新增的menuItem，搜尋高山類別的地點
                        sf = new SpotFragment();
                        spcate_no = "SC0001";
                        bundle.putString("spcate_no",spcate_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 32 : //用動態新增的menuItem，搜尋平地類別的地點
                        sf = new SpotFragment();
                        spcate_no = "SC0002";
                        bundle.putString("spcate_no",spcate_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 33 : //用動態新增的menuItem，搜尋離島類別的地點
                        sf = new SpotFragment();
                        spcate_no = "SC0003";
                        bundle.putString("spcate_no",spcate_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 34: //用動態新增的menuItem，搜尋收藏的地點
                        sf = new SpotFragmentMyCollention();
                        bundle.putString("se_mem_no",mem_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 21 : //用動態新增的menuItem，搜尋開放報名的揪團
                        sf = new TourGroupFragment();
                        tgState_no = 1;
                        bundle.putInt("tgState_no",tgState_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 22 : //用動態新增的menuItem，搜尋已完成的揪團
                        sf = new TourGroupFragment();
                        tgState_no = 2 ;
                        bundle.putInt("tgState_no",tgState_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
                    case 23: //用動態新增的menuItem，搜尋被取消的揪團
                        sf = new TourGroupFragment();
                        tgState_no = 3 ;
                        bundle.putInt("tgState_no",tgState_no);
                        sf.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment_contair, sf).commit();
                        break;
//                    case R.id.spot_search:  尚未完成關鍵字查詢

                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //建立Toolbar
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //當BottomNavigation項目被切換時，變更Toolbar的menu
        switch (bnv.getSelectedItemId()) {
            case R.id.item_tab1:
                break;
            case R.id.item_tab2:
                myToolBar.setTitle("揪團");
                getMenuInflater().inflate(R.menu.tb_tgmenu, menu);
                menu.add(21,21,21,"報名中");
                menu.add(22,22,22,"已完成");
                menu.add(23,23,23,"已取消");
                break;
//            case R.id.item_tab3:
//                myToolBar.setTitle("直播");
//                getMenuInflater().inflate(R.menu.tb_livemenu, menu);
//                break;
            case R.id.item_tab4:
                myToolBar.setTitle("觀星地點");
                getMenuInflater().inflate(R.menu.tb_attramenu, menu);
                menu.add(31,31,31,"高山");
                menu.add(32,32,32,"平地");
                menu.add(33,33,33,"離島");
                menu.add(34,34,34,"我的收藏");
                break;
            case R.id.item_tab5:
                myToolBar.setTitle("即時客服");
                getMenuInflater().inflate(R.menu.tb_wmmenu, menu);
                break;
        }
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    private void getMemberData() {
        //用來取得會員在資料庫中的資料
        pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        mem_Email = pref.getString("user", ""); //獲得偏好記錄檔中的會員email
        String url = Util.LocalHostURL + "MemberServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getMemberByEmail");
        jsonObject.addProperty("mem_Email", mem_Email);
        String jsonOut = jsonObject.toString();
        getData = new CommonTask(url, jsonOut);
        memberData = null;
        try {
            String result = getData.execute().get();
            memberData = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, SCMemberVO.class);
            pref.edit().putString("mem_No", memberData.getMem_No()).apply();
            mem_no = pref.getString("mem_No", "");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
