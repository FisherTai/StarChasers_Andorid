package com.example.starChasers.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.starChasers.Loginpage;
import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.starChasers.myutil.Util.showToast;

public class MemberFragment extends Fragment {
    SharedPreferences pref;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PICK_FROM_GET = 3;
    private SCMemberVO memberData;
    private static final String TAG = "MemberFragment";
    private Button membtn_Data;
    private Button membtn_RentOrder;
    private Button membtn_TGDetail;
    private ImageView iMobileRent;
    private ImageView iMobileSign;
    private Bitmap bitmap;
    private ImageView iMemIMG;
    private TextView tvMemname;
    private Button btnlogout;
    private Intent intent;
    private Button album;
    private Button camera;
    private Button update;
    private int imageSize;
    private Bitmap updateIcon;
    private File file;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memberData = (SCMemberVO) (getArguments().getSerializable("memberData"));
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View memberView = inflater.inflate(R.layout.member_fragment, container, false);
        intent = new Intent();
        findView (memberView);
            setPage();

        membtn_Data.setOnClickListener(new View.OnClickListener() {
            //會員基本資料按鈕，包裝會員資料送進MemberData.class
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), MemberData.class);
                Bundle bundle = new Bundle();
                bundle.putString("Mem_Email", memberData.getMem_Email());
                bundle.putString("Mem_Name", memberData.getMem_Name());
                bundle.putString("Mem_No", memberData.getMem_No());
                bundle.putString("Mem_Phone", memberData.getMem_Phone());
                bundle.putString("Mem_Psw", memberData.getMem_Psw());
                bundle.putString("Mem_Birthday", (memberData.getMem_Birthday()).toString());
//                bundle.putByteArray("Mem_Img", memberData.getMem_Img());
                bundle.putInt("Mem_Points", memberData.getMem_Points());
                if (memberData.getMem_Sex() == 1) {
                    bundle.putString("Mem_Sex", "男");
                } else {
                    bundle.putString("Mem_Sex", "女");
                }
                bundle.putInt("Mem_State", memberData.getMem_State());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        membtn_RentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(getActivity(), MemberRentorder.class);
                startActivity(intent);
            }
        });
        //揪團管理
                membtn_TGDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.setClass(getActivity(), MemTgManage.class);
                        startActivity(intent);
            }
        });
        //行動租借證
        iMobileRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberFragment.AlertFragment alertFragment = new MemberFragment.AlertFragment();
                FragmentManager mo = getActivity().getSupportFragmentManager();
                alertFragment.show(mo, "alert");
//
            }
        });
        iMobileSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "揪團報到");
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                pref.edit().putBoolean("login", false).apply();
                Intent intent = new Intent(getActivity(), Loginpage.class);
                startActivityForResult(intent, 1);
                getActivity().finish();
            }
        });
        return memberView;
    }
    private  void findView (View memberView){
        membtn_Data = (Button) memberView.findViewById(R.id.membtn_Data);  //會員資料按鈕
        membtn_RentOrder = (Button) memberView.findViewById(R.id.membtn_RentOrder); //租借訂單按鈕
        membtn_TGDetail = (Button) memberView.findViewById(R.id.membtn_TGDetail); //揪團明細按鈕
        iMobileRent = (ImageView) memberView.findViewById(R.id.iMobileRent);
        iMobileSign = (ImageView) memberView.findViewById(R.id.iMobileSign);
        iMemIMG = (ImageView) memberView.findViewById(R.id.iMemIMG);
        tvMemname = memberView.findViewById(R.id.tvMemname);
        btnlogout = (Button) memberView.findViewById(R.id.btnlogout);
        intent = new Intent();
        iMemIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });
    }

    //顯示會員大頭貼及會員名稱
    private void setPage() {
//        Picasso.with(getActivity()).load("http://i.imgur.com/XY1856Y.png").into(iMemIMG);
//        if (memberData.getMem_Img() != null) {
            String url = Util.LocalHostURL + "MemberServlet";
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            ImageTask mem_imageTask = new ImageTask(url, memberData.getMem_No(), imageSize, iMemIMG);
            mem_imageTask.execute();
//            bitmap = BitmapFactory.decodeByteArray(memberData.getMem_Img(), 0, memberData.getMem_Img().length);
//            iMemIMG.setImageBitmap(bitmap);
//        } else {
//            iMemIMG.setImageResource(R.drawable.membericon);
//        }
        tvMemname.setText(memberData.getMem_Name());
    }

    //Qrcode租借清單跳窗
    public static class AlertFragment extends DialogFragment implements DialogInterface.OnClickListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            SharedPreferences pref = getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);

            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllNoString");
            jsonObject.addProperty("mem_no", pref.getString("mem_No", ""));
            String jsonOut = jsonObject.toString();
            CommonTask get_tsutext = new CommonTask(Util.LocalHostURL + "RentOrderServlet", jsonOut);
            String[] resultlist = null;
            try {
                String result = get_tsutext.execute().get();
                resultlist = new GsonBuilder().setDateFormat("yyyy-MM-dd").create().fromJson(result, String[].class);

            } catch (Exception e) {
                Log.e("MobileAlert", "Qrcode:\n"+e.toString());
            }

            final String[] rentOrderList = resultlist;
            AlertDialog alertOrder = new AlertDialog.Builder(getActivity())
                    //設定圖示
