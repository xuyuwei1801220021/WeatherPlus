package com.xuyuwei.app.app;

import android.app.Application;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xuyuwei.app.com.xuyuwei.app.bean.City;
import com.xuyuwei.app.db.CityDB;
import com.xuyuwei.app.weatherplus.R;
import com.xuyuwei.app.weatherplus.SelectCity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {
    private  static final String TAG = "MyApp";

    private static MyApplication mApplication;
    private CityDB mCityDB;

    private String[] cityname;
    private String[] citycode;

    private List<City> mCityList;
    private List<City> sCityList;
    private ListView mlistview;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "MyApplication->Oncreate");
        mApplication = this;
//
       mCityDB = openCityDB();
        initCityList();
//        mCityList = mCityDB.getAllCity();
//        mlistview = (ListView) mlistview.findViewById((R.id.list_view));
//        String[] cityName={"0"};
//        int i=0;
//        for (City city : mCityList) {
//            cityName[i] = city.getCity();
//            i++;
//        }
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
//                MyApplication.this,android.R.layout.simple_list_item_1,cityName);
//        mlistview.setAdapter(adapter);
    }

    private void initCityList(){
        mCityList = new ArrayList<City>();
        sCityList = new ArrayList<City>();
       new Thread(new Runnable(){
           @Override
       public void run(){
               prepareCityList();
           }
       }).start();
    }
    private boolean prepareCityList() {
        mCityList = mCityDB.getAllCity();
        int i=0;
//        for (City city : mCityList) {
//            i++;
//            String cityName = city.getCity();
//            String cityCode = city.getNumber();
//            //cityname[i]=cityName;
////            citycode[i]=cityCode;
//            Log.d(TAG,cityCode+":"+cityName);
//        }
//        Intent intent = new Intent(MyApplication.this,SelectCity.class);
//        intent.putExtra("CityName",cityname);
//        intent.putExtra("CityCode",ci)
//        startActivity(intent);

        Log.d(TAG, "i=" + i);
        return true;
    }
    public List<City> getCityList() {
        return mCityList;
    }
    public List<City> getSearchCityList(String sname) {
        sCityList = mCityDB.getSearchCity(sname);
        int i=0;
        for (City city : sCityList) {
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            //cityname[i]=cityName;
//            citycode[i]=cityCode;
            Log.d(TAG,cityCode+":"+cityName);
        }
        return sCityList;
    }
    public static MyApplication getInstance(){
        return mApplication;
    }
    private CityDB openCityDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        Log.d(TAG, "dbpath=" + path);
        File db = new File(path);
        Log.d(TAG,path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;

            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
}