package com.chat.View;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

public class ContactsFragment extends Fragment {
    private TextView tv;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View conactsLayout = inflater.inflate(R.layout.contacts, container, false);
        tv=(TextView)  conactsLayout.findViewById(R.id.contacts);
        tv.setText("哈哈哈哈哈哈");
        return  conactsLayout;
    }

}