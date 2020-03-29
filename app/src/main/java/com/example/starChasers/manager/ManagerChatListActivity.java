package com.example.starChasers.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.web_master_chat.State;
import com.example.starChasers.web_master_chat.WebMasterChatActivity;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

//import com.example.starChasers.web_master_chat.WebMasterChatActivity;

public class ManagerChatListActivity  extends AppCompatActivity {
    private final String TAG ="ManagerChatListActivity";
    private RecyclerView rvFriends;
    private String user;
    private List<String> friendList;
    private LocalBroadcastManager broadcastManager;
    private String mem_no;
    private String wm_name;
    private TextView tv_onlineMaster ;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.avtivity_memberlist);
        Log.d("WMFStep","onCreateView");
        mem_no = getIntent().getStringExtra("wm_no");
        wm_name = getIntent().getStringExtra("wm_name");
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerFriendStateReceiver();
        // 初始化聊天清單
        friendList = new LinkedList<>();
        // 初始化RecyclerView
        rvFriends = findViewById(R.id.rvFriends);
        rvFriends.setLayoutManager(new LinearLayoutManager(this));
        rvFriends.setAdapter(new FriendAdapter(this));
        tv_onlineMaster = findViewById(R.id.tv_onlineMaster);
        tv_onlineMaster.setText("目前無會員在線");
        Util.connectServer(this, mem_no);
    }

    // 攔截user連線或斷線的Broadcast(執行一次)
    private void registerFriendStateReceiver() {
        Log.d("WMFStep","registerFriendStateReceiver");
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        FriendStateReceiver friendStateReceiver = new FriendStateReceiver();
        broadcastManager.registerReceiver(friendStateReceiver, openFilter);
        broadcastManager.registerReceiver(friendStateReceiver, closeFilter);
    }

    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private class FriendStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WMFStep","FriendStateReceiver");
            String message = intent.getStringExtra("message");
            State stateMessage = new Gson().fromJson(message, State.class);
            Log.d("WMFStep",message);
            String type = stateMessage.getType();
            String friend = stateMessage.getUser();
            switch (type) {
                // 有user連線
                case "open":
                    // 如果是自己連線
                    if (friend.equals(mem_no)) {
                        // 取得server上的所有user
                        friendList = new LinkedList<>(stateMessage.getUsers());
                        // 將自己從聊天清單中移除
                        friendList.remove(mem_no);
                    } else {
                        // 如果其他user連線且尚未加入聊天清單，就加上
                        if (!friendList.contains(friend)) {
                            friendList.add(friend);
                        }
//                        Util.showToast(context, friend + " is online");
                    }
                    // 重刷聊天清單
                    rvFriends.getAdapter().notifyDataSetChanged();
                    break;
                // 有user斷線
                case "close":
                    // 將斷線的user從聊天清單中移除
                    friendList.remove(friend);
                    rvFriends.getAdapter().notifyDataSetChanged();
//                    Util.showToast(context, friend + " is offline");
            }
            if (friendList.size()==0)
                tv_onlineMaster.setVisibility(View.VISIBLE);
            else{
                tv_onlineMaster.setVisibility(View.GONE);
            }
            Log.d(TAG, message);
        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
        Context context;
        FriendAdapter(Context context) {
            Log.d("WMFStep","FriendAdapter");
            this.context = context;
        }

        class FriendViewHolder extends RecyclerView.ViewHolder {
            TextView tvFriendName;

            FriendViewHolder(View itemView) {
                super(itemView);
                Log.d("WMFStep","FriendViewHolder");
                tvFriendName = itemView.findViewById(R.id.tvFrinedName);
            }
        }

        @Override
        public int getItemCount() {
            Log.d("WMFStep","getItemCount");
            return friendList.size();
        }

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("WMFStep","onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.card_master, parent, false);
            return new FriendViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            Log.d("WMFStep","onBindViewHolder");
            final String friend = friendList.get(position);
            holder.tvFriendName.setText(friend);
            // 點選聊天清單上的user即開啟聊天頁面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ManagerChatListActivity.this, WebMasterChatActivity.class);
                    intent.putExtra("friend", friend);
                    intent.putExtra("mem_no",mem_no);
                    intent.putExtra("mem_name",wm_name);
                    startActivity(intent);
                }
            });
        }
    }
    // 結束即中斷WebSocket連線
    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.disconnectServer();
    }


}
