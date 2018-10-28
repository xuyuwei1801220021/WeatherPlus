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
        String[] data={"第1组","第2组","第3组","第4组","第5组","第6组",
                "第7组","第8组","第9组","第10组","第11组","第12组","第13组",
                "第14组","第15组","第16组","第17组","第18组","第19组","第20组",
                "第21组","第22组"};

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