package com.example.gastrogang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar actionbarRegister;

    public void init() {
        actionbarRegister = (Toolbar) findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        getSupportActionBar().setTitle("KAYIT OL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
}
