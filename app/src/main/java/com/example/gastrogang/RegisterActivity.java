package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar actionbarRegister;
    private EditText txtUsername, txtPassword;
    private Button btnRegister;

    public void init() {
        actionbarRegister = (Toolbar) findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        getSupportActionBar().setTitle("KAYIT OL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUsername = (EditText) findViewById(R.id.txtUsernameRegister);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void createNewAccount() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Kullanıcı Adı boş olamaz!", Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Şifre boş olamaz!", Toast.LENGTH_LONG).show();
        }
        else if (password.length() < 6) {
            Toast.makeText(this, "Şifre en az 6 karakterli olmalı!", Toast.LENGTH_LONG).show();
        }
        else {

            //TODO: HTTP REQUEST

            /*
            if (isSuccessful) {
                Toast.makeText(RegisterActivity.this, "Hesap başarılı bir şekilde oluşturuldu!", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
            else {
                Toast.makeText(RegisterActivity.this, "Kayıt olurken bir hata oluştu!", Toast.LENGTH_LONG).show();
            }
            */

            
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }
}
