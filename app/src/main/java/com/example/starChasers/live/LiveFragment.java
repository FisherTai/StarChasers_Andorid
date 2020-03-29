package com.example.starChasers.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.starChasers.R;
import com.example.starChasers.tourgroup.TourGroupVO;

import java.util.ArrayList;
import java.util.List;

public class LiveFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_fragment, container, false);

        RecyclerView live_recyclerView = view.findViewById(R.id.live_recyclerView);
        live_recyclerView.setHasFixedSize(true);
        live_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        final List<TourGroupVO> liveList = new ArrayList<>();
        liveList.add(new TourGroupVO(1, R.drawable.pic_card_live, "直播標題A"));
        liveList.add(new TourGroupVO(2, R.drawable.pic_card_live, "直播標題B"));
        liveList.add(new TourGroupVO(3, R.drawable.pic_card_live, "直播標題C"));
        liveList.add(new TourGroupVO(4, R.drawable.pic_card_live, "直播標題D"));
        liveList.add(new TourGroupVO(5, R.drawable.pic_card_live, "直播標題E"));
        liveList.add(new TourGroupVO(6, R.drawable.pic_card_live, "直播標題F"));
        liveList.add(new TourGroupVO(7, R.drawable.pic_card_live, "直播標題G"));
        liveList.add(new TourGroupVO(8, R.drawable.pic_card_live, "直播標題H"));
        liveList.add(new TourGroupVO(9, R.drawable.pic_card_live, "直播標題I"));
        liveList.add(new TourGroupVO(10, R.drawable.pic_card_live, "直播標題J"));
        live_recyclerView.setAdapter(new LiveAdapter(liveList));
        return view;
    }


    private class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {
        private List<TourGroupVO> liveList;

        private LiveAdapter(List<TourGroupVO> liveList) {
            this.liveList = liveList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView liveName;
            private ImageView liveLogo;

            private ViewHolder(View view) {
                super(view);
                liveName = view.findViewById(R.id.liveName);
                liveLogo = view.findViewById(R.id.liveLogo);
            }
        }

        @NonNull
        @Override
        public LiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_live, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LiveAdapter.ViewHolder holder, int position) {
            final TourGroupVO tourGroupVO = liveList.get(position);
            holder.liveName.setText(tourGroupVO.getName());
            holder.liveLogo.setImageResource(tourGroupVO.getLogo());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), tourGroupVO.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return liveList.size();
        }
    }

}
