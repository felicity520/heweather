package com.coolweather.android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.coolweather.android.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //这是本地homebranch分支----
        initView();
        initDate();
        initEvent();


    }

    private void initEvent() {

    }

    private void initDate() {

    }

    private void initView() {

    }




}


