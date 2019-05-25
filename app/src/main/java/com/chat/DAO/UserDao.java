package com.chat.DAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chat.model.UserInf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class UserDao {

    //定义之前刚完成的SQLiteUtil
    private SQLiteUtil sqLiteUtil;
    //该类构造方法
    public UserDao(Context context){
        //实例SQLiteUtil ，并将参数传入，1为版本号，可自定义
        sqLiteUtil = new SQLiteUtil(context,2);
    }

    /**
     * 获取数据库有两种：getWritableDatabase()和getWritableDatabase()，均返回SQLiteDatabase
     *    前者用来对数据库的插入，修改等有改变值得操作，后者用来查询等无改变值的操作
     * 数据库操作：增删改查
     * 有两种方式：系统自己封装好的各种方法，如rawQuery(),insert(),update(),delete()等
     * 也可以自己写sql语句，并通过execSQL()执行语句
     */
    //查询方法
    public boolean searchAccount(String account)
    {
        SQLiteDatabase db=sqLiteUtil.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user_info where _account = ?", new String[]{account});
        //循环得到结果集里的对象
        try {
            if (cursor.moveToNext()==true) {
                cursor.close();
                db.close();
                return false;
            }
        }
        catch (Exception e)
        {
            return true;
        }
        cursor.close();
        db.close();
        return true;
    }
    public List search(String account,String password){
        //创建list用来存放对象
        List list = new ArrayList();
        //通过getReadableDatabase()获取可读的数据库
        SQLiteDatabase db = sqLiteUtil.getReadableDatabase();
        //执行语句，获取返回类型为Cursor 的结果集
        Cursor cursor = db.rawQuery("select * from user_info where _account = ? and pwd = ?", new String[]{account,password});
        UserInf userInfo = null;
        //循环得到结果集里的对象
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_account"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
                String tel=cursor.getString(cursor.getColumnIndex("tel"));
                String mail=cursor.getString(cursor.getColumnIndex("mail"));
                userInfo = new UserInf();
                userInfo.setAccount(id);
                userInfo.setUsername(username);
                userInfo.setPassword(pwd);
                userInfo.setMail(mail);
                userInfo.setTel(tel);
                list.add(userInfo);
            }
            cursor.close();
            db.close();
        return list;
    }

    //添加
    public boolean insert(UserInf userInfo){
        //通过getWritableDatabase()获取可写的数据库
        SQLiteDatabase db = sqLiteUtil.getWritableDatabase();
        //ContentValues 用来存放数据，类似Map
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("username", userInfo.getUsername());
            contentValues.put("pwd", userInfo.getPassword());
            contentValues.put("_account", userInfo.getAccount());
            contentValues.put("mail", userInfo.getMail());
            contentValues.put("tel", userInfo.getTel());
            //执行插入
            db.insert("user_info", null, contentValues);
        }
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            db.close();
        }
        return true;
    }

    //删除
    public void del(int account){
        //通过getWritableDatabase()获取可写的数据库
        SQLiteDatabase db = sqLiteUtil.getWritableDatabase();
        db.delete("user_info", "_account = ?", new String[]{String.valueOf(account)});
        db.close();
    }

    //更新
    public void update(UserInf userInfo){
        //通过getWritableDatabase()获取可写的数据库
        SQLiteDatabase db = sqLiteUtil.getWritableDatabase();
        String sql = "update user_info set username = ? where _account = ?";
        db.execSQL(sql,new Object[]{userInfo.getUsername(), userInfo.getAccount()});
        db.close();
    }
}

