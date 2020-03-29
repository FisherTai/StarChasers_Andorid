package com.example.starChasers.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.starChasers.myutil.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class CommonTask extends AsyncTask<String,Integer,String> {
    private final static String TAG ="CommonTask";
    private String url,outStr;

    public CommonTask(String url ,String outStr){
        this.url = url;
        this.outStr =outStr;
    }


    protected String doInBackground(String... strings){return getRemoteData();}

    private String getRemoteData(){
        HttpURLConnection connection = null;
        StringBuilder inStr =new StringBuilder();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(15 * 1000);
            connection.setReadTimeout(15 * 1000);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "url: " + url);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code:" + responseCode);
            }
        }   catch (SocketTimeoutException e){
            Log.e(TAG,"SocketTimeoutException:"+e.toString());
            Util.confail = true;
            System.out.println("連線逾時");
        }   catch (IOException e){
            Log.e(TAG,e.toString());
        }   finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }

}
