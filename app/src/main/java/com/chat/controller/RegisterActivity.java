package com.chat.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.View.AutoComplete;
import com.chat.model.HttpUtilsHttpURLConnection;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    EditText reg_user;
    EditText reg_pwd;
    EditText reg_checkpwd;
    EditText reg_tel;
    AutoComplete reg_mail;
    Button bt_regist_save,bt_regist_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        reg_user= (EditText)findViewById(R.id.reg_username);
        reg_pwd= (EditText) findViewById(R.id.reg_pwd);
        reg_checkpwd = (EditText) findViewById(R.id.reg_checkpwd);
        reg_tel= (EditText) findViewById(R.id.reg_tel);
        reg_mail = findViewById(R.id.reg_mail);
        bt_regist_save= (Button) findViewById(R.id.bt_regist_save);// 组册按钮
        bt_regist_cancel= (Button) findViewById(R.id.bt_regist_cancel);

        bt_regist_save.setOnClickListener(this);
        bt_regist_cancel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_regist_save:

                String check_pwd=reg_checkpwd.getText().toString();
                final String user_nam=reg_user.getText().toString();
                final String user_password=reg_pwd.getText().toString();
                final String user_mail = reg_mail.getText().toString();
                final String user_tel = reg_tel.getText().toString();
                // 非空验证
                if (user_nam.isEmpty() || user_password.isEmpty() || user_mail.isEmpty() ||  user_tel.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "请填入完整数据", Toast.LENGTH_SHORT).show();
                    return;
                }
               else if(!isPassword(user_password))
                {
                    Toast.makeText(RegisterActivity.this, "请输入正确格式密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!isCheckPassword(check_pwd,user_password))
                {
                    Toast.makeText(RegisterActivity.this, "两次密码不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!isEmail(user_mail))
                {
                    Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!isMobileNO(user_tel))
                {
                    Toast.makeText(RegisterActivity.this, "手机号格式错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                final android.app.ProgressDialog progressDialog=android.app.ProgressDialog.show(this, "提示", "正在注册");
                new Thread(new Runnable() {
                    @Override


                    public void run() {
                        String url= HttpUtilsHttpURLConnection.BASE_URL+"/RegisterServlet";
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("password",user_password);
                        params.put("username",user_nam);
                        params.put("email",user_mail);
                        params.put("phone",user_tel);
                        String result = HttpUtilsHttpURLConnection.getContextByHttp(url,params);

                        Message msg = new Message();
                        msg.what=0x12;
                        Bundle data=new Bundle();
                        data.putString("result",result);
                        msg.setData(data);
                        hander.sendMessage(msg);
                    }

                    Handler hander = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what==0x12){
                                Bundle data = msg.getData();
                                String key = data.getString("result");//得到json返回的json
                                System.out.println(key);
                                progressDialog.dismiss();
                                //                                   Toast.makeText(MainActivity.this,key,Toast.LENGTH_LONG).show();
                                try {
                                    if(key.startsWith("\ufeff"))
                                    {
                                        key =  key.substring(1);
                                    }
                                    JSONObject json= new JSONObject(key);
                                    String result = (String) json.get("result");
                                    int account=(int)json.get("account");
                                    if ("success".equals(result)){
                                        new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("标题")
                                                .showCancelButton(false)
                                                .setContentText("注册成功\n账号为"+account)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog dialog) {
                                                        finish();
                                                    }
                                                }).show();
                                    }else {
                                        new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("标题")
                                                .setContentText("注册失败")
                                                .showCancelButton(false)
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog dialog) {
                                                    }
                                                }).show();
                                    }
                                } catch (JSONException e) {
                                    new SweetAlertDialog(RegisterActivity.this,SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("标题")
                                            .setContentText("服务器异常")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog dialog) {
                                                }
                                            })
                                            .showCancelButton(false)
                                            .show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }).start();
                //注意：不能用save方法进行注册
                break;
            case R.id.bt_regist_cancel:
                finish();

                break;
            default:
                break;
        }
    }
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p =  Pattern.compile(str);//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean isPassword(String pwd)
    {
        Pattern p=Pattern.compile("^[a-zA-Z0-9]{6,16}$");
        Matcher m=p.matcher(pwd);
        return m.matches();
    }
    public static boolean isCheckPassword(String check_pwd,String pwd)
    {
       if(!check_pwd.equals(pwd)) {
           return false;
       }
       else return true;
    }

}
