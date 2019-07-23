package com.example.gastrogang;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublicRecipeActivity extends AppCompatActivity {

    private String recipeId = "";
    private String recipeName = "";
    private String recipeDetails = "";
    private ArrayList<String> recipeIngredientsList = new ArrayList<>();
    private ArrayList<String> recipeStepsList = new ArrayList<>();
    private ArrayList<String> recipeTagsList = new ArrayList<>();
    private String ACCESS_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_recipe);

        Toolbar actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("RECIPE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(PublicRecipeActivity.this, ViewActivity.class);
                recipeIntent.putExtra("token", getIntent().getStringExtra("token"));
                startActivity(recipeIntent);
                finish();}
        });

        TextView txtviewRecipeName = findViewById(R.id.txtrcpRecipeName);
        TextView txtviewRecipeDetails = findViewById(R.id.txtrcpRecipeDetails);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recipeId = extras.getString("id");
            ACCESS_TOKEN = extras.getString("token");
            recipeName = extras.getString("name");
            recipeDetails = extras.getString("details");
            recipeIngredientsList = extras.getStringArrayList("ingredients");
            recipeStepsList = extras.getStringArrayList("steps");
            recipeTagsList = extras.getStringArrayList("tags");
        }
        else {
            Toast.makeText(PublicRecipeActivity.this, "Unexpected Error" , Toast.LENGTH_LONG).show();

        }
        txtviewRecipeName.setText(recipeName);
        txtviewRecipeDetails.setText(recipeDetails);

        ListView lvIngredients = findViewById(R.id.rcpIngredientList);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeIngredientsList);
        lvIngredients.setAdapter(ingredientsAdapter);

        ListView lvSteps = findViewById(R.id.rcpSteplist);
        ArrayAdapter<String> stepsAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepsList);
        lvSteps.setAdapter(stepsAdapter);

        ListView lvTags = findViewById(R.id.rcpTagList);
        ArrayAdapter<String> tagsAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeTagsList);
        lvTags.setAdapter(tagsAdapter);



    }
}