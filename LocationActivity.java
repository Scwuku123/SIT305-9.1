package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.myapplication.Utils.SPUtils;
import com.example.myapplication.db.ShopLoc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    百度地图应用，包含定位信息和地图显示
    一般需要打开定位服务，选择高精度定位模式，有网络连接
    需要在清单文件里使用百度云服务（参见清单文件service标签）
    需要创建应用（模块）的Key，并写入清单文件（参见清单文件meta标签）
*/
public class LocationActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener {

    LocationClient mLocationClient;  //定位客户端
    MapView mapView;  //Android Widget地图控件
    BaiduMap baiduMap;
    boolean isFirstLocate = true;
    private List<ShopLoc> shopLocs;
    private DBManager mUserDataManager;         //用户数据管理类

    TextView tv_Lat;  //纬度
    TextView tv_Lon;  //经度
    TextView tv_Add;  //地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果没有定位权限，动态请求用户允许使用该权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            requestLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "没有定位权限！", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    requestLocation();
                }
        }
    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation() {  //初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);

        mapView = findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        tv_Lat = findViewById(R.id.tv_Lat);
        tv_Lon = findViewById(R.id.tv_Lon);
        tv_Add = findViewById(R.id.tv_Add);

        LocationClientOption option = new LocationClientOption();
        //设置扫描时间间隔
        option.setScanSpan(1000);
        //设置定位模式，三选一
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);*/
        //设置需要地址信息
        option.setIsNeedAddress(true);
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);

        //保存定位参数
        mLocationClient.setLocOption(option);
        baiduMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    //内部类，百度位置监听器
    private class MyLocationListener  implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            tv_Lat.setText(bdLocation.getLatitude()+"");
            tv_Lon.setText(bdLocation.getLongitude()+"");
            tv_Add.setText(bdLocation.getAddrStr());
            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
                    true, null );
            baiduMap.setMyLocationConfiguration(configuration);
            baiduMap.animateMapStatus(update);
            baiduMap.setMyLocationData(locData);
            MapStatus.Builder builder = new MapStatus.Builder();
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            builder.target(ll).zoom(12.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            //构建Marker图标
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_mark);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions overlayOptions = new MarkerOptions()
                    .position(latLng)
                    .title(bdLocation.getAddrStr())
                    .icon(bitmapDescriptor);
            //在地图上添加Marker，并显示
            baiduMap.addOverlay(overlayOptions);

            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation || bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(bdLocation);
            }
        }
    }
    private void navigateTo(BDLocation bdLocation) {
        if(isFirstLocate){
            LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
            if (getIntent().getIntExtra("all",0)!=0)
                generateShop();
        }
    }

    private void generateShop() {
        shopLocs=new ArrayList<>();

        if (mUserDataManager == null) {
            mUserDataManager = new DBManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
        mUserDataManager.openDataBase();
        Cursor cursor=mUserDataManager.fetchAllUserDatas();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String title= cursor.getString(cursor.getColumnIndex(DBManager.USER_HEIGHT));
                String strlat= SPUtils.getString(title+"_lat");
                String strlon=SPUtils.getString(title+"_lon");
                if (strlat!=null && strlon!=null){
                    double lat=Double.valueOf(strlat);
                    double lon =Double.valueOf(strlon);
                    String name=title;
                    ShopLoc shopLoc=new ShopLoc(0+"",name,lat+"",lon+"",null);
                    shopLocs.add(shopLoc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ShowUserInfoActivity","error:"+e.getMessage());
        }
        mHandler.sendEmptyMessageDelayed(2, 3000);

    }
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    for (ShopLoc shopLoc:shopLocs) {
                        Double lat = Double.valueOf(shopLoc.getLat());
                        Double lon = Double.valueOf(shopLoc.getLon());
                        LatLng point=new LatLng(lat,lon);
                        //构建Marker图标
                        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_mark);
                        //构建MarkerOption，用于在地图上添加Marker
                        OverlayOptions overlayOptions = new MarkerOptions()
                                .position(point)
                                .icon(bitmapDescriptor);
                        //在地图上添加Marker，并显示
                        baiduMap.addOverlay(overlayOptions);

                    }
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public boolean onMarkerClick(Marker marker) {
        String title=marker.getTitle();
        Intent intent = new Intent();
        intent.putExtra("loc",title);
        SPUtils.putString(title+"_lat",marker.getPosition().latitude+"");
        SPUtils.putString(title+"_lon",marker.getPosition().longitude+"");
        setResult(0, intent);
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView=(MapView)findViewById(R.id.bmapView);
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView=(MapView)findViewById(R.id.bmapView);
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
    }
}
