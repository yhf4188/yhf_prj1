package com.chat.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.os.Handler;
   import android.os.Message;
  import org.json.JSONException;
  import org.json.JSONObject;
  import java.util.HashMap;
  import java.util.Map;


import com.chat.model.HttpUtilsHttpURLConnection;
import com.chat.model.UserInf;
import com.example.myapplication.R;
import com.gc.materialdesign.views.*;
import com.uniquestudio.library.CircleCheckBox;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    EditText editText;
    EditText editText2;
    TextView error;
    CircleCheckBox normalCheckBox;
    ToggleButton normalToggleBtn;
    UserInf userInf;
    ProgressBarCircularIndeterminate progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.login);
        error=(TextView)findViewById(R.id.error);
        editText=(EditText)findViewById((R.id.EditText01));
        editText2=(EditText)findViewById((R.id.EditText02));
        userInf=new UserInf();
        findViewById(R.id.savepass);
        editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
        normalCheckBox = findViewById(R.id.checkbox_normal);
        boolean isRemember=pref.getBoolean("remember_password",false);
        if (isRemember) {
            // 将账号和密码都设置到文本框中,下一次进入不如再输密码啦
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            editText.setText(account);
            editText2.setText(password);
            normalCheckBox.setChecked(true);
        }
        normalToggleBtn=(ToggleButton)findViewById(R.id.toggle_button_normal);
        normalToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    editText2.setTransformationMethod(null);
                }else{
                    Toast.makeText(getApplicationContext(),"当前为隐藏密码状态",Toast.LENGTH_SHORT).show();
                    editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button_register).setOnClickListener(this);
    }

    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.button_login:
                final android.app.ProgressDialog progressDialog=android.app.ProgressDialog.show(this, "提示", "正在登陆中");
                new Thread(new Runnable() {
                      @Override


                     public void run() {
                                                 String url= HttpUtilsHttpURLConnection.BASE_URL+"/LoginServlet";
                                                 Map<String, String> params = new HashMap<String, String>();
                                                 String account=editText.getText().toString();
                                                 String password=editText2.getText().toString();
                                                 params.put("account",account);
                                                 params.put("password",password);

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
                                                                         JSONObject user=json.getJSONObject("userinf");
                                                                         userInf=new UserInf((int)user.get("account"),(String)user.get("username")
                                                                                 ,(String)user.get("password"),(String)user.get("mail"),(String)user.get("tel"));
                                                                         if ("success".equals(result)){
                                                                             editor=pref.edit();
                                                                             if(normalCheckBox.isChecked()) {
                                                                                 editor.putBoolean("remember_password", true);
                                                                                 editor.putString("account", editText.getText().toString());
                                                                                 editor.putString("password", editText2.getText().toString());
                                                                             }else {
                                                                                 editor.clear();
                                                                             }
                                                                             editor.apply();
                                                                             Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                                                                             Bundle bundle=new Bundle();
                                                                             bundle.putSerializable("User", userInf);//序列化
                                                                             intent.putExtras(bundle);//发送数据
                                                                             startActivity(intent);
                                                                             finish();

                                                                             }else {
                                                                             new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE)
                                                                                     .setTitleText("标题")
                                                                                     .setContentText("账号或密码错误")
                                                                                     .showCancelButton(false)
                                                                                     .show();
                                                                             }
                                                                     } catch (JSONException e) {
                                                                     new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.WARNING_TYPE)
                                                                             .setTitleText("标题")
                                                                             .setContentText("服务器异常")
                                                                             .showCancelButton(false)
                                                                             .show();
                                                                         e.printStackTrace();
                                                                     }
                                                             }
                                                     }
                      };
                  }).start();

                break;
            case R.id.button_register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
        }

    }

}

