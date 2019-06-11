package com.example.gastrogang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private Toolbar actionbarLogin;
    private EditText txtUsername, txtPassword;
    private Button btnLogin;

    public void init() {
        actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("GİRİŞ YAP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUsername = (EditText) findViewById(R.id.txtUsernameLogin);
        txtPassword = (EditText) findViewById(R.id.txtPasswordLogin);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

    }


    private void loginUser() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Kullanıcı Adı giriniz!", Toast.LENGTH_LONG).show();

        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Şifre giriniz!", Toast.LENGTH_LONG).show();
        }
        else {

            //TODO: HTTP REQUEST

            /*
            if (isSuccessful) {
                Toast.makeText(LoginActivity.this, "Başarılı bir şekilde giriş yapıldı!", Toast.LENGTH_LONG).show();
                Intent viewIntent = new Intent(LoginActivity.this, ViewActivity.class);
                startActivity(viewIntent);
                finish();
            }
            else {
                Toast.makeText(LoginActivity.this, "Giriş yaparken bir hata oluştu!", Toast.LENGTH_LONG).show();
            }
            */


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }
}
