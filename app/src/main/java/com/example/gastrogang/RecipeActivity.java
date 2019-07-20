package com.example.gastrogang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RecipeActivity extends AppCompatActivity {

    private String recipeId = "";
    private String recipeName = "";
    private String recipeDetails = "";
    private ArrayList<String> recipeIngredientsList = null;
    private ArrayList<String> recipeStepsList = null;
    private String ACCESS_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

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
        } else {
            Toast.makeText(RecipeActivity.this, "Unexpected Error", Toast.LENGTH_LONG).show();
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

        Button editRecipe = findViewById(R.id.btnEditRecipe);

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setChecked(false);

        final Button copyUrl = findViewById(R.id.copyUrlButton);
        copyUrl.setVisibility(View.GONE);

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
                                } else if (error.networkResponse.statusCode == 404) {
                                    Toast.makeText(RecipeActivity.this, "Error : Recipe does not exist", Toast.LENGTH_LONG).show();
                                } else {
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

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // TODO public/private post at
                    String url = " https://webhook.site/ab98b14e-a11c-4b76-a53e-fe2c20f163ab ";
                    JSONObject obj = new JSONObject();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(RecipeActivity.this, "Successful: " + response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RecipeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String ACCESS_TOKEN = getIntent().getStringExtra("token");
                            params.put("Authorization", "Bearer " + ACCESS_TOKEN);
                            return params;
                        }
                    };
                    queue.add(jsObjRequest);


                    // if toggle button is enabled/on
                    copyUrl.setVisibility(View.VISIBLE);

                    // Make a toast to display toggle button status
                    Toast.makeText(getApplicationContext(),
                            "Recipe is public", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO public/private post at
                    String url = "https://webhook.site/ab98b14e-a11c-4b76-a53e-fe2c20f163ab ";
                    JSONObject obj = new JSONObject();
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(RecipeActivity.this, "Successful: " + response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RecipeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String ACCESS_TOKEN = getIntent().getStringExtra("token");
                            params.put("Authorization", "Bearer " + ACCESS_TOKEN);
                            return params;
                        }
                    };
                    queue.add(jsObjRequest);
                    // If toggle button is disabled/off
                    copyUrl.setVisibility(View.GONE);

                    // Make a toast to display toggle button status
                    Toast.makeText(getApplicationContext(),
                            "Recipe is private", Toast.LENGTH_SHORT).show();
                }
            }
        });

        copyUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        "URL copied", Toast.LENGTH_SHORT).show();
                // TODO get URL
                String url = "https://api.myjson.com/bins/wf259";
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String copiedUrl = response.getString("url");
                                    Toast.makeText(getApplicationContext(),
                                            "URL: " + copiedUrl, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RecipeActivity.this, "Error while getting URL" + error, Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Bearer " + ACCESS_TOKEN);
                        return params;
                    }
                };
                queue.add(getRequest);
            }
        });


    }
}