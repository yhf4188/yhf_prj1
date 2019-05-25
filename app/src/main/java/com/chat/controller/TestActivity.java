package com.chat.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.model.Question;
import com.example.myapplication.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.hanks.htextview.evaporate.EvaporateTextView;


import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TestActivity extends AppCompatActivity{
    private int recLen = 10;
    private EvaporateTextView txtView;
    Timer timer = new Timer();
    private List<View> viewList = new ArrayList<>();
    private RadioButton []radioList[]=new RadioButton[6][4];
    ArrayList<Question>questions=new ArrayList<>();
    private String []chooses={"A","B","C","D"};
    private String []answers=new String[6];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        txtView =  findViewById(R.id.timeTaker);
        txtView.animateText("倒计时：10");
        timer.schedule(task,1000,1000);// timeTask 
        Intent intent=getIntent();
        questions=(ArrayList<Question>)intent.getSerializableExtra("Question");
        for (int i = 1; i < 7; i++) {
            answers[i-1]=(String)questions.get(i-1).getAnswer();
            View rootView = View.inflate(TestActivity.this, R.layout.test, null);
            ButtonRectangle buttonRectangle=rootView.findViewById(R.id.submit);
            TextView textView = (TextView) rootView.findViewById(R.id.title);
            textView.setText(String.valueOf(i)+questions.get(i-1).getQuestion());
            TextView choose=rootView.findViewById(R.id.choose);
            if(i<=4) {
                RadioGroup radioGroup = rootView.findViewById(R.id.select);
                radioList[i-1][0]=(RadioButton)rootView.findViewById(R.id.comb1);
                radioList[i-1][1]=(RadioButton)rootView.findViewById(R.id.comb2);
                radioList[i-1][2]=(RadioButton)rootView.findViewById(R.id.comb3);
                radioList[i-1][3]=(RadioButton)rootView.findViewById(R.id.comb4);
                rootView.findViewById(R.id.combA).setVisibility(View.GONE);
                rootView.findViewById(R.id.combB).setVisibility(View.GONE);
                rootView.findViewById(R.id.combC).setVisibility(View.GONE);
                rootView.findViewById(R.id.combD).setVisibility(View.GONE);

            }
            else
            {
                rootView.findViewById(R.id.select).setVisibility(View.GONE);
                radioList[i-1][0]=(RadioButton)rootView.findViewById(R.id.combA);
                radioList[i-1][1]=(RadioButton)rootView.findViewById(R.id.combB);
                radioList[i-1][2]=(RadioButton)rootView.findViewById(R.id.combC);
                radioList[i-1][3]=(RadioButton)rootView.findViewById(R.id.combD);
            }
            radioList[i-1][0].setText("A:"+questions.get(i-1).getA());
            radioList[i-1][1].setText("B:"+questions.get(i-1).getB());
            radioList[i-1][2].setText("C:"+questions.get(i-1).getC());
            radioList[i-1][3].setText("D:"+questions.get(i-1).getD());
            if(i==6)
            {
                buttonRectangle.setVisibility(View.VISIBLE);
                buttonRectangle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(TestActivity.this,SweetAlertDialog.WARNING_TYPE)
                                .setContentText("确认提交吗？")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        try {

                                            String[] youAnswer = new String[6];
                                            for (int i = 0; i < 6; i++) {

                                                if (i < 4) {
                                                    for (int j = 0; j <= 3; j++) {
                                                        if (radioList[i][j].isChecked()) {
                                                            youAnswer[i] = chooses[j];
                                                        }

                                                    }
                                                } else {
                                                    for (int j = 0; j <= 3; j++) {
                                                        if (radioList[i][j].isChecked()) {
                                                            if (youAnswer[i] == null) {
                                                                youAnswer[i] = chooses[j];
                                                            } else
                                                                youAnswer[i] += chooses[j];
                                                        }
                                                    }
                                                }
                                            }
                                            int num = 0;
                                            for (int j = 0; j < 6; j++) {
                                                System.out.println(youAnswer[j] + " " + answers[j] + "\n");
                                                if ((youAnswer[j].equals(answers[j])) == true) {
                                                    num++;
                                                }

                                            }
                                            sweetAlertDialog
                                                    .showCancelButton(false)
                                                    .setContentText("一共六题你答对了" + num + "题")
                                                    .setConfirmText("确定")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            finish();
                                                        }
                                                    })
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        } catch (NullPointerException e) {
                                            sweetAlertDialog
                                                    .showCancelButton(false)
                                                    .setContentText("请完整答题")
                                                    .setConfirmText("确定");

                                        }
                                    }
                                }
                                ).show();
                    }
                });
            }
            else
            {
                buttonRectangle.setVisibility(View.INVISIBLE);
            }
            viewList.add(i-1, rootView);
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyAdapter());
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread    
                @Override
                public void run() {
                    recLen--;
                    txtView.animateText("倒计时：" + recLen);
                    if (recLen < 0) {
                        txtView.setVisibility(View.GONE);
                            String[] youAnswer = new String[6];
                            for (int i = 0; i < 6; i++) {

                                if (i < 4) {
                                    for (int j = 0; j <= 3; j++) {
                                        if (radioList[i][j].isChecked()) {
                                            youAnswer[i] = chooses[j];
                                        }
                                    }
                                } else {
                                    for (int j = 0; j <= 3; j++) {
                                        if (radioList[i][j].isChecked()) {
                                            if (youAnswer[i] == null) {
                                                youAnswer[i] = chooses[j];
                                            } else
                                                youAnswer[i] += chooses[j];
                                        }
                                    }
                                }
                            }
                            int num = 0;
                            for (int j = 0; j < 6; j++) {
                                if(youAnswer[j]!=null) {
                                    if ((youAnswer[j].equals(answers[j])) == true) {
                                        num++;
                                    }
                                }

                            }
                            SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(TestActivity.this,SweetAlertDialog.SUCCESS_TYPE);
                                    sweetAlertDialog
                                            .setCancelClickListener(null)
                                    .setContentText("时间到！一共六题你答对了" + num + "题")
                                    .setConfirmText("确定")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    }).show();
                                    sweetAlertDialog.showCancelButton(false);
                        timer.cancel();
                        }
                    }
                });
            }
        };
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

    }
}
