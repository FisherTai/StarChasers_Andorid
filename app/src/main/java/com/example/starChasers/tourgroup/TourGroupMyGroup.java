package com.example.starChasers.tourgroup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.starChasers.R;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TourGroupMyGroup extends BaseActivity {

    private ImageTask tg_imageTask;
//    private CommonTask get_tgtext;
    private RecyclerView mytour_group_recyclerView;
    private static final String TAG = "TourGroupMyGroup";

    @Override
    protected int getLayout() {
        return R.layout.tour_group_mygroup_activity;
    }

    @Override
    protected void findView() {
        mytour_group_recyclerView = findViewById(R.id.tour_group_myrecyclerView);
    }

    @Override
    protected void initActivity() {
        mytour_group_recyclerView.setHasFixedSize(true);
        mytour_group_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        updateUI();
    }

    private class TGMyGroupAdapter extends RecyclerView.Adapter<TGMyGroupAdapter.ViewHolder>{
        private List<TgVO> tG_List;
        private int imageSize;

        private TGMyGroupAdapter(List<TgVO> tG_List) {
            this.tG_List = tG_List;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tg_name;
            private TextView tg_activitydate;
            private ImageView tg_img;
            private TextView tg_address;
            private TextView tg_num;
            private TextView tgCardState;

            private ViewHolder(View view) {
                super(view);
                tg_name = view.findViewById(R.id.tg_name);
                tg_img = view.findViewById(R.id.tg_img);
                tg_activitydate = view.findViewById(R.id.tg_activitydate);
                tg_address = view.findViewById(R.id.tg_address);
                tg_num = view.findViewById(R.id.tg_num);
                tgCardState = view.findViewById(R.id.tgCardState);
            }
        }
        @Override
        public TGMyGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tour_group, parent, false);
            return new TGMyGroupAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TGMyGroupAdapter.ViewHolder holder, int position) {
            final TgVO tgVO = tG_List.get(position);
            String url = Util.LocalHostURL + "TourGroupServlet";
            String tg_no = tgVO.getTg_no();
            String state = null;
            holder.tg_name.setText(tgVO.getTg_name());
            holder.tg_activitydate.setText((tgVO.getTg_activitydate()).toString());
            holder.tg_address.setText(tgVO.getTg_address());
            holder.tg_num.setText(tgVO.getTg_num().toString());
            switch (tgVO.getTg_state()){
                case 1:
                    state = "報名中";
                    break;
                case 2:
                    state = "揪團完成";
                    holder.tgCardState.setBackgroundColor(getResources().getColor(R.color.deep_purple_primary));
                    break;
                case 3:
                    state = "已取消";
                    holder.tgCardState.setBackgroundColor(getResources().getColor(R.color.gray_deep));
                    break;
            }

            holder.tgCardState.setText(state);
            tg_imageTask = new ImageTask(url, tg_no, imageSize, holder.tg_img);
            tg_imageTask.execute();



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(TourGroupMyGroup.this, TourGrorupDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mem_no",tgVO.getMem_no());//這裡是揪團創建人會員編號
                    bundle.putString("tg_no",tgVO.getTg_no());
                    bundle.putString("tg_name",tgVO.getTg_name());
                    bundle.putString("tg_activitydate",(tgVO.getTg_activitydate()).toString());
                    bundle.putString("tg_signupend",(tgVO.getTg_signupend()).toString());
                    bundle.putString("tg_content",tgVO.getTg_content());
                    bundle.putString("tg_numlimit",(tgVO.getTg_numlimit()).toString());
                    bundle.putString("tg_enddate",(tgVO.getTg_enddate()).toString());
                    bundle.putString("tg_num",(tgVO.getTg_num()).toString());
                    bundle.putString("tg_condition",tgVO.getTg_condition());
                    bundle.putInt("tg_state",tgVO.getTg_state());
                    bundle.putString("tg_address",tgVO.getTg_address());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tG_List.size();
        }

    }
    private void updateUI() {
        if (Util.networkConnected(TourGroupMyGroup.this)) {
            Bundle bundle = getIntent().getExtras();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getTextOfMyTG");
            if(bundle.getInt("isPart") == 1 ){
                jsonObject.addProperty("action", "getTextOfMyAddTG");
            }
            jsonObject.addProperty("mem_no",bundle.getString("mem_no"));
            String jsonOut = jsonObject.toString();
            CommonTask get_tgtext = new CommonTask(Util.LocalHostURL + "TourGroupServlet", jsonOut);
            List<TgVO> tgList = null;
            try {
                String result = get_tgtext.execute().get();
                Type listType = new TypeToken<List<TgVO>>() {}.getType();
                tgList = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (tgList == null || tgList.isEmpty()) {
                Util.showToast(TourGroupMyGroup.this, "找不到揪團");
            } else {
                mytour_group_recyclerView.setAdapter(new TGMyGroupAdapter(tgList));
            }
        } else {
            Util.showToast(TourGroupMyGroup.this, "沒有連線");
        }

    }


}
