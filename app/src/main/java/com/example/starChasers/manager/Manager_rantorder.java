package com.example.starChasers.manager;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;


public class Manager_rantorder extends AppCompatActivity {
    private  RentOrder_VO roVO;
    private TextView m_rd_no;
    private TextView m_erg_no;
    private TextView m_mem_no;
    private TextView m_rd_startdate;
    private TextView m_rd_enddate;
    private TextView m_rd_day;
    private TextView m_rd_deposit;
    private TextView m_rd_fine;
    private TextView m_rd_cps;
    private TextView m_rd_money;
    private TextView m_rd_nowstate;
    private Spinner m_rd_state;
    private Button statechange;
    private static final String PACKAGE = "com.google.zxing.client.android";
    private final String TAG = "Manager_rantorder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_rentorder);
        findViews();
//        setUi();
    }

    private void findViews() {
        m_rd_no = findViewById(R.id.m_rd_no);
        m_erg_no= findViewById(R.id.m_erg_no);
        m_mem_no= findViewById(R.id.m_mem_no);
        m_rd_startdate= findViewById(R.id.m_rd_startdate);
        m_rd_enddate= findViewById(R.id.m_rd_enddate);
        m_rd_day= findViewById(R.id.m_rd_day);
        m_rd_deposit= findViewById(R.id.m_rd_deposit);
        m_rd_fine= findViewById(R.id.m_rd_fine);
        m_rd_cps= findViewById(R.id.m_rd_cps);
        m_rd_money= findViewById(R.id.m_rd_money);
        m_rd_state= findViewById(R.id.m_rd_state);
        statechange= findViewById(R.id.statechange);
        m_rd_nowstate =findViewById(R.id.m_rd_nowstate);

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                try {
                    startActivityForResult(intent, 0);
                }
                // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
                catch (ActivityNotFoundException ex) {
                    showDownloadDialog();
                }
            }

    private void setUi() {
        m_rd_no.setText(roVO.getRd_no());
        m_erg_no.setText(roVO.getErg_no());
        m_mem_no.setText(roVO.getMem_no());
        m_rd_startdate.setText(String.valueOf(roVO.getRd_startdate()));
        m_rd_enddate.setText(String.valueOf(roVO.getRd_enddate()));
        m_rd_day.setText(String.valueOf(roVO.getRd_day()));
        m_rd_deposit.setText(String.valueOf(roVO.getRd_deposit()));
        m_rd_fine.setText(String.valueOf(roVO.getRd_fine()));
        m_rd_cps.setText(String.valueOf(roVO.getRd_cps()));
        m_rd_money.setText(String.valueOf(roVO.getRd_money()));
        m_rd_nowstate.setText(roVO.getRd_state());
        statechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("statechange",m_rd_state.getSelectedItem().toString());
                JsonObject jsonObject =new JsonObject();
                jsonObject.addProperty("rd_no",roVO.getRd_no());
                jsonObject.addProperty("action","changeState");
                jsonObject.addProperty("rd_state",m_rd_state.getSelectedItem().toString());
                CommonTask statechange = new CommonTask(Util.LocalHostURL+"RentOrderServlet",(jsonObject.toString()));
                String jsonRes=null;
                try {
                    jsonRes= statechange.execute().get();
                }catch (Exception e){
                    Log.d(TAG,e.toString());
                }
                Util.showToast(Manager_rantorder.this,"修改"+jsonRes);
                finish();

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        if (requestCode == 0) {
            String message = "";
            String jsonResult ="";
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                message = "Content: " + contents + "\nFormat: " + format;

                String jsonOut = contents;
                CommonTask commonTask = new CommonTask(Util.LocalHostURL+"RentOrderServlet",jsonOut);
                try {
                     jsonResult = commonTask.execute().get();
                     roVO = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(jsonResult, RentOrder_VO.class);
                    setUi();
                }catch (Exception e){
                    Log.e("Manager_rantorder",e.toString());
                }

            } else if (resultCode == RESULT_CANCELED) {
                message = "Scan was Cancelled!";
            }
            Log.d("ScannerMessage",message);
            Log.d("ScannerInput",jsonResult);
        }
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("No Barcode Scanner Found");
        downloadDialog.setMessage("Please download and install Barcode Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }
}
