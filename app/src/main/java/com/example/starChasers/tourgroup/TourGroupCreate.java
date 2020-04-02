package com.example.starChasers.tourgroup;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.starChasers.R;
import com.example.starChasers.base.BaseActivity;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TourGroupCreate extends BaseActivity {
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PICK_FROM_GET = 3;
    private static final String TAG = "TourGroupCreate";
    private TextView tgc_AcDate,
                     tgc_AcEndDate,
                     tgc_SignEndDay;
    private EditText tgc_Address,
                     tgc_NumLimit,
                     tgc_Condition,
                     tgc_Content,
                     tgc_Name;
    private ImageView tgc_IMG;
    private Button tgc_btnSubmit;
    private ImageTask imageTask;
    private String mem_no;
    private CommonTask addTgdate;
    private File file;
    private Bitmap picture;
    private Uri outputFileUri;
    private int mYear, mMonth, mDay;
    

    @Override
    protected int getLayout() {
        return R.layout.tour_group_create_activity;
    }

    protected void findView() {
        tgc_Name = findViewById(R.id.tgc_Name);
        tgc_AcDate = findViewById(R.id.tgc_AcDate);
        tgc_AcEndDate = findViewById(R.id.tgc_AcEndDate);
        tgc_SignEndDay = findViewById(R.id.tgc_SignEndDay);
        tgc_Address = findViewById(R.id.tgc_Address);
        tgc_NumLimit = findViewById(R.id.tgc_NumLimit);
        tgc_Condition = findViewById(R.id.tgc_Condition);
        tgc_Content = findViewById(R.id.tgc_Content);
        tgc_IMG = findViewById(R.id.tgc_IMG);
        tgc_btnSubmit = findViewById(R.id.tgc_btnSubmit);
        Bundle bundle = getIntent().getExtras();
        mem_no = bundle.getString("mem_no");


        tgc_AcDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog((new Date()).getTime(),tgc_AcDate); //第一個參數傳入要顯示的TextView,第二個參數是MinDate,第三個參數(如果有)是MaxDate
            }
        });
        tgc_AcEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long min = Util.StringCastUtilDate(tgc_AcDate.getText().toString()).getTime();
                    setDialog(min,tgc_AcEndDate);
                }catch (NullPointerException e){
                    Util.showToast(TourGroupCreate.this,"請先選擇活動日期");
                }
            }
        });
        tgc_SignEndDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long max = Util.StringCastUtilDate(tgc_AcDate.getText().toString()).getTime();
                    setDialog((new Date()).getTime(),tgc_SignEndDay,max);
                }catch (NullPointerException e){
                    Util.showToast(TourGroupCreate.this,"請先選擇活動日期");
                }
            }
        });


    }

    @Override
    protected void initActivity() {
        creatTourGroup();
    }

    protected void creatTourGroup() {

        tgc_btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String img = null;

                if (picture != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] cpicture = baos.toByteArray();
                    img = Base64.encodeToString(cpicture, 100);
                }
                if (tgc_Name.getText().toString().trim().length() <= 0 || tgc_Name.getText() == null || tgc_Name.getText().toString().trim().length() > 20) {
                    Util.showToast(TourGroupCreate.this, "行程名稱不得空白且長度不可超過20字");

                } else if (tgc_AcDate.getText().toString().trim().length() <= 0 || tgc_AcDate.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "活動日期不得空白");

                } else if (tgc_AcEndDate.getText().toString().trim().length() <= 0 || tgc_AcEndDate.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "結束日期不得空白");

                } else if (tgc_SignEndDay.getText().toString().trim().length() <= 0 || tgc_SignEndDay.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "截止報名日期不得空白");

                } else if (tgc_Address.getText().toString().trim().length() <= 0 || tgc_Address.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "地點不得空白");

                } else if (tgc_NumLimit.getText().toString().trim().length() <= 0 || tgc_NumLimit.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "人數上限不得空白");

                } else if (tgc_Condition.getText().toString().trim().length() <= 0 || tgc_Condition.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "請輸入成團條件");

                } else if (tgc_Content.getText().toString().trim().length() <= 0 || tgc_Content.getText() == null) {
                    Util.showToast(TourGroupCreate.this, "請輸入揪團描述");

                } else {
                    addTgdate = null;
                    String url = Util.LocalHostURL + "TourGroupServlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "addTg");
                    jsonObject.addProperty("tgc_Name", tgc_Name.getText().toString());
                    jsonObject.addProperty("tgc_AcDate", tgc_AcDate.getText().toString());
                    jsonObject.addProperty("tgc_AcEndDate", tgc_AcEndDate.getText().toString());
                    jsonObject.addProperty("tgc_SignEndDay", tgc_SignEndDay.getText().toString());
                    jsonObject.addProperty("tgc_Address", tgc_Address.getText().toString());
                    jsonObject.addProperty("tgc_NumLimit", tgc_NumLimit.getText().toString());
                    jsonObject.addProperty("tgc_Condition", tgc_Condition.getText().toString());
                    jsonObject.addProperty("tgc_Content", tgc_Content.getText().toString());
                    jsonObject.addProperty("mem_no", mem_no);
                    jsonObject.addProperty("tgc_IMG", img);
                    String jsonOut = jsonObject.toString();
                    addTgdate = new CommonTask(url, jsonOut);
                    String result = null;
                    try {
                        result = addTgdate.execute().get();
                        Util.showToast(TourGroupCreate.this, result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if ("Success".equals(result)) {
                        if (picture != null) {
                        picture.recycle();//在這裡才把圖片資源回收
                            }
                        finish();

                    }
                }

            }
        });
//設定
        tgc_IMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*在Activity Action裡面有一個“ACTION_GET_CONTENT”字串常量，
                  該常量讓使用者選擇特定型別的資料，並返回該資料的URI.我們利用該常量，
                  然後設定型別為“image/*”，就可獲得Android手機內的所有image。*/
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                // 開啟Pictures畫面Type設定為image
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // 使用Intent.ACTION_GET_CONTENT這個Action
                intent.setAction(Intent.ACTION_GET_CONTENT);
 /*照相用
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
//                file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//                file = new File(file, "picture.jpg");
//                Uri contentUri = FileProvider.getUriForFile(
//                        TourGroupCreate.this, getPackageName() + ".provider", file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
*/
                if (isIntentAvailable(TourGroupCreate.this, intent)) {
                    //startActivityForResult(intent, PICK_FROM_CAMERA); 照相用
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                } else {
                    Util.showToast(TourGroupCreate.this, "NoIMG");
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_GALLERY:
                    Uri uri = data.getData();
                    Log.e("uri", uri.toString());
                    ContentResolver cr = this.getContentResolver();
                    try {
                        picture = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        /* 將Bitmap設定到ImageView */
                        tgc_IMG.setImageBitmap(picture);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                    break;

            }
        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private void setDialog(long minDate, final TextView showTV,long maxDate) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        showTV.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(minDate);
        dialog.getDatePicker().setMaxDate(maxDate);
        dialog.show();
    }
    private void setDialog(long minDate, final TextView showTV) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        showTV.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(minDate);
        dialog.show();
    }
    private void setDialog(final TextView showTV,long maxDate ) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        showTV.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMaxDate(maxDate);
        dialog.show();
    }
}
