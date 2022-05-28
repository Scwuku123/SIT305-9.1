package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Utils.LogUtils;

public class CreateLostActivity extends AppCompatActivity {
    private EditText etname,etphone,etdesc,etdate,etloc;
    private DBManager mDBManager;
    private Button btncreate;
    RadioGroup group;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createlos);
        mContext=this;

        group=findViewById(R.id.rg_classfy);
        etname=findViewById(R.id.et_name);
        etphone=findViewById(R.id.et_phone);
        etdesc=findViewById(R.id.et_desc);
        etdate=findViewById(R.id.et_date);
        etloc=findViewById(R.id.et_loc);
        btncreate=findViewById(R.id.btncreate);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectClassfy = ((RadioButton) findViewById(group
                        .getCheckedRadioButtonId())).getText().toString();
                String name=etname.getText().toString();
                String phone=etphone.getText().toString();
                String desc=etdesc.getText().toString();
                String date=etdate.getText().toString();
                String loc=etloc.getText().toString();
                if (name.isEmpty() || desc.isEmpty()|| phone.isEmpty()||date.isEmpty()||loc.isEmpty()){
                    Toast.makeText(mContext,"please input",Toast.LENGTH_SHORT).show();
                }else{
                    if (mDBManager == null) {
                        mDBManager = new DBManager(mContext);
                        mDBManager.openDataBase();  //建立本地数据库
                    }
                    LostDb mUser = new LostDb(Integer.valueOf(phone), name, selectClassfy+" "+desc,date, loc);
                    long flag = mDBManager.insertUserData(mUser);
                    if (flag == -1) {
                        Toast.makeText(getApplicationContext()," failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "create successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
    public void onClickgetloc(View view) {
        Intent intent=new Intent(this,LocationActivity.class);
        startActivityForResult(intent,0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LogUtils.filename(new Exception()),LogUtils.funAndLine(new Exception())+
                requestCode+"|"+resultCode);
        if (requestCode==0 && resultCode==0){
            if (data!=null) {
                String loc = data.getStringExtra("loc"); //获取回传的数据
                etloc.setText(loc + "");
            }
        }

    }
}