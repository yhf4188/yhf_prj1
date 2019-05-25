package com.chat.View;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

public class MessageFragment extends Fragment {
    private TextView tv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.message, container, false);
        tv=(TextView) messageLayout.findViewById(R.id.message);
        tv.setText("哈哈哈哈哈哈");
        return messageLayout;
    }

}