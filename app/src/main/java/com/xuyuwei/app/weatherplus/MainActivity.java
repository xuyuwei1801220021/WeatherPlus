package com.xuyuwei.app.weatherplus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xuyuwei.app.adapter.MyAdapter;
import com.xuyuwei.app.com.xuyuwei.app.bean.TodayWeather;
import com.xuyuwei.app.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.util.logging.Handler;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity  implements View.OnClickListener ,ViewPager.OnPageChangeListener{
    private static final int UPDATE_TODAY_WEATHER = 1;

    private ImageView mUpdataBtn;
    private ProgressBar progressBar;
    private ProgressBar progressBarB;

    private ImageView mCitySelect;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,
                      temperatureTv,climateTv,windTv,city_name_Tv;
    private ImageView weatherImg,pmImg;
    private String nowcity="101010100";

    private ViewPager viewPager;
    private List<View> viewPagerList;
    //六天天?信息展示
//显示两个展示?
    private MyAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    //为引导?增加?圆点
    private ImageView[] dots; //存放?圆点的集合
    private int[] ids = {R.id.iv1,R.id.iv2};
    private ImageView weatherImg0,weatherImg1,weatherImg2,weatherImg3,weatherImg4,weatherImg5;
    private TextView
            week_today,temperature,climate,wind,
            week_today1,temperature1,climate1,wind1,
            week_today2,temperature2,climate2,wind2;
    private TextView
            week_today3,temperature3,climate3,wind3,
            week_today4,temperature4,climate4,wind4,
            week_today5,temperature5,climate5,wind5;

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_PROGRESS);
        //绑定布局文件
        setContentView(R.layout.weather_logo);

        mUpdataBtn = (ImageView)findViewById(R.id.title_update_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBarB = (ProgressBar) findViewById(R.id.progressB);
        //设置按钮监听
        mUpdataBtn.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
            Log.d("WeatherPlus","网络正常");
            Toast.makeText(MainActivity.this,"网络正常",Toast.LENGTH_LONG).show();
        }else
        {
            Log.d("WeatherPlus","网络异常");
            Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
        }
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        //初始化控件内容
        initView();
        //初始化两个滑动??
        initViews();
        
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.tianqilayout1, null));
        views.add(inflater.inflate(R.layout.tianqilayout2, null));
        week_today = (TextView) views.get(0).findViewById(R.id.week_today0);
        temperature =(TextView) views.get(0).findViewById(R.id.temperature0);
        climate =(TextView) views.get(0).findViewById(R.id.climate0);
        wind = (TextView) views.get(0).findViewById(R.id.wind0);
        weatherImg0 = (ImageView) views.get(0).findViewById(R.id.weather_img0);

        week_today1 = (TextView) views.get(0).findViewById(R.id.week_today1);
        temperature1 =(TextView) views.get(0).findViewById(R.id.temperature1);
        climate1 =(TextView) views.get(0).findViewById(R.id.climate1);
        wind1 = (TextView) views.get(0).findViewById(R.id.wind1);
        weatherImg1 = (ImageView) views.get(0).findViewById(R.id.weather_img1);

        week_today2 = (TextView) views.get(0).findViewById(R.id.week_today2);
        temperature2 =(TextView) views.get(0).findViewById(R.id.temperature2);
        climate2 =(TextView) views.get(0).findViewById(R.id.climate2);
        wind2 = (TextView) views.get(0).findViewById(R.id.wind2);
        weatherImg2 = (ImageView) views.get(0).findViewById(R.id.weather_img2);

        week_today3 = (TextView) views.get(1).findViewById(R.id.week_today3);
        temperature3 =(TextView) views.get(1).findViewById(R.id.temperature3);
        climate3 =(TextView) views.get(1).findViewById(R.id.climate3);
        wind3 = (TextView) views.get(1).findViewById(R.id.wind3);
        weatherImg3 = (ImageView) views.get(1).findViewById(R.id.weather_img3);

        week_today4 = (TextView) views.get(1).findViewById(R.id.week_today4);
        temperature4 =(TextView) views.get(1).findViewById(R.id.temperature4);
        climate4 =(TextView) views.get(1).findViewById(R.id.climate4);
        wind4 = (TextView) views.get(1).findViewById(R.id.wind4);
        weatherImg4 = (ImageView) views.get(1).findViewById(R.id.weather_img4);

        week_today5 = (TextView) views.get(1).findViewById(R.id.week_today5);
        temperature5 =(TextView) views.get(1).findViewById(R.id.temperature5);
        climate5 =(TextView) views.get(1).findViewById(R.id.climate5);
        wind5 = (TextView) views.get(1).findViewById(R.id.wind5);
        weatherImg5 = (ImageView) views.get(1).findViewById(R.id.weather_img5);

        week_today.setText("N/A");
        temperature.setText("N/A");
        climate.setText("N/A");
        wind.setText("N/A");

        week_today1.setText("N/A");
        temperature1.setText("N/A");
        climate1.setText("N/A");
        wind1.setText("N/A");

        week_today2.setText("N/A");
        temperature2.setText("N/A");
        climate2.setText("N/A");
        wind2.setText("N/A");

        week_today3.setText("N/A");
        temperature3.setText("N/A");
        climate3.setText("N/A");
        wind3.setText("N/A");

        week_today4.setText("N/A");
        temperature4.setText("N/A");
        climate4.setText("N/A");
        wind4.setText("N/A");

        week_today5.setText("N/A");
        temperature5.setText("N/A");
        climate5.setText("N/A");
        wind5.setText("N/A");

        vpAdapter = new MyAdapter(views,this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        //为pageviewer配置监听事件
        vp.setOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //初始化控件内容
    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);




        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");




    }

