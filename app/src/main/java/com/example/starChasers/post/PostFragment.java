package com.example.starChasers.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starChasers.R;
import com.example.starChasers.tourgroup.TourGroupVO;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_fragment,container,false);

        RecyclerView post_recylerView = view.findViewById(R.id.post_recyclerView);
        post_recylerView.setHasFixedSize(true);
        post_recylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<TourGroupVO> tgList = new ArrayList<>();
        tgList.add(new TourGroupVO(1, R.drawable.t01, "拍天文光圈 F 值的迷思"));
        tgList.add(new TourGroupVO(2, R.drawable.t02, "平地長時間曝光不同 ISO 熱噪比較 "));
        tgList.add(new TourGroupVO(3, R.drawable.t03, "光譜攝影試拍"));
        tgList.add(new TourGroupVO(4, R.drawable.t04, "DSS 疊圖問題"));
        tgList.add(new TourGroupVO(5, R.drawable.t05, "很有趣的攝星儀 AstroTrac TT320X AG "));
        tgList.add(new TourGroupVO(6, R.drawable.t06, "讚長大大的木星疊加"));
        tgList.add(new TourGroupVO(7, R.drawable.t07, "疊圖問題"));
        tgList.add(new TourGroupVO(8, R.drawable.t08, "拍天文光圈"));
        tgList.add(new TourGroupVO(9, R.drawable.t09, "平地長時間曝光不同"));
        tgList.add(new TourGroupVO(10, R.drawable.t10, "光譜攝影試拍"));
        post_recylerView.setAdapter(new PostAdapter(tgList));
        return view;
    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
        private List<TourGroupVO> tG_List;

        private PostAdapter(List<TourGroupVO> tG_List) {
            this.tG_List = tG_List;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView postName;
            private TextView postUesr;
            private TextView postTime;


            private ViewHolder(View view) {
                super(view);
                postName = view.findViewById(R.id.postName);
                postUesr = view.findViewById(R.id.postUesr);
                postTime = view.findViewById(R.id.postTime);
            }
        }

        @NonNull
        @Override
        public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
            final TourGroupVO tourGroupVO = tG_List.get(position);
            holder.postName.setText(tourGroupVO.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), tourGroupVO.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return tG_List.size();
        }
    }

}