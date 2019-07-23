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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar actionbarRegister;
    private EditText txtUsername, txtPassword, txtLifetime;
    private Button btnRegister;

    public void init() {
        actionbarRegister = (Toolbar) findViewById(R.id.actionbarRegister);
        setSupportActionBar(actionbarRegister);
        getSupportActionBar().setTitle("SIGN UP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtUsername = (EditText) findViewById(R.id.txtUsernameRegister);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        txtLifetime = (EditText) findViewById(R.id.txtLifetime);
    }

    private void createNewAccount() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        String lifetime = txtLifetime.getText().toString();

        if (TextUtils.isEmpty(username)) {
           Toast.makeText(this, "Username cannot be empty!", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password should be at least 6 chars long!", Toast.LENGTH_LONG).show();
        }
        else if (lifetime.equals("")) {
            Toast.makeText(this, "Lifetime cannot be empty!", Toast.LENGTH_LONG).show();
        }
        else {
            String url = "https://gastrogang.herokuapp.com/api/v1/register";
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", username);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("lifetime", Integer.parseInt(lifetime));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(RegisterActivity.this, "Account has been successfully created!", Toast.LENGTH_LONG).show();
                            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(RegisterActivity.this, "This username has already been taken!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "An error occurred while registering!", Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }
                    });
            queue.add(jsObjRequest);
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
