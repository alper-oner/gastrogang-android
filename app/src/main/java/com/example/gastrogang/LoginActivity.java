package com.example.gastrogang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {


    private Toolbar actionbarLogin;

    public void init() {
        actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("GİRİŞ YAP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
}
