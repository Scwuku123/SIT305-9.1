package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {
    //一些宏定义和声明
    private static final String TAG = "DBManager";
    private static final String DB_NAME = "lostdb";
    private static final String TABLE_NAME = "users";
    public static final String ID = "_id";
    public static final String USER_PHONE = "userid";

    public static final String USER_NAME = "username";
    public static final String USER_SEX = "usersex";
    public static final String USER_CLASS = "userclass";
    public static final String USER_HEIGHT = "userheight";

    //    public static final String SILENT = "silent";
//    public static final String VIBRATE = "vibrate";
    private static final int DB_VERSION = 2;
    private Context mContext = null;

    //创建用户book表
    private static final String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key,"
            + USER_PHONE + " integer,"
            + USER_NAME + " varchar,"
            + USER_SEX + " varchar,"
            + USER_CLASS + " varchar,"
            + USER_HEIGHT + " varchar"  + ");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

  //  long i= insertUserData( manager_User);//插入管理员
    //DataBaseManagementHelper继承自SQLiteOpenHelper
    private static class DataBaseManagementHelper extends SQLiteOpenHelper {
 
        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.v(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            db.execSQL(DB_CREATE);
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.v(TAG, "DBManager onUpgrade");
            onCreate(db);
        }
    }
 
    public DBManager(Context context) {
        mContext = context;
        Log.v(TAG, "DBManager construction!");
    }
    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }
    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertUserData(LostDb lostDb) {
        int id= lostDb.getPhone();
        String userName= lostDb.getUserName();
        String userPwd= lostDb.getDesc();
        String userType= lostDb.getDate();
        String userLike= lostDb.getLocation();
        ContentValues values = new ContentValues();
        values.put(USER_PHONE, id);
        values.put(USER_NAME, userName);
        values.put(USER_SEX, userPwd);
        values.put(USER_CLASS,userType);
        values.put(USER_HEIGHT,userLike);
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    //
    public Cursor fetchAllUserDatas() {
        return mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null,
                null);
    }
    //根据id删除用户
    public boolean deleteUserData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME, ID + "=" + id, null) > 0;
    }
 
}