package com.chat.controller;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chat.View.ContactsFragment;
import com.chat.View.MessageFragment;
import com.chat.model.HttpUtilsHttpURLConnection;
import com.chat.model.Question;
import com.chat.model.UserInf;
import com.example.myapplication.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    private  UserInf userInf;
    private ArrayList<Question> questionss;
    private View messageLayout;
    private View contactsLayout;
    private android.app.FragmentManager fragmentManager;
    private MessageFragment messageFragment;

    /**
     * 用于展示联系人的Fragment
     */
    private ContactsFragment contactsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        questionss=new ArrayList<Question>();
        messageLayout=findViewById(R.id.message_layout);
        contactsLayout=findViewById(R.id.contacts_layout);
        messageLayout.setOnClickListener(this);
        contactsLayout.setOnClickListener(this);
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.bringToFront();
        findViewById((R.id.action_b)).setOnClickListener(this);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.white));
        findViewById(R.id.action_a).setOnClickListener(this);
        Intent intent=getIntent();
        userInf=(UserInf)intent.getSerializableExtra("User");
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.action_a:
                final android.app.ProgressDialog progressDialog=android.app.ProgressDialog.show(this, "提示", "正在加载题目");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url= HttpUtilsHttpURLConnection.BASE_URL+"/TestServlet";
                        Map<String, String> params = new HashMap<String, String>();
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
                                    JSONObject []questions=new JSONObject[6];
                                    for(int i=0;i<6;i++)
                                    {
                                        questions[i]=json.getJSONObject("question"+i);
                                        Question question=new Question((String)questions[i].get("question"),(String)questions[i].get("a"),
                                                (String)questions[i].get("b"),(String)questions[i].get("c"),(String)questions[i].get("d"),
                                                (String)questions[i].get("answer"));
                                        questionss.add(question);
                                    }
                                    Intent intent= new Intent(ChatActivity.this, TestActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("User", userInf);
                                    bundle.putSerializable("Question",(Serializable) questionss);//序列化,要注意转化(Serializable)
                                    intent.putExtras(bundle);//发送数据
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    new SweetAlertDialog(ChatActivity.this,SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("标题")
                                            .setContentText(key)
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog dialog) {
                                                }
                                            }).show();
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }).start();
                break;
            case R.id.action_b:
                Intent intent=new Intent(ChatActivity.this, DrawingBoardActivity.class);
                startActivity(intent);
            case R.id.message_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.contacts_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     * 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                messageLayout.setBackgroundColor(0xff0000ff);

                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                contactsLayout.setBackgroundColor(0xff0000ff);
                if (contactsFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    contactsFragment = new ContactsFragment();
                    transaction.add(R.id.content, contactsFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(contactsFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     * 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        messageLayout.setBackgroundColor(0xffffffff);
        contactsLayout.setBackgroundColor(0xffffffff);
    }
}
