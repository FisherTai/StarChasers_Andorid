package com.example.starChasers.web_master_chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.example.starChasers.myutil.Util.chatWebSocketClient;



/*
 * 此頁可以發送與接收訊息。
 * 當ChatWebSocketClient接收到聊天訊息時會發LocalBroadcast，
 * 此頁的BroadcastReceiver會接收到並在TextView呈現。
 */

public class WebMasterChatActivity extends AppCompatActivity {
    private static final String TAG = "WebMasterChatActivity";
    private LocalBroadcastManager broadcastManager;
    private TextView wmctv_message;
    private EditText wmced_mymessege;
    private ScrollView chat_container;
    private Button wmcbtn_send ;
    private String friend;
    private String mem_no;
    private String mem_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wm_chat_activity);
        findViews();
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(this);
        registerChatReceiver();
        // 取得前頁傳來的聊天對象
        friend = getIntent().getStringExtra("friend");
        mem_no = getIntent().getStringExtra("mem_no");
        mem_name = getIntent().getStringExtra("mem_name");
        setTitle("friend: " + friend);
        Util.connectServer(this, mem_no);
        getHistoryMessage();
    }

    private void findViews() {
        wmctv_message = findViewById(R.id.wmctv_message);
        wmced_mymessege = findViewById(R.id.wmced_mymessege);
        chat_container = findViewById(R.id.chat_container);
        wmcbtn_send = findViewById(R.id.wmcbtn_send);

        wmcbtn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = wmced_mymessege.getText().toString();
                if (message.trim().isEmpty()) {
                    Util.showToast(WebMasterChatActivity.this,"空白消息");
                    return;
                }
                String sender = mem_no;
//                String sender = mem_name;
                // 將欲傳送訊息先顯示在TextView上
                wmctv_message.append(sender + ": " + message + "\n");

                // 將輸入的訊息清空
                wmced_mymessege.setText(null);
                // 捲動至最新訊息
                chat_container.post(new Runnable() {
                    @Override
                    public void run() {
                        chat_container.fullScroll(View.FOCUS_DOWN);
                    }
                });
                // 將欲傳送訊息轉成JSON後送出
                ChatMessage chatMessage = new ChatMessage("chat", sender, friend, message);
                String chatMessageJson = new Gson().toJson(chatMessage);
                chatWebSocketClient.send(chatMessageJson);
                Log.d(TAG, "output: " + chatMessageJson);
            }
        });
    }

    private void getHistoryMessage() {
        ChatMessage chatMessage = new ChatMessage("history", mem_no, friend, "");
        String chatMessageJson = new Gson().toJson(chatMessage);
        chatWebSocketClient.send(chatMessageJson);
    }

    private void registerChatReceiver() {
        IntentFilter chatFilter = new IntentFilter("chat");
        IntentFilter historyFilter = new IntentFilter("history");
        ChatReceiver chatReceiver = new ChatReceiver();
        broadcastManager.registerReceiver(chatReceiver, chatFilter);
        broadcastManager.registerReceiver(chatReceiver, historyFilter);
    }

    // 接收到聊天訊息會在TextView呈現
    private class ChatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);
            if ("history".equals(chatMessage.getType())) {
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> historyMsg = new Gson().fromJson(chatMessage.getMessage(), type);
                for (String str : historyMsg) {
                    ChatMessage cm = new Gson().fromJson(str, ChatMessage.class);
                    wmctv_message.append(cm.getSender() + ": " + cm.getMessage() + "\n");
                }
            }

            String sender = chatMessage.getSender();
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView
            if (sender.equals(friend)) {
                wmctv_message.append(sender + ": " + chatMessage.getMessage() + "\n");
                chat_container.post(new Runnable() {
                    @Override
                    public void run() {
                        chat_container.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
            Log.d(TAG, message);
        }
    }
}
