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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TourGroupFragment extends Fragment {
    private static final String TAG = "TourGroupFargment";
    private ImageTask tg_imageTask;
    private CommonTask get_tgtext;
    RecyclerView tour_group_recyclerView;

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tour_group_fragment, container, false);
        tour_group_recyclerView = view.findViewById(R.id.tour_group_recyclerView);
        tour_group_recyclerView.setHasFixedSize(true);
        tour_group_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        updateUI();
        return view;
    }

    private class TourGroupAdapter extends RecyclerView.Adapter<TourGroupAdapter.ViewHolder> {
        private List<TgVO> tG_List;
        private int imageSize;

        private TourGroupAdapter(List<TgVO> tG_List) {
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

        @NonNull
        @Override
        public TourGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_tour_group, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TourGroupAdapter.ViewHolder holder, int position) {
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
                    Intent intent =new Intent(getActivity(), TourGrorupDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("mem_no",tgVO.getMem_no());//這裡是揪團創建人會員編號
                    bundle.putString("tg_no",tgVO.getTg_no());
                    bundle.putString("tg_name",tgVO.getTg_name());
                    bundle.putString("tg_activitydate",(tgVO.getTg_activitydate()).toString());
                    bundle.putString("tg_signupend",(tgVO.getTg_signupend()).toString());
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

    @Override
    public void onStop() {
        super.onStop();
        if (get_tgtext != null) {
            get_tgtext.cancel(true);
        }

        if (tg_imageTask != null) {
            tg_imageTask.cancel(true);
        }

    }

    private void updateUI() {
        if (Util.networkConnected(getActivity())) {

            String search = "getTextOfAll";
            int state = 0;
            if( getArguments() != null){
                state = getArguments().getInt("tgState_no"); //依據MainActivity的Item項目傳來的值決定篩選的資料
                search = "search";
            }


            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", search);
            jsonObject.addProperty("state", state);
            String jsonOut = jsonObject.toString();
            get_tgtext = new CommonTask(Util.LocalHostURL + "TourGroupServlet", jsonOut);
            List<TgVO> tgList = null;
            try {
                String result = get_tgtext.execute().get();
                Type listType = new TypeToken<List<TgVO>>() {}.getType();
                tgList = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (tgList == null || tgList.isEmpty()) {
                Util.showToast(getActivity(), "找不到揪團");
            } else {
                tour_group_recyclerView.setAdapter(new TourGroupAdapter(tgList));
            }
        } else {
            Util.showToast(getActivity(), "沒有連線");
        }

    }

}