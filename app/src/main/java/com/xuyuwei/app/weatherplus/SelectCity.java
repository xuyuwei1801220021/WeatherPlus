package com.xuyuwei.app.weatherplus;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xuyuwei.app.app.MyApplication;
import com.xuyuwei.app.com.xuyuwei.app.bean.City;
import com.xuyuwei.app.db.CityDB;

import java.util.ArrayList;
import java.util.List;
import com.xuyuwei.app.app.MyApplication;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackbtn;
    private ListView mlistview;
    private List<City> cityList;
    private CityDB mCityDB;
    private  ArrayList<City> filterDateList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mBackbtn = (ImageView) findViewById(R.id.title_back);
        mBackbtn.setOnClickListener(this);
        initViews();
    }

    private void initViews() {
        mlistview = (ListView) findViewById((R.id.list_view));
       MyApplication myApplication = (MyApplication) getApplication();
       cityList = myApplication.getCityList();
        String[] cityname = new String[10000];
        final String[] citycode = new String[10000];

        int i=0;
        for(City city:cityList){
            cityname[i]=city.getCity();
            citycode[i]=city.getNumber();
            i++;
        }
       ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,cityname);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent();
                i.putExtra("cityCode", citycode[position]);
                setResult(RESULT_OK, i);
                finish();
                //Toast.makeText(SelectCity.this, "Äãµ¥»÷ÁË:"+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
                Intent i=new Intent();
                i.putExtra("cityCode", "101160101");
                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }
}