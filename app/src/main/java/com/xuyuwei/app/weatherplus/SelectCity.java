package com.xuyuwei.app.weatherplus;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.xuyuwei.app.app.MyApplication;
import com.xuyuwei.app.com.xuyuwei.app.bean.City;
import com.xuyuwei.app.db.CityDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.xuyuwei.app.app.MyApplication;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackbtn;
    private ListView mlistview;
    private List<City> cityList;
    private List<City> mCityList;
    private SearchView searchView;
    private CityDB mCityDB;
    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mEditText = (EditText) findViewById(R.id.search_city);

        TextWatcher mTextWatcher = new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                temp = charSequence;
                Log.d("WeatherPlus", "beforeTextChanged:" + temp);
                //searchView(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("WeatherPlus", "onTextChanged:" + charSequence);
                //mCityList = mCityDB.getSearchCity(charSequence.toString());
                temp = charSequence;
                if (temp.toString().isEmpty()){

                }
                else{
                    searchView(temp);
                }

                //Toast.makeText(SelectCity.this,charSequence.toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editStart = mEditText.getSelectionStart();
                editEnd = mEditText.getSelectionEnd();

                if (temp.length()>10){
                    Toast.makeText(SelectCity.this,"超出限制",Toast.LENGTH_SHORT).show();
                    editable.delete(editStart-1,editEnd);
                    int tempSelection = editStart;
                    mEditText.setText(editable);
                    mEditText.setSelection(tempSelection);
                }
                Log.d("WeatherPlus","afterTextChanged");
            }
        };
        mEditText.addTextChangedListener(mTextWatcher);
        mBackbtn = (ImageView) findViewById(R.id.title_back);
        mBackbtn.setOnClickListener(this);
        initViews();

    }
    private  void searchView(CharSequence charSequence) {
        mlistview = (ListView) findViewById((R.id.list_view));
        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();
//        String[] cityname = new String[cityList.size()];
//        final String[] citycode = new String[cityList.size()];

        int j = 0;
        for (City city : cityList) {
            if (city.getCity().contains(charSequence)) {
//                cityname[j] = city.getCity();
//                citycode[j] = city.getNumber();
//                Log.d("shuchu:", cityname[j]+" "+citycode[j]);
                j++;

            }

        }
        String[] cityname = new String[j];
        final String[] citycode = new String[j];
        int p=0;
        for (City city : cityList) {
            if (city.getCity().contains(charSequence)) {
                cityname[p] = city.getCity();
                citycode[p] = city.getNumber();
                Log.d("shuchu:", cityname[p]+" "+citycode[p]);
                p++;

            }

        }

        setresult(cityname,citycode);
    }
    private void setresult(String[] cityname, final String[] citycode){
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,cityname);
        for (int y=0;y<cityname.length;y++)
        Log.d("city:",cityname[y]+" "+y);
        if (cityname.toString().equals(null)){

        }
        else{
            mlistview.setAdapter(adapter);
            mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i=new Intent();
                    i.putExtra("cityCode", citycode[position]);
                    setResult(RESULT_OK, i);
                    finish();
                    //Toast.makeText(SelectCity.this, "你单击了:"+position, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    private void initViews() {
        mlistview = (ListView) findViewById((R.id.list_view));
       MyApplication myApplication = (MyApplication) getApplication();
       cityList = myApplication.getCityList();
        String[] cityname = new String[cityList.size()];
            final String[] citycode = new String[cityList.size()];

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
                //Toast.makeText(SelectCity.this, "你单击了:"+position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_back:
//                Intent i=new Intent();
//                i.putExtra("cityCode", "101160101");
//                setResult(RESULT_OK, i);
                finish();
                break;
            default:
                break;
        }
    }
}