package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_recipe);

        Toolbar actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("RECIPE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(RecipeActivity.this, ViewActivity.class);
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
            Toast.makeText(RecipeActivity.this, "Unexpected Error" , Toast.LENGTH_LONG).show();
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

        Button editRecipe = findViewById(R.id.btnEditRecipe);

        editRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeIntent = new Intent(RecipeActivity.this, EditRecipeActivity.class);
                recipeIntent.putExtra("id", recipeId);
                recipeIntent.putExtra("name", recipeName);
                recipeIntent.putExtra("steps", recipeStepsList);
                recipeIntent.putExtra("ingredients", recipeIngredientsList);
                recipeIntent.putExtra("details", recipeDetails);
                recipeIntent.putExtra("token", ACCESS_TOKEN);
                recipeIntent.putExtra("tags", recipeTagsList);
                startActivity(recipeIntent);
            }
        });

        Button deleteRecipe = findViewById(R.id.btnDeleteRecipe);

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://gastrogang.herokuapp.com/api/v1/recipes/" + recipeId;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(RecipeActivity.this, ViewActivity.class);
                                intent.putExtra("token", ACCESS_TOKEN);
                                startActivity(intent);
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse.statusCode == 403) {
                                    Toast.makeText(RecipeActivity.this, "Error : Recipe belongs to someone else", Toast.LENGTH_LONG).show();
                                }
                                else if (error.networkResponse.statusCode == 404) {
                                    Toast.makeText(RecipeActivity.this, "Error : Recipe does not exist", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(RecipeActivity.this, "Unexpected Error while deleting the recipe", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Bearer " + ACCESS_TOKEN);
                        return params;
                    }
                    //Override parseNetworkResponse because response of the JsonObjectRequest is empty.
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                            JSONObject result = null;
                            if (jsonString.length() > 0)
                                result = new JSONObject(jsonString);
                            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                    }
                };
                queue.add(getRequest);
            }
        });
    }
}