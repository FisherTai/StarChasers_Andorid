package com.example.starChasers.member;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.starChasers.R;
import com.example.starChasers.zxing.Contents;
import com.example.starChasers.zxing.QRCodeEncoder;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.util.Timer;
import java.util.TimerTask;

public class AcQrcode extends AppCompatActivity  {
    private Timer timer;
    private int tt=300;//設置初始秒數
    private int mm=4;
    private int s1=60;
    private String s3="";
    private ImageView ac_qrcode;
    private  final String TAG = "ArQrcode";
    private String qrCodeText;
    private TextView qr_rdno;
    private TextView timeout;

    private String jsonout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        findview();
        showqrcode();
        timecounter();
        qr_rdno.setText(qrCodeText);

    }
    private void findview(){
        ac_qrcode =findViewById(R.id.ac_qrcode);
        Intent intent = this.getIntent();
        qrCodeText = intent.getStringExtra("rd_no");
        qr_rdno =findViewById(R.id.qr_rdno);
        timeout=findViewById(R.id.timeout);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("rd_no",qrCodeText);
        jsonObject.addProperty("action","getOneOrder");
        jsonout = jsonObject.toString();

    }
    private int getDimension() {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }

    private void showqrcode(){
        int smallerDimension = getDimension();
//        String url=Util.LocalHostURL+"RentOrderServletforMaster";
//        String url=Util.LocalHostURL+"RentOrderServlet";
        Log.d(TAG, qrCodeText);
        // Encode with a QR Code image
//        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(url+"?"+"arcode="+qrCodeText+"&action=getOneOrder", null,
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(jsonout, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                smallerDimension);

        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ac_qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void timecounter(){
        timer = new Timer();//時間函示初始化
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tt--;//時間倒數
                        s1--;

                        timeout.setText(mm+":"+s3+s1);//讓TextView元件顯示時間倒數情況
                        //if判斷示裡面放置在時間結束後想要完成的事件
                        if (tt < 1) {
                            finish();
                            cancel();
                            System.gc();
                        }
                        if (s1 < 1){
                            mm--;
                            s3 ="";
                            s1 =60;

                        }else if(s1<=10){
                            s3 ="0";
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);//時間在幾毫秒過後開始以多少毫秒執行

    }
}
