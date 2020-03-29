package com.example.starChasers.Spot;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.starChasers.R;
import com.example.starChasers.myutil.Util;
import com.example.starChasers.task.CommonTask;
import com.example.starChasers.task.ImageTask;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import java.util.Locale;

public class SpotDetail extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "SpotDetail";
    //    private static final int MY_REQUEST_CODE = 0;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int MY_REQUEST_CODE = 1;
    private GoogleMap map;
    private SpotVO spotVO;
    private Spot_CategoryVO spcVO;
    private boolean isFavorite;
    private TextView spDetail_des;
    private TextView spDetail_spName;
    private TextView spCategory;
    private TextView map_prompt;
    private ImageView spDetail_img;
    private ImageTask sp_imageTask;
    private Button addfavorite;
    private Button btn_mapbacklocal;
    private String mem_no;
    // 標記
    private Marker spotMarker;
    // 緯經度
    private LatLng spotLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location location;

    //Activity生命週期----------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spot_detail_activity);
        findView();
        getmap();
    }
    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sp_imageTask != null) {
            sp_imageTask.cancel(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += "NotGranted";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void findView() {
        final SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        mem_no = pref.getString("mem_No", "");
        Log.d(TAG, "findView: " + mem_no);
        spDetail_des = findViewById(R.id.spDetail_des);
        spDetail_spName = findViewById(R.id.spDetail_spName);
        spCategory = findViewById(R.id.spCategory);
        spDetail_img = findViewById(R.id.spDetail_img);
        map_prompt = findViewById(R.id.map_prompt);
        btn_mapbacklocal = findViewById(R.id.btn_mapbacklocal);
        spotVO = (SpotVO) getIntent().getSerializableExtra("spotVO");
        spcVO = (Spot_CategoryVO) getIntent().getSerializableExtra("spcVO");
        addfavorite = findViewById(R.id.addfavorite);
        //設定地標經緯度
        spotLatLng = new LatLng((spotVO.getSp_lat()), (spotVO.getSp_lon()));

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permission2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED || permission2 != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            btn_mapbacklocal.setVisibility(View.GONE);
            Util.showToast(SpotDetail.this,"未取得相關權限，可能有部分功能無法使用");
        }else{
            //設定Map的Fragment
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.fmMap);
            mapFragment.getMapAsync(this);

        }
        spCategory.setText("(" + spcVO.getSpcate_name() + ")");
        spDetail_spName.setText(spotVO.getSp_name());
        spDetail_des.setText(spotVO.getSp_des2());
        map_prompt.setText("點擊標記可察看評價及導航路線");
        String url = Util.LocalHostURL + "SpotServlet";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 2;
        sp_imageTask = new ImageTask(url, spotVO.getSp_no(), imageSize, spDetail_img);
        sp_imageTask.execute();
        setFavorite(spotVO.getSp_no(), mem_no);

        addfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = null;
                //判斷是否已經是收藏狀態
                if (isFavorite) {
                    action = "delete";
                } else {
                    action = "add";
                }
                JsonObject jsonObject = new JsonObject();
                String url = Util.LocalHostURL + "Spot_CollectionServlet";
                jsonObject.addProperty("action", action);
                jsonObject.addProperty("sp_no", spotVO.getSp_no());
                jsonObject.addProperty("mem_no", mem_no);
                String jsonOut = jsonObject.toString();
                CommonTask sp_CommonTask = new CommonTask(url, jsonOut);
                String jsonRes = null;
                try {
                    jsonRes = sp_CommonTask.execute().get();
                    if ("Success".equals(jsonRes)) {
                        isFavorite = !isFavorite;
                        if (isFavorite) {
                            Util.setButtonState(addfavorite, "取消收藏", getDrawable(R.drawable.bg_btn_gray));
                        } else {
                            Util.setButtonState(addfavorite, "+收藏", getDrawable(R.drawable.bg_btn_green));
                        }
                    } else {
                        Util.showToast(SpotDetail.this, "Fail");
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private boolean setFavorite(String sp_no, String mem_no) {
        isFavorite = false;
        JsonObject jsonObject = new JsonObject();
        String url = Util.LocalHostURL + "Spot_CollectionServlet";
        jsonObject.addProperty("action", "isFavorite");
        jsonObject.addProperty("sp_no", sp_no);
        jsonObject.addProperty("mem_no", mem_no);
        String jsonOut = jsonObject.toString();
        CommonTask sp_CommonTask = new CommonTask(url, jsonOut);
        try {
            isFavorite = Boolean.parseBoolean(sp_CommonTask.execute().get());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (isFavorite) {
            Util.setButtonState(addfavorite, "取消收藏", getResources().getDrawable(R.drawable.bg_btn_gray));
        } else {
            Util.setButtonState(addfavorite, "+收藏", getResources().getDrawable(R.drawable.bg_btn_green));
        }
        return isFavorite;
    }

    protected void getmap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setupMap();
    }

    // 完成地圖相關設定
    @SuppressLint("MissingPermission")
    private void setupMap() {
        map.setMyLocationEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                // 鏡頭焦點
                .target(spotLatLng)
                // 地圖縮放層級定為7
                .zoom(7)
                .build();
        // 改變鏡頭焦點到指定的新地點
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        addMarkersToMap();
        // 如果不套用自訂InfoWindowAdapter會自動套用預設訊息視窗
        map.setInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    private void addMarkersToMap() {
        spotMarker = map.addMarker(new MarkerOptions()
                .position(spotLatLng)   // 設定標記位置
                .title(spotVO.getSp_name())   // 設定預設標記標題
                .snippet(spotVO.getSp_content2()));   // 設定預設標記描述
//              .icon(BitmapDescriptorFactory.fromResource(R.drawable.lock))    // 自訂標記圖示
//                .draggable(true)// 長按標記可以拖曳該標記
    }

    // 自訂InfoWindowAdapter，當點擊標記時會跳出自訂風格的訊息視窗
    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View infoWindow;

        MyInfoWindowAdapter() {
            infoWindow = LayoutInflater.from(SpotDetail.this)
                    .inflate(R.layout.custom_infowindow, null);
        }

        @Override
        // 回傳設計好的訊息視窗樣式
        // 回傳null會自動呼叫getInfoContents(Marker)
        public View getInfoWindow(Marker marker) {
            // 顯示標題
            String title = marker.getTitle();
            TextView tvTitle = infoWindow.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
            // 顯示描述
            String snippet = marker.getSnippet();
            TextView tvSnippet = infoWindow.findViewById(R.id.tvSnippet);
            tvSnippet.setText(snippet);
            return infoWindow;
        }

        @Override
        // 當getInfoWindow(Marker)回傳null時才會呼叫此方法
        // 此方法如果再回傳null，代表套用預設視窗樣式
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    public void onResetMapClick(View view) {
        // 改變鏡頭焦點到指定的新地點
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(spotLatLng)
                .zoom(7)
                .build()));
        map.clear();// 先清除Map上的標記再重新打上標記以避免標記重複
        addMarkersToMap();
    }

    /*以下為導航功能*/
    public void navigation(View view) {
        String locationName = String.valueOf(spotLatLng); //這裡必須傳送地名而非經緯度
        if (location == null || locationName.isEmpty()) {
            Util.showToast(this, "找不到地點");
            return;
        }

        // 取得自己位置與使用者輸入位置的緯經度
        double fromLat = location.getLatitude();
        double fromLng = location.getLongitude();
        double toLat = spotLatLng.latitude;
        double toLng = spotLatLng.longitude;
        direct(fromLat, fromLng, toLat, toLng);
    }

    private void direct(double fromLat, double fromLng, double toLat, double toLng) {
        // 設定欲前往的Uri，saddr-出發地緯經度；daddr-目的地緯經度
        String uriStr = String.format(Locale.TAIWAN,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                fromLat, fromLng, toLat, toLng);
        Intent intent = new Intent();
        // 指定交由Google地圖應用程式接手
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        // ACTION_VIEW-呈現資料給使用者觀看
        intent.setAction(Intent.ACTION_VIEW);
        // 將Uri資訊附加到Intent物件上
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        // 10秒要一次位置資料 (但不一定, 有可能不到10秒, 也有可能超過10秒才要一次)
        locationRequest.setInterval(10000);
        // 若有其他app也使用了LocationServices, 就會以此時間為取得位置資料的依據
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.e(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SpotDetail.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(SpotDetail.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Log.e(TAG, "Cancel location updates requested");
                    }
                });
    }

}
