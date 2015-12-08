package com.lochbridge.peike.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lochbridge.peike.demo.util.Constants;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        int subId = getIntent().getIntExtra(Constants.EXTRA_SUB_ID, 0);
    }
}
