package com.example.starChasers.tourgroup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starChasers.R;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.member.SCMemberVO;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class TourGroupReview extends BaseActivity {

    private Toolbar tGReviewToolbar;
    private String TAG ="ToruGroupReview";
    private CommonTask get_tsutext;
    private RecyclerView tour_group_review_recyclerView;
    private int search = 3 ; //預設為全部出現
    private static String posMemNo;
    private static String posTgNo;


    @Override
    protected int getLayout() {
        return R.layout.tour_group_review;
    }

    protected void findView(){
        tour_group_review_recyclerView = findViewById(R.id.tour_group_review_recyclerView);
        tGReviewToolbar =findViewById(R.id.review_toolbar);
        setSupportActionBar(tGReviewToolbar);
        tGReviewToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.tg_RNotYet:
                        search = 0;
                        updateUI();
                        break;
                    case R.id.tg_RSuccess:
                        search = 1;
                        updateUI();
                        break;
                    case R.id.tg_RFail:
                        search = 2;
                        updateUI();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void initActivity() {
        tour_group_review_recyclerView.setHasFixedSize(true);
        tour_group_review_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //建立Toolbar
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        tGReviewToolbar.setTitle("參加者清單");
        getMenuInflater().inflate(R.menu.tg_review, menu);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    private class ToruGroupReviewAdapter extends RecyclerView.Adapter<ToruGroupReviewAdapter.ViewHolder>{
        private List<TsuVO> tsuVO_List;
        private List alist;
        private List<SCMemberVO> scmlist;
//        private int imageSize;

        private ToruGroupReviewAdapter(List alist) {
            this.alist = alist;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tgcd_memname;
            private TextView tgcd_addTime;
            private TextView tgcd_state;
            private TextView tgcd_sex;
            private TextView tgcd_email;

            private ViewHolder(View view) {
                super(view);
                tgcd_memname = view.findViewById(R.id.tgcd_memname);
                tgcd_addTime = view.findViewById(R.id.tgcd_addTime);
                tgcd_state=view.findViewById(R.id.tgcd_state);
                tgcd_sex = view.findViewById(R.id.tgcd_sex);
                tgcd_email= view.findViewById(R.id.tgcd_email);
            }
        }
        @Override
        public ToruGroupReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tgreview, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ToruGroupReviewAdapter.ViewHolder holder, int position) {


//            for(TsuVO tsuVO2 : tsuVO_List){
//                if(tsuVO2.getTg_sn_state().equals("0")){
//                    tsuVO_List2.add(tsuVO2);
//                }
//                if(tsuVO2.getTg_sn_state().equals("1")){}
//                if(tsuVO2.getTg_sn_state().equals("2")){}
//            }

            final TsuVO tsuVO = tsuVO_List.get(position);
            final SCMemberVO scmVO = scmlist.get(position);
            String reviewstate = null;
            holder.tgcd_addTime.setText(tsuVO.getTg_sn_date().toString());
            holder.tgcd_memname.setText(scmVO.getMem_Name());
            if(scmVO.getMem_Sex() == 0){
                holder.tgcd_sex.setText("女");
            }else if(scmVO.getMem_Sex() == 1){
                holder.tgcd_sex.setText("男");
            }
            holder.tgcd_email.setText(scmVO.getMem_Email());
            if("0".equals(tsuVO.getTg_sn_state())){
                reviewstate = "審核中";
            }else if("1".equals(tsuVO.getTg_sn_state())){
                reviewstate = "審核通過";
            }else if("2".equals(tsuVO.getTg_sn_state())){
                reviewstate = "審核不通過";
            }
            holder.tgcd_state.setText(reviewstate);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posMemNo = tsuVO.getMem_no();
                    posTgNo =tsuVO.getTg_no();
                    AlertFragment alertFragment = new AlertFragment();
                    FragmentManager fm = getSupportFragmentManager();
                    alertFragment.show(fm, "alert");
                }
            });
        }

        @Override
        public int getItemCount() {
            //因為list外層只有固定2個元素，需使用內部的list
            //因內部仍為Gson物件，需再個別轉型
            Type listType = new TypeToken<List<TsuVO>>() {}.getType();
            Type listType2 = new TypeToken<List<SCMemberVO>>() {}.getType();
            String s_tsuVO_List = new Gson().toJson(alist.get(0));
            String s_scmlist =  new Gson().toJson(alist.get(1));
            tsuVO_List  = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(s_tsuVO_List, listType);
            scmlist =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(s_scmlist, listType2);
            return tsuVO_List.size();
        }
    }
        protected void updateUI(){
            if (Util.networkConnected(this)) {
                Bundle bundle = getIntent().getExtras();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getListByTgno");
                jsonObject.addProperty("tg_no",bundle.getString("tg_no")); //取得從TourGroupDetail傳來的資料
                jsonObject.addProperty("search_state",search);
                String jsonOut = jsonObject.toString();
                get_tsutext = new CommonTask(Util.LocalHostURL + "TourGroupSignUpServlert", jsonOut);
                List alist = null; //
                try {
                    String result = get_tsutext.execute().get();
                    Type listType = new TypeToken<List>() {}.getType();
                    alist = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, listType);
                    Log.d("XXX",alist.toString());
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (alist == null || alist.isEmpty()) {
                    Util.showToast(this, "沒有參加者");
                } else {
                    tour_group_review_recyclerView.setAdapter(new ToruGroupReviewAdapter(alist));
                }
            } else {
                Util.showToast(this, "沒有連線");
            }
        }
//下面是跳窗類別
    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    //設定圖示
//                    .setIcon(R.drawable.alert)
                    .setTitle("審核")
                    //設定訊息內容
                    .setMessage("是否讓這位會員加入")
                    .setNeutralButton("下次再說",this)
                    .setPositiveButton("通過", this)
                    .setNegativeButton("不通過", this)
                    .create();
            return alertDialog;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String state = null;
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    state = "1";
                    dialog.cancel();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    state = "2";
                    dialog.cancel();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
            if (state != null) {
                Bundle bundle =new Bundle();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "review");
            jsonObject.addProperty("tg_state",state);
            jsonObject.addProperty("tg_no",posTgNo);
            jsonObject.addProperty("mem_no",posMemNo);
            String jsonOut = jsonObject.toString();
            CommonTask get_tsutext = new CommonTask(Util.LocalHostURL + "TourGroupSignUpServlert", jsonOut);
            try {
                String result = get_tsutext.execute().get();
                Util.showToast(getActivity(),result);
            }catch (Exception e){
                Log.e("Review", e.toString());
            }
            }
        }
    }

}
