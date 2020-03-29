package com.example.starChasers.Spot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpotFragment extends Fragment {
    private static final String TAG = "SpotFragment";
    private ImageTask sp_imageTask;
    private CommonTask get_sptext;
    private RecyclerView attrac_reclerView;
    private static final int MY_REQUEST_CODE = 1;

    @Nullable
    @Override
    public void onStop() {
        super.onStop();
        if (get_sptext != null) {
            get_sptext.cancel(true);
        }
        if (sp_imageTask != null) {
            sp_imageTask.cancel(true);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spot_fragment,container,false);
        attrac_reclerView = view.findViewById(R.id.attrac_recyclerView);
        attrac_reclerView.setHasFixedSize(true);
        attrac_reclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        askPermissions();
        updateUI();
        return view;
    }


    public class SpotAdapter extends RecyclerView.Adapter< SpotAdapter.ViewHolder> {
        private List<SpotVO> spList;
        private List alist;
        private List <Spot_CategoryVO> spcList;
        private int imageSize;
        private Spot_CategoryVO spcVO =null;
        private  SpotAdapter(List alist) {
            this.alist = alist;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView spcd_name;
            private TextView spcd_cate;
            private TextView spcd_dif;
            private ImageView spcd_img;

            private ViewHolder(View view) {
                super(view);
                spcd_name = view.findViewById(R.id.spcd_name);
                spcd_cate = view.findViewById(R.id.spcd_cate);
                spcd_dif = view.findViewById(R.id.spcd_dif);
                spcd_img= view.findViewById(R.id.spcd_img);
            }
        }
        public  SpotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_spot, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull  SpotAdapter.ViewHolder holder, int position) {
            final SpotVO spotVO = spList.get(position);
            String dif = null;
            holder.spcd_name.setText(spotVO.getSp_name());
            //放入對應的種類名稱
            for(Spot_CategoryVO spcVO:spcList){
                if(spcVO.getSpcate_no().equals(spotVO.getSpcate_no())){
                    holder.spcd_cate.setText(spcVO.getSpcate_name());
                    spcVO = this.spcVO;
                    break;
                }
            }
            //設定對應的觀星難易度字串
            switch (spotVO.getSp_dif()){
                case 1:
                    dif = "容易";
                    break;
                case 2:
                    dif = "普通";
                    holder.spcd_dif.setBackgroundColor(getResources().getColor(R.color.yellow_primary_dark));
                    break;
                case 3:
                    dif = "困難";
                    holder.spcd_dif.setBackgroundColor(getResources().getColor(R.color.red_primary_dark));
                    break;
            }
            holder.spcd_dif.setText(dif);
            String url = Util.LocalHostURL + "SpotServlet";
            sp_imageTask = new ImageTask(url, spotVO.getSp_no(), imageSize, holder.spcd_img);
            sp_imageTask.execute();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0;i<spcList.size();i++){
                        spcVO =spcList.get(i);
                        if(spcVO.getSpcate_no().equals(spotVO.getSpcate_no()))
                            break;
                    }
                    Intent intent = new Intent(getActivity(),SpotDetail.class);
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("spotVO" , spotVO);
                    bundle.putSerializable("spcVO" , spcVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            Type listType = new TypeToken<List<SpotVO>>() {}.getType();
            Type listType2 = new TypeToken<List<Spot_CategoryVO>>() {}.getType();
            //如果直接使用toString或String.valueof()在遇到特殊字元時會跳出例外
            String s_spList = new Gson().toJson(alist.get(0));
            String s_spcList =  new Gson().toJson(alist.get(1));
            spList  = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(s_spList, listType);
            spcList =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(s_spcList, listType2);
            Log.d("POS",s_spList);
            Log.d("POS",s_spcList);
            return spList.size();
        }
    }
    private void updateUI() {
        if (Util.networkConnected(getActivity())) {
            String search = "getAll"; //預設是查詢全部的資料

            if( getArguments() != null){
            search = getArguments().getString("spcate_no"); //依據MainActivity的Item項目傳來的值決定篩選的資料
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getSimpleData");
            jsonObject.addProperty("search", search);
            String jsonOut = jsonObject.toString();
            get_sptext = new CommonTask(Util.LocalHostURL + "SpotServlet", jsonOut);
            List<SpotVO> spList = null;
            List aList= new ArrayList();
            try {
                String result = get_sptext.execute().get();
                Type listType = new TypeToken<List>() {}.getType();
                aList = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (aList == null || aList.isEmpty()) {
                Util.showToast(getActivity(), "找不到地點");
            } else {
                attrac_reclerView.setAdapter(new SpotAdapter(aList));
            }
        } else {
            Util.showToast(getActivity(), "沒有連線");
        }

    }


    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,

        };
        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }
        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }

}