//                    .setIcon(R.drawable.alert)
                    .setTitle("訂單選取")
                    .setItems(rentOrderList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent2 = new Intent(getActivity(), AcQrcode.class);
                            intent2.putExtra("rd_no", rentOrderList[which]);
                            startActivity(intent2);
                        }
                    })
                    .setNeutralButton("離開", this)
                    .show();
            return alertOrder;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEUTRAL:
                    dialog.cancel();
                    break;
                default:
                    break;
            }
        }
    }
//PopWindow的設定+相機及相簿上傳的設定
    private void initPopWindow(View v) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_qrcode, null, false);
        album = view.findViewById(R.id.album);
        camera= view.findViewById(R.id.camera);
        update= view.findViewById(R.id.update);
        //獲得螢幕資訊
        /*        metric.widthPixels;       螢幕寬度（畫素）
                  metric.heightPixels;      螢幕高度（畫素）
                  metric.density;           螢幕密度（0.75 / 1.0 / 1.5）
                  metric.densityDpi;        螢幕密度DPI（120 / 160 / 240）
        */
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

        //1.創建PopupWindow，參數依次是View的寬高
        final PopupWindow popWindow = new PopupWindow(view,
                metric.widthPixels, metric.heightPixels/5, true);
        popWindow.setAnimationStyle(R.style.pop_animation);  //設置顯示動畫
        //為了讓PopupWindow在點擊非PopupWindow區域時會消失，如果沒有下面代碼的話，當PopupWindow顯示出來了，無論按多少次返回鍵都關不掉也無法退出
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 這裏如果返回true的話，touch事件將被攔截，攔截後 PopupWindow的onTouchEvent不被調用，這樣點擊外部區域無法dismiss
            }
        });
//        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop));    //為popWindow設置背景
        //設置顯示位置，View,對應位置,y,x
        popWindow.showAtLocation(v, Gravity.NO_GRAVITY, metric.widthPixels,metric.heightPixels-(metric.heightPixels/5));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img = null;
                if (bitmap != null) {
                    img = Util.bitmapToBase64(bitmap);
                }
                String url = Util.LocalHostURL + "MemberServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "updateMemberImg");
                jsonObject.addProperty("mem_no", memberData.getMem_No());
                jsonObject.addProperty("mem_img", img);
                String jsonOut = jsonObject.toString();
                CommonTask updateImg = new CommonTask(url, jsonOut);
                String result = null;
                try {
                    result = updateImg.execute().get();
                    if (!result.equals("Success")){
                    Util.showToast(getActivity(), result);}
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                popWindow.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                Uri contentUri = FileProvider.getUriForFile(
                        getActivity(), getActivity().getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                if (isIntentAvailable(getActivity(), intent)) {
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                } else {
                    Util.showToast(getActivity(), "NoCameraApp");
                }
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                // 開啟Pictures畫面Type設定為image
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // 使用Intent.ACTION_GET_CONTENT這個Action
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (isIntentAvailable(getActivity(), intent)) {
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                } else {
                    Util.showToast(getActivity(), "NoIMG");
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
//                可以取得照片圖檔
                case PICK_FROM_CAMERA:
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    // inSampleSize值即為縮放的倍數 (數字越大縮越多)
                    opt.inSampleSize = Util.getImageScale(file.getPath(), 640, 1280);
                    bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
                    //如果照片被轉向過，則進行校正
                    if (Util.getBitmapDegree(file.getPath()) != 0){
                        bitmap = Util.rotateBitmapByDegree(bitmap,Util.getBitmapDegree(file.getPath()));
                    }
                    iMemIMG.setImageBitmap(bitmap);
                    break;
                case PICK_FROM_GALLERY:
                    Uri uri = data.getData();
                    Log.e("uri", "PICK_FROM_GALLERY"+uri.toString());
                    ContentResolver cr = getActivity().getContentResolver();
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        /* 將Bitmap設定到ImageView */
                        iMemIMG.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                    break;
            }
            //將修改過的bitmap在手機端直接存入memberData
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] buf = out.toByteArray();
            memberData.setMem_Img(buf);
        }
    }
    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}




