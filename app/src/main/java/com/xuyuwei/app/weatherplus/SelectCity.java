package com.xuyuwei.app.weatherplus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.xuyuwei.app.app.MyApplication;
import com.xuyuwei.app.com.xuyuwei.app.bean.City;
import com.xuyuwei.app.db.CityDB;

import java.util.List;
import com.xuyuwei.app.app.MyApplication;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackbtn;
    private ListView mlistview;
    private List<City> cityList;
    private CityDB mCityDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mBackbtn = (ImageView) findViewById(R.id.title_back);
        mBackbtn.setOnClickListener(this);
        //initViews();
    }

    private void initViews() {
        mlistview = (ListView) findViewById((R.id.list_view));
        String[] data={"��1��","��2��","��3��","��4��","��5��","��6��",
                "��7��","��8��","��9��","��10��","��11��","��12��","��13��",
                "��14��","��15��","��16��","��17��","��18��","��19��","��20��",
                "��21��","��22��"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
               SelectCity.this,android.R.layout.simple_list_item_1,data);
        mlistview.setAdapter(adapter);
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