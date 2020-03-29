package com.example.starChasers.member;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.rent.RentOrder_VO;
import com.example.starChasers.task.CommonTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MemberRentorder extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_rentorder_activity);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.gank_swipe_refresh_layout);
        final RecyclerView rentorder_recyclerView = findViewById(R.id.rentorder_recyclerView);
        rentorder_recyclerView.setHasFixedSize(true);
        rentorder_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rentorder_recyclerView.setAdapter(new MemberRentorderAdapter(getRoList()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rentorder_recyclerView.setAdapter(new MemberRentorderAdapter(getRoList()));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private class MemberRentorderAdapter extends RecyclerView.Adapter<MemberRentorderAdapter.ViewHolder> {
        private List<com.example.starChasers.rent.RentOrder_VO> roList;

        private MemberRentorderAdapter(List<com.example.starChasers.rent.RentOrder_VO> roList) {
            this.roList = roList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView me_rd_no;
            private TextView me_erg_no;
            private TextView me_rd_startdate;
            private TextView me_rd_enddate;
            private TextView me_rd_day;
            private TextView me_rd_deposit;
            private TextView me_rd_money;
            private TextView me_rd_nowstate;
            private Button btn_cancel;

            private ViewHolder(View view) {
                super(view);
                me_rd_no = view.findViewById(R.id.me_rd_no);
                me_erg_no = view.findViewById(R.id.me_erg_no);
                me_rd_startdate = view.findViewById(R.id.me_rd_startdate);
                me_rd_enddate = view.findViewById(R.id.me_rd_enddate);
                me_rd_day = view.findViewById(R.id.me_rd_day);
                me_rd_deposit = view.findViewById(R.id.me_rd_deposit);
                me_rd_money = view.findViewById(R.id.me_rd_money);
                me_rd_nowstate = view.findViewById(R.id.me_rd_nowstate);
                btn_cancel =view.findViewById(R.id.btn_cancel);
            }
        }

        @NonNull
        @Override
        public MemberRentorderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_rendorder, parent, false);
            return new MemberRentorderAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MemberRentorderAdapter.ViewHolder holder, int position) {
            final RentOrder_VO RentOrder_VO = roList.get(position);

            try {
            holder.me_rd_no.setText(RentOrder_VO.getRd_no());
            holder.me_erg_no.setText(RentOrder_VO.getErg_no());
            holder.me_rd_startdate.setText(RentOrder_VO.getRd_startdate().toString());
            holder.me_rd_enddate.setText(RentOrder_VO.getRd_enddate().toString());
            holder.me_rd_day.setText(RentOrder_VO.getRd_day().toString());
            holder.me_rd_deposit.setText(RentOrder_VO.getRd_deposit().toString());
            holder.me_rd_money.setText(RentOrder_VO.getRd_money().toString());

            switch (RentOrder_VO.getRd_state()){
                case "0":
                    holder.me_rd_nowstate.setText("預約中");
                    break;
                case "1":
                    holder.me_rd_nowstate.setText("出租中");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
                case "2":
                    holder.me_rd_nowstate.setText("已歸還");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
                case "3":
                    holder.me_rd_nowstate.setText("延遲歸還");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
                case "4":
                    holder.me_rd_nowstate.setText("商品損壞");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
                case "5":
                    holder.me_rd_nowstate.setText("訂單取消");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
                case "6":
                    holder.me_rd_nowstate.setText("遺失");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                    break;
            }


            if(!RentOrder_VO.getRd_state().equals("0")){
                Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
            }

            }catch (NullPointerException e){
                Util.showToast(MemberRentorder.this,"資料錯誤:"+e.toString());
            }

            holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action","changeState");
                    jsonObject.addProperty("rd_no",RentOrder_VO.getRd_no());
                    jsonObject.addProperty("rd_state","5");
                    String jsonOut =jsonObject.toString();
                    CommonTask commonTask = new CommonTask(Util.LocalHostURL+"RentOrderServlet",jsonOut);
                    String jsonRes = null;
                    try {
                        jsonRes = commonTask.execute().get();
                        Util.showToast(MemberRentorder.this,jsonRes);
                    }catch (Exception e){
                        Log.e("delete",e.toString());
                    }
                    holder.me_rd_nowstate.setText("訂單取消");
                    Util.setButtonState( holder.btn_cancel,false, getDrawable(R.drawable.bg_btn_gray));
                }
            });
        }
        @Override
        public int getItemCount() {
            return roList.size();
        }
    }
    private  List<RentOrder_VO> getRoList(){
        JsonObject jsonObject = new JsonObject();
        final SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        jsonObject.addProperty("mem_no",pref.getString("mem_No",""));
        jsonObject.addProperty("action","getAllNo");
        String jsonOut = jsonObject.toString();
        String jsonRes = null;
        try {
            CommonTask commonTask = new CommonTask(Util.LocalHostURL+"RentOrderServlet",jsonOut);
            jsonRes = commonTask.execute().get();
        }catch (Exception e){
            Log.e("MemberRentorder",e.toString());
        }
        final  Type listType = new TypeToken<List<com.example.starChasers.rent.RentOrder_VO>>() {}.getType();
        return new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonRes, listType);
    }

}
