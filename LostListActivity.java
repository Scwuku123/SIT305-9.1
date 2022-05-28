package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LostListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private DBManager mDBManager;         //用户数据管理类
    private List<LostDb> lostDbList = new ArrayList<>();
    private HashMap<String, Object> mHashMap;
    private SimpleAdapter mSimpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        initData(null);
    }

    //初始化
    @SuppressLint("Range")
    private void initData(String str) {

        if (mDBManager == null) {
            mDBManager = new DBManager(this);
            mDBManager.openDataBase();                              //建立本地数据库
        }
        lostDbList.clear();
        mDBManager.openDataBase();
        Cursor cursor= mDBManager.fetchAllUserDatas();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            while (cursor.moveToNext()) {
                mHashMap = new HashMap<>();
                mHashMap.put(DBManager.ID, cursor.getInt(cursor.getColumnIndex(DBManager.ID))+"");
                mHashMap.put(DBManager.USER_PHONE, cursor.getInt(cursor.getColumnIndex(DBManager.USER_PHONE))+"");
                mHashMap.put(DBManager.USER_NAME, cursor.getString(cursor.getColumnIndex(DBManager.USER_NAME)));
                mHashMap.put(DBManager.USER_SEX, cursor.getString(cursor.getColumnIndex(DBManager.USER_SEX)));
                mHashMap.put(DBManager.USER_CLASS, cursor.getString(cursor.getColumnIndex(DBManager.USER_CLASS)));
                mHashMap.put(DBManager.USER_HEIGHT, cursor.getString(cursor.getColumnIndex(DBManager.USER_HEIGHT)));
                if (str!=null) {
                    if (str.equals(cursor.getString(cursor.getColumnIndex(DBManager.USER_NAME))))
                        mapList.add(mHashMap);
                } else{
                    mapList.add(mHashMap);
                }
            }
            mSimpleAdapter = new SimpleAdapter(getBaseContext(), mapList, R.layout.user_list_item,
                    new String[]{DBManager.ID,
                            DBManager.USER_PHONE,
                            DBManager.USER_NAME,
                            DBManager.USER_SEX,
                            DBManager.USER_CLASS,
                            DBManager.USER_HEIGHT},
                    new int[]{ R.id.tv_stu_id, R.id.tv_stu_userid, R.id.tv_stu_name, R.id.tv_stu_pwd,R.id.tv_stu_sex,
                            R.id.tv_stu_likes});
            listView.setAdapter(mSimpleAdapter);
            mSimpleAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ShowUserInfoActivity","error:"+e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         TextView tvId=view.findViewById(R.id.tv_stu_id);
         String userid=tvId.getText().toString();
        TextView tvnum=view.findViewById(R.id.tv_stu_userid);
        String usernum=tvnum.getText().toString();
        TextView tvname=view.findViewById(R.id.tv_stu_name);
        String username=tvname.getText().toString();
        TextView tvsex=view.findViewById(R.id.tv_stu_pwd);
        String usersex=tvsex.getText().toString();
        TextView tvclass=view.findViewById(R.id.tv_stu_sex);
        String userclass=tvclass.getText().toString();
        TextView tvscore=view.findViewById(R.id.tv_stu_likes);
        String userscore=tvscore.getText().toString();

        Intent intent=new Intent(this, LostdetailsActivity.class);
        intent.putExtra("id",userid);
        intent.putExtra("phone",usernum);
        intent.putExtra("name",username);
        intent.putExtra("desc",usersex);
        intent.putExtra("date",userclass);
        intent.putExtra("loc",userscore);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        initData(null);
    }

}
