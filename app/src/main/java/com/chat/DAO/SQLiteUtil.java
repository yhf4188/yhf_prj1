package com.chat.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/4/27.
 */
public class SQLiteUtil extends SQLiteOpenHelper {

    //定义数据库的各种常量，如数据库名，表名，表的字段
    private static final String DBNAME = "user.db";
    private static final String TABLE_NAME = "user_info";
    private static final String TABLE_INFO_COLUM_ACCOUNT  = "_account";//主键前面一般都带下划线，也可不带
    private static final String TABLE_INFO_COLUM_USERNAME = "username";
    private static final String TABLE_INFO_COLUM_PWD = "pwd";
    private static final String TABLE_INFO_COLUM_MAIL="mail";
    private static final String TABLE_INFO_COLUM_tel="tel";

    //本类的构造方法
    public SQLiteUtil(Context context, int version) {
        /**
         创建构造方法时，默认是以下构造方式，可根据个人需要修改
         public SQLiteUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
         super(context, name, factory, version);
         }
         */
        //上面定义了数据库名，故可直接写死DBNAME
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表,使用StringBuffer代替String减少内存消耗
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
        stringBuffer.append(TABLE_NAME + "(");
        stringBuffer.append(TABLE_INFO_COLUM_ACCOUNT+" integer primary key autoincrement ,");
        stringBuffer.append(TABLE_INFO_COLUM_USERNAME+" varchar(10),");
        stringBuffer.append(TABLE_INFO_COLUM_PWD+" varchar(16),");
        stringBuffer.append(TABLE_INFO_COLUM_MAIL+" varchar(30),");
        stringBuffer.append(TABLE_INFO_COLUM_tel+" varchar(11))");
        //执行操作
        db.execSQL(stringBuffer.toString());
    }
    //重写的onUpgrade方法。当数据库结构修改，优化后，需要更新版本时，执行该方法，具体就是将旧的数据库删除，重写创建数据库。以达到更新的目的。方法中的oldVersion和newVersion分别对应新旧版本，可以用户自己定义，系统会自动回调该方法并判断版本是否发生变化
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists "
                +TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}

