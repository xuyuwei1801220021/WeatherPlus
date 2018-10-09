package com.xuyuwei.app.weatherplus;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xuyuwei.app.util.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements View.OnClickListener{
    private ImageView mUpdataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_logo);

        mUpdataBtn = (ImageView)findViewById(R.id.title_update_btn);
        mUpdataBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("WeatherPlus","网络正常");
            Toast.makeText(MainActivity.this,"网络正常",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("WeatherPlus","网络异常");
            Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
        }
    }


//    获取城市名称
    private void queryWeatherCode(String cityCode){
        final  String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("WeatherPlus",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("WeatherPlus",str);
                    }
                    String responseStr= response.toString();
                    Log.d("WeatherPlus",responseStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //更新事件
    @Override
    public void onClick(View view){
        if (view.getId() == R.id.title_update_btn){
            SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");
            Log.d("WeatherPlus",cityCode);

            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("WeatherPlus","网络正常");
                queryWeatherCode(cityCode);
            }else{
                Log.d("WeatherPlus","网络异常");
                Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
            }

        }
    }
}