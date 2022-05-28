package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LostdetailsActivity extends AppCompatActivity {
    TextView etdate,etdesc,etloc;
    private DBManager mDBManager;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostdetails);
        etdesc=findViewById(R.id.desc);
        etdate=findViewById(R.id.date);

        etloc=findViewById(R.id.loc);

        id=Integer.valueOf(getIntent().getStringExtra("id"));
        etdesc.setText(getIntent().getStringExtra("desc"));
        etdate.setText(getIntent().getStringExtra("date"));
        etloc.setText(getIntent().getStringExtra("loc"));
    }

    public void onClick1(View view) {
        new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("delete")
                .setMessage("do you want remove it?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ToDo: 你想做的事情
                if (mDBManager == null) {
                    mDBManager = new DBManager(LostdetailsActivity.this);
                    mDBManager.openDataBase();                              //建立本地数据库
                }
                mDBManager.deleteUserData(id);
                Toast.makeText(getApplicationContext(), "remove successfully", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                setResult(0, intent);
                finish();

            }
        }).setNegativeButton("no", null).show();
    }
}