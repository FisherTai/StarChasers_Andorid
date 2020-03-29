package com.example.starChasers.myutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    private final static String TAG = "Util";
    public final static String LocalHost ="0851243b.ngrok.io";
//    public final static String LocalHost ="10.0.2.2:8081";
//    public final static String LocalHost ="da104g5.nctu.me";

//    public final static String LocalHostURL = "http://10.0.2.2:8081/DA104G5M/Mobile";
//    public final static String LocalHostURL = "http://da104g5.nctu.me/DA104G5M/Mobile";
//    public final static String LocalHostURL = "https://cd5ff39a.ngrok.io/DA104G5M/Mobile";
public final static String LocalHostURL = "http://"+LocalHost+"/DA104G5/Mobile";
public static final String SERVER_URI =
//        "ws://10.0.2.2:8081/DA104G5/MasterWS/";
        "wss://"+LocalHost+"/DA104G5M/MasterWS/";

    public final static String PREF_FILE = "preference";
    public static boolean confail = false;
    public static ChatWebSocketClient chatWebSocketClient;

    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e("connectServer", e.toString());
        }
        if (chatWebSocketClient == null) {
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }



    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setButtonState(Button button,String setText, Drawable setBackground){
        button.setBackground(setBackground);
        button.setText(setText);
    }
    public static void setButtonState(Button button,Boolean setEnabled,String setText, Drawable setBackground){
        button.setBackground(setBackground);
        button.setEnabled(false);
        button.setText(setText);
    }

    public static void setButtonState(Button button,Boolean setEnabled, Drawable setBackground){
        button.setBackground(setBackground);
        button.setEnabled(false);
    }

    public static byte[] bitmapToPNG(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 轉成PNG不會失真，所以quality參數值會被忽略
        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * options.inJustDecodeBounds取得原始圖片寬度與高度資訊 (但不會在記憶體裡建立實體)
     * 當輸出寬與高超過自訂邊長邊寬最大值，scale設為2 (寬變1/2，高變1/2)
     */
    public static int getImageScale(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int scale = 1;
        while (options.outWidth / scale >= width ||
                options.outHeight / scale >= height) {
            scale *= 2;
        }
        return scale;
    }



    /**
     * 集合一些圖片工具
     *
     * Created by zhuwentao on 2016-07-22.
     */
        //存放拍攝圖片的資料夾
        private static final String FILES_NAME = "/MyPhoto";
        //獲取的時間格式
        public static final String TIME_STYLE = "yyyyMMddHHmmss";
        //圖片種類
        public static final String IMAGE_TYPE = ".png";

        /**
         * 獲取手機可儲存路徑
         *
         * @param context 上下文
         * @return 手機可儲存路徑
         */
        private static String getPhoneRootPath(Context context) {
// 是否有SD卡
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    || !Environment.isExternalStorageRemovable()) {
// 獲取SD卡根目錄
                return context.getExternalCacheDir().getPath();
            } else {
// 獲取apk包下的快取路徑
                return context.getCacheDir().getPath();
            }
        }
        /**
         * 使用當前系統時間作為上傳圖片的名稱
         *
         * @return 儲存的根路徑 圖片名稱
         */
        public static String getPhotoFileName(Context context) {
            File file = new File(getPhoneRootPath(context),FILES_NAME);
// 判斷檔案是否已經存在，不存在則建立
            if (!file.exists()) {
                file.mkdirs();
            }
// 設定圖片檔名稱
            SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE, Locale.getDefault());
            Date date = new Date(System.currentTimeMillis());
            String time = format.format(date);
            String photoName = "/"+time+IMAGE_TYPE;
            return file+photoName;
        }
        /**
         * 儲存Bitmap圖片在SD卡中
         * 如果沒有SD卡則存在手機中
         *
         * @param mbitmap 需要儲存的Bitmap圖片
         * @return 儲存成功時返回圖片的路徑，失敗時返回null
         */
        public static String savePhotoToSD(Bitmap mbitmap, Context context) {
            FileOutputStream outStream = null;
            String fileName = getPhotoFileName(context);
            try {
                outStream = new FileOutputStream(fileName);
// 把資料寫入檔案，100表示不壓縮
                mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (outStream != null) {
// 記得要關閉流！
                        outStream.close();
                    }
                    if (mbitmap != null) {
                        mbitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         * 把原圖按1/10的比例壓縮
         *
         * @param path 原圖的路徑
         * @return 壓縮後的圖片
         */
        public static Bitmap getCompressPhoto(String path) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 14;  // 圖片的大小設定為原來的十分之一
            Bitmap bmp = BitmapFactory.decodeFile(path, options);
            options = null;
            return bmp;
        }
        /**
         * 處理旋轉後的圖片
         * @param originpath 原圖路徑
         * @param context 上下文
         * @return 返回修復完畢後的圖片路徑
         */
        public static String amendRotatePhoto(String originpath, Context context) {
// 取得圖片旋轉角度
            int angle = readPictureDegree(originpath);
// 把原圖壓縮後得到Bitmap物件
            Bitmap bmp = getCompressPhoto(originpath);;
// 修復圖片被旋轉的角度
            Bitmap bitmap = rotaingImageView(angle, bmp);
// 儲存修復後的圖片並返回儲存後的圖片路徑
            return savePhotoToSD(bitmap, context);
        }
        /**
         * 讀取照片旋轉角度
         *
         * @param path 照片路徑
         * @return 角度
         */
        public static int readPictureDegree(String path) {
            int degree = 0;
            try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return degree;
        }
        /**
         * 旋轉圖片
         * @param angle 被旋轉角度
         * @param bitmap 圖片物件
         * @return 旋轉後的圖片
         */
        public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
            Bitmap returnBm = null;
// 根據旋轉角度，生成旋轉矩陣
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            try {
// 將原始圖片按照旋轉矩陣進行旋轉，並得到新的圖片
                returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (OutOfMemoryError e) {
            }
            if (returnBm == null) {
                returnBm = bitmap;
            }
            if (bitmap != returnBm) {
                bitmap.recycle();
            }
            return returnBm;
        }

        /*取得今天日期 */
        public static java.sql.Date getSqlToday() {
            //=== 取得今天日期===
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return java.sql.Date.valueOf(df.format(new Date()));
        }

    public static String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] cpicture = baos.toByteArray();
//        bitmap.recycle();
        String img = Base64.encodeToString(cpicture, 6);
        return img;
    }


    /**
     * 讀取圖片的旋轉的角度
     *
     * @param path  女
     *
     * @return 圖片的旋轉角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 從指定路徑下讀取圖片，並獲取其EXIF資訊
            ExifInterface exifInterface = new ExifInterface(path);
            // 獲取圖片的旋轉資訊
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 將圖片按照某個角度進行旋轉
     *
     * @param bm
     *            需要旋轉的圖片
     * @param degree
     *            旋轉角度
     * @return 旋轉後的圖片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根據旋轉角度，生成旋轉矩陣
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 將原始圖片按照旋轉矩陣進行旋轉，並得到新的圖片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    //日期加減，第一個參數輸入日期yyyy-MM-dd，第二個參數輸入要增加的天數，若要減少則輸入負數
    public  static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date nowDate = null;
        try {
            nowDate =df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }
    //字串轉UtilDate
    public  static Date StringCastUtilDate (String yyyy_mm_dd){
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
        String  str = "2018-03-02";
        Date sdf =null;
        try {
            sdf = f.parse(yyyy_mm_dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf;
    }

}