//    获取城市名称
    private void queryWeatherCode(String cityCode){
        final  String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("WeatherPlus",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather=null;
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
                    todayWeather=parseXML(responseStr);
                    if (todayWeather!=null){
                        Log.d("WeaterPlus",todayWeather.toString());
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    //更新事件
    @Override
    public void onClick(final View view){
        if(view.getId() == R.id.title_city_manager){
            Intent i = new Intent(this,SelectCity.class);
            //startActivity(i);
            startActivityForResult(i,1);
        }
        if (view.getId() == R.id.title_update_btn){
            progressBar.setVisibility(View.VISIBLE);
            mUpdataBtn.setVisibility(View.INVISIBLE);
            progressBarB.setVisibility(View.VISIBLE);

            String newCityCode=nowcity;
            if (newCityCode==null){
                newCityCode="101010100";
            }
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            final String cityCode = sharedPreferences.getString("main_city_code", newCityCode);//101160101
            Log.d("WeatherPlus"," "+newCityCode);
            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int i = 0;
                        while (i < 100 && progressBar.getProgress() != 100) {
                            try {
                                Thread.sleep(20);
                                // 更新进度条的进度,可以在子线程中更新进度条进度
                                progressBar.incrementProgressBy(30);
                                progressBarB.incrementProgressBy(30);
                                i++;
//                                if (progressBar.getProgress()>100){
//                                    progressBar.setVisibility(View.INVISIBLE);
//                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                        // 在进度条走完时删除Dialog
                        queryWeatherCode(cityCode);

                    }
                }).start();
                Log.d("WeatherPlus","网络正常");



                //progressBar.setVisibility(view.INVISIBLE);
//                queryWeatherCode(cityCode);
            } else {
                Log.d("WeatherPlus","网络异常");
                Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_LONG).show();
            }

        }
    }
    //解析函数
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try{
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int evenType = xmlPullParser.getEventType();
            Log.d("WeatherPlus","parseXML");
            while (evenType != XmlPullParser.END_DOCUMENT){
                switch (evenType){
                    //判断开始事件\
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断结束事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather=new TodayWeather();
                        }
                        if (todayWeather!=null){
                            if(xmlPullParser.getName().equals("city")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","city:" + xmlPullParser.getText());
                                todayWeather.setCity(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("updatetime")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","updatetime:" +xmlPullParser.getText());
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("shidu")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","shidu:" +xmlPullParser.getText());
                                todayWeather.setShidu(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("wendu")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","wendu:" +xmlPullParser.getText());
                                todayWeather.setWendu(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("pm25")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","PM2.5:" +xmlPullParser.getText());
                                todayWeather.setPm25(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("quality")){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","quality:" +xmlPullParser.getText());
                                todayWeather.setQuality(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("fengxiang")&&fengxiangCount ==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengxiang:" +xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengxiang")&&fengxiangCount ==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengxiang:" +xmlPullParser.getText());
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("fengli")&&fengliCount==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli:" +xmlPullParser.getText());
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==1){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli1:" +xmlPullParser.getText());
                                todayWeather.setWind1(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==2){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli2:" +xmlPullParser.getText());
                                todayWeather.setWind2(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==3){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli3:" +xmlPullParser.getText());
                                todayWeather.setWind3(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==4){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli4:" +xmlPullParser.getText());
                                todayWeather.setWind4(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli")&&fengliCount==5){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","fengli5:" +xmlPullParser.getText());
                                todayWeather.setWind5(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if (xmlPullParser.getName().equals("date")&&dateCount==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","date:" +xmlPullParser.getText());
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date")&&dateCount==1){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","week_today1:" +xmlPullParser.getText());
                                todayWeather.setWeek_today1(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if (xmlPullParser.getName().equals("date")&&dateCount==2){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","week_today2:" +xmlPullParser.getText());
                                todayWeather.setWeek_today2(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date")&&dateCount==3){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","week_today3:" +xmlPullParser.getText());
                                todayWeather.setWeek_today3(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date")&&dateCount==4){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","week_today4:" +xmlPullParser.getText());
                                todayWeather.setWeek_today4(xmlPullParser.getText());
                                dateCount++;
                            }else if (xmlPullParser.getName().equals("date")&&dateCount==5){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","week_today5:" +xmlPullParser.getText());
                                todayWeather.setWeek_today5(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if(xmlPullParser.getName().equals("high")&&highCount==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high:" +xmlPullParser.getText());
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if(xmlPullParser.getName().equals("high")&&highCount==1){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high1:" +xmlPullParser.getText());
                                todayWeather.setTemperatureH1(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if(xmlPullParser.getName().equals("high")&&highCount==2){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high2:" +xmlPullParser.getText());
                                todayWeather.setTemperatureH2(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if(xmlPullParser.getName().equals("high")&&highCount==3){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high3:" +xmlPullParser.getText());
                                todayWeather.setTemperatureH3(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if(xmlPullParser.getName().equals("high")&&highCount==4){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high4:" +xmlPullParser.getText());
                                todayWeather.setTemperatureH4(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }else if(xmlPullParser.getName().equals("high")&&highCount==5){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","high5:" +xmlPullParser.getText());
                                todayWeather.setTemperatureH(xmlPullParser.getText().substring(2).trim());
                                highCount++;
                            }
                            else if (xmlPullParser.getName().equals("low")&&lowCount==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low:" +xmlPullParser.getText());
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low")&&lowCount==1){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low1:" +xmlPullParser.getText());
                                todayWeather.setTemperatureL1(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low")&&lowCount==2){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low2:" +xmlPullParser.getText());
                                todayWeather.setTemperatureL2(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low")&&lowCount==3){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low3:" +xmlPullParser.getText());
                                todayWeather.setTemperatureL3(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low")&&lowCount==4){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low4:" +xmlPullParser.getText());
                                todayWeather.setTemperatureL4(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low")&&lowCount==5){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","low5:" +xmlPullParser.getText());
                                todayWeather.setTemperatureL5(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }
                            else if (xmlPullParser.getName().equals("type")&&typeCount==0){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type:" +xmlPullParser.getText());
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            } else if (xmlPullParser.getName().equals("type")&&typeCount==1){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type1:" +xmlPullParser.getText());
                                todayWeather.setClimate1(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type")&&typeCount==2){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type2:" +xmlPullParser.getText());
                                todayWeather.setClimate2(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type")&&typeCount==3){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type3:" +xmlPullParser.getText());
                                todayWeather.setClimate3(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type")&&typeCount==4){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type4:" +xmlPullParser.getText());
                                todayWeather.setClimate4(xmlPullParser.getText());
                                typeCount++;
                            }else if (xmlPullParser.getName().equals("type")&&typeCount==5){
                                evenType = xmlPullParser.next();
                                Log.d("WeatherPlus","type5:" +xmlPullParser.getText());
                                todayWeather.setClimate5(xmlPullParser.getText());
                                typeCount++;
                            }

                        }

                        break;
                    //判断当前事件是否为结束事件

                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                evenType = xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    return todayWeather;
    }
    //返回城市信息
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            nowcity=newCityCode;
            Log.d("WeatherPlus", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("WeatherPlus", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("WeatherPlus", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
//更新页面信息
    void updateTodayWeather(TodayWeather todayWeather){
        // TodayWeather{city=北京', updatetime='17:46',wendu='17',shudu='45%',
        // pm25='90',quality='轻度污染',fengxiang='北风',fengli='1级',date='
        // 13日星期六',high='20℃',low='8℃',type='晴'}
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());

        wind.setText(todayWeather.getFengli());
        week_today.setText(todayWeather.getDate());
        temperature.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climate.setText(todayWeather.getType());

        if(todayWeather.getType().equals("晴")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));

        }
        if(todayWeather.getType().equals("暴雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getType().equals("暴雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getType().equals("大暴雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getType().equals("大雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getType().equals("大雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getType().equals("多云")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getType().equals("雷阵雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getType().equals("雷阵雨冰雹")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getType().equals("沙尘暴")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getType().equals("特大暴雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getType().equals("小雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getType().equals("小雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getType().equals("阴")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getType().equals("雨夹雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getType().equals("阵雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getType().equals("阵雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getType().equals("中雪")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getType().equals("中雨")){
            weatherImg0.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }

        week_today1.setText(todayWeather.getWeek_today1());
        temperature1.setText(todayWeather.getTemperatureH1()+"~"+todayWeather.getTemperatureL1());
        climate1.setText(todayWeather.getClimate1());
        wind1.setText(todayWeather.getWind1());

        if(todayWeather.getClimate1().equals("晴")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
        }
        if(todayWeather.getClimate1().equals("暴雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getClimate1().equals("暴雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getClimate1().equals("大暴雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getClimate1().equals("大雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getClimate1().equals("大雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getClimate1().equals("多云")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getClimate1().equals("雷阵雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getClimate1().equals("雷阵雨冰雹")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getClimate1().equals("沙尘暴")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getClimate1().equals("特大暴雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getClimate1().equals("小雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getClimate1().equals("小雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getClimate1().equals("阴")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getClimate1().equals("雨夹雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getClimate1().equals("阵雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getClimate1().equals("阵雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getClimate1().equals("中雪")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getClimate1().equals("中雨")){
            weatherImg1.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }

        week_today2.setText(todayWeather.getWeek_today2());
        temperature2.setText(todayWeather.getTemperatureH2()+"~"+todayWeather.getTemperatureL2());
        climate2.setText(todayWeather.getClimate2());
        wind2.setText(todayWeather.getWind2());
        if(todayWeather.getClimate2().equals("晴")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
        }
        if(todayWeather.getClimate2().equals("暴雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getClimate2().equals("暴雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getClimate2().equals("大暴雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getClimate2().equals("大雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getClimate2().equals("大雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getClimate2().equals("多云")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getClimate2().equals("雷阵雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getClimate2().equals("雷阵雨冰雹")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getClimate2().equals("沙尘暴")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getClimate2().equals("特大暴雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getClimate2().equals("小雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getClimate2().equals("小雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getClimate2().equals("阴")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getClimate2().equals("雨夹雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getClimate2().equals("阵雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getClimate2().equals("阵雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getClimate2().equals("中雪")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getClimate2().equals("中雨")){
            weatherImg2.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }

        week_today3.setText(todayWeather.getWeek_today3());
        temperature3.setText(todayWeather.getTemperatureH3()+"~"+todayWeather.getTemperatureL3());
        climate3.setText(todayWeather.getClimate3());
        wind3.setText(todayWeather.getWind3());

        if(todayWeather.getClimate3().equals("晴")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
        }
        if(todayWeather.getClimate3().equals("暴雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getClimate3().equals("暴雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getClimate3().equals("大暴雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getClimate3().equals("大雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getClimate3().equals("大雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getClimate3().equals("多云")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getClimate3().equals("雷阵雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getClimate3().equals("雷阵雨冰雹")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getClimate3().equals("沙尘暴")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getClimate3().equals("特大暴雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getClimate3().equals("小雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getClimate3().equals("小雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getClimate3().equals("阴")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getClimate3().equals("雨夹雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getClimate3().equals("阵雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getClimate3().equals("阵雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getClimate3().equals("中雪")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getClimate3().equals("中雨")){
            weatherImg3.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }
        week_today4.setText(todayWeather.getWeek_today4());
        temperature4.setText(todayWeather.getTemperatureH4()+"~"+todayWeather.getTemperatureL4());
        climate4.setText(todayWeather.getClimate4());
        wind4.setText(todayWeather.getWind4());
        if(todayWeather.getClimate4().equals("晴")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
        }
        if(todayWeather.getClimate4().equals("暴雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getClimate4().equals("暴雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getClimate4().equals("大暴雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getClimate4().equals("大雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getClimate4().equals("大雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getClimate4().equals("多云")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getClimate4().equals("雷阵雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getClimate4().equals("雷阵雨冰雹")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getClimate4().equals("沙尘暴")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getClimate4().equals("特大暴雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getClimate4().equals("小雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getClimate4().equals("小雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getClimate4().equals("阴")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getClimate4().equals("雨夹雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getClimate4().equals("阵雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getClimate4().equals("阵雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getClimate4().equals("中雪")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getClimate4().equals("中雨")){
            weatherImg4.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }

        week_today5.setText(todayWeather.getWeek_today5());
        temperature5.setText(todayWeather.getTemperatureH5()+"~"+todayWeather.getTemperatureL5());
        climate5.setText(todayWeather.getClimate5());
        wind5.setText(todayWeather.getWind5());

        if(todayWeather.getClimate5().equals("晴")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
        }
        if(todayWeather.getClimate5().equals("暴雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getClimate5().equals("暴雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getClimate5().equals("大暴雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getClimate5().equals("大雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getClimate5().equals("大雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getClimate5().equals("多云")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getClimate5().equals("雷阵雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getClimate5().equals("雷阵雨冰雹")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getClimate5().equals("沙尘暴")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getClimate5().equals("特大暴雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getClimate5().equals("小雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getClimate5().equals("小雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getClimate5().equals("阴")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getClimate5().equals("雨夹雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getClimate5().equals("阵雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getClimate5().equals("阵雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getClimate5().equals("中雪")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getClimate5().equals("中雨")){
            weatherImg5.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }
        int pm2_5=Integer.valueOf(todayWeather.getPm25()).intValue();
       if(pm2_5==-1){
           pmDataTv.setText("无");
       }
        if (pm2_5<=50){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_0_50));
        }
       else if ((pm2_5<=100)
                &&(pm2_5>50)){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_51_100));
        }
       else if ((pm2_5<=150)
                &&(pm2_5>100)){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_101_150));
        }
       else if ((pm2_5<=200)
                &&(pm2_5>150)){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_151_200));
        }
       else if ((pm2_5<=300)
                &&(pm2_5>200)){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_201_300));
        }
       else if ((pm2_5>300)){
            pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_greater_300));
        }
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        if(todayWeather.getType().equals("晴")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));

        }
        if(todayWeather.getType().equals("暴雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
        }
        if(todayWeather.getType().equals("暴雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
        }
        if(todayWeather.getType().equals("大暴雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
        }
        if(todayWeather.getType().equals("大雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
        }
        if(todayWeather.getType().equals("大雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
        }
        if(todayWeather.getType().equals("多云")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
        }
        if(todayWeather.getType().equals("雷阵雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
        }
        if(todayWeather.getType().equals("雷阵雨冰雹")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
        }
        if(todayWeather.getType().equals("沙尘暴")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
        }
        if(todayWeather.getType().equals("特大暴雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
        }
        if(todayWeather.getType().equals("小雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
        }
        if(todayWeather.getType().equals("小雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
        }
        if(todayWeather.getType().equals("阴")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
        }
        if(todayWeather.getType().equals("雨夹雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
        }
        if(todayWeather.getType().equals("阵雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
        }
        if(todayWeather.getType().equals("阵雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
        }
        if(todayWeather.getType().equals("中雪")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
        }
        if(todayWeather.getType().equals("中雨")){
            weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
        }
        windTv.setText("风力:" + todayWeather.getFengli());
        progressBar.setVisibility(View.INVISIBLE);
        progressBarB.setVisibility(View.INVISIBLE);
        mUpdataBtn.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
    }


}