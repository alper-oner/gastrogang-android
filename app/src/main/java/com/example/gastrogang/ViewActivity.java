package com.example.gastrogang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewActivity extends AppCompatActivity {

    private ArrayList<String> recipeIdsList = new ArrayList<>();
    private ArrayList<String> recipeNamesList = new ArrayList<>();
    private ArrayList<String> recipeDetailsList = new ArrayList<>();
    private ArrayList<ArrayList<String>> recipeIngredientsListList = new ArrayList<>();
    private ArrayList<ArrayList<String>> recipeStepsListList = new ArrayList<>();
    private ArrayList<ArrayList<String>> recipeTagsListList = new ArrayList<>();
    private String ACCESS_TOKEN = "";
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("GASTROGANG");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ACCESS_TOKEN = getIntent().getStringExtra("token");

        Button buttonAddRecipe = findViewById(R.id.addRecipe);

        SearchView search = findViewById(R.id.searchView);
        search.setActivated(true);
        search.setQueryHint("Search...");
        search.onActionViewExpanded();
        search.setIconified(false);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url = query;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String recipeId = response.getString("ID");
                                    String recipeName = response.getString("name");
                                    JSONArray recipeSteps = response.getJSONArray("steps");
                                    JSONArray recipeIngredients = response.getJSONArray("ingredients");
                                    JSONArray recipeTags = response.getJSONArray("tags");
                                    String recipeDetails = response.getString("details");

                                    Intent recipeIntent = new Intent(ViewActivity.this, PublicRecipeActivity.class);
                                    recipeIntent.putExtra("id", recipeId);
                                    recipeIntent.putExtra("name", recipeName);

                                    ArrayList<String> recipeStepsList = new ArrayList<>();
                                    for (int j = 0; j < recipeSteps.length(); j++) {
                                        recipeStepsList.add(recipeSteps.get(j).toString());
                                    }
                                    recipeIntent.putExtra("steps", recipeStepsList);

                                    ArrayList<String> recipeIngredientsList = new ArrayList<>();
                                    for (int j = 0; j < recipeIngredients.length(); j++) {
                                        recipeIngredientsList.add(recipeIngredients.get(j).toString());
                                    }
                                    recipeIngredientsListList.add(recipeIngredientsList);
                                    recipeIntent.putExtra("ingredients", recipeIngredientsList);

                                    ArrayList<String> recipeTagsList = new ArrayList<>();
                                    for (int j = 0; j < recipeTags.length(); j++) {
                                        recipeTagsList.add(recipeTags.get(j).toString());
                                    }
                                    recipeIntent.putExtra("tags", recipeTagsList);

                                    recipeIntent.putExtra("details", recipeDetails);
                                    recipeIntent.putExtra("token", ACCESS_TOKEN);
                                    startActivity(recipeIntent);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ViewActivity.this, "Error while getting recipes", Toast.LENGTH_LONG).show();
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        buttonAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recipeIntent = new Intent(ViewActivity.this, StoreRecipeActivity.class);
                recipeIntent.putExtra("token", ACCESS_TOKEN);
                startActivity(recipeIntent);
            }
        });

        String url = "https://gastrogang.herokuapp.com/api/v1/recipes";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                String recipeId = jsonObject.getString("ID");
                                System.out.println(recipeId);
                                String recipeName = jsonObject.getString("name");
                                JSONArray recipeSteps = jsonObject.getJSONArray("steps");
                                JSONArray recipeIngredients = jsonObject.getJSONArray("ingredients");
                                String recipeDetails = jsonObject.getString("details");
                                JSONArray recipeTags = jsonObject.getJSONArray("tags");

                                recipeIdsList.add(recipeId);
                                recipeNamesList.add(recipeName);

                                ArrayList<String> recipeStepsList = new ArrayList<>();
                                for (int j = 0; j < recipeSteps.length(); j++) {
                                    recipeStepsList.add(recipeSteps.get(j).toString());
                                }
                                recipeStepsListList.add(recipeStepsList);

                                ArrayList<String> recipeIngredientsList = new ArrayList<>();
                                for (int j = 0; j < recipeIngredients.length(); j++) {
                                    recipeIngredientsList.add(recipeIngredients.get(j).toString());
                                }
                                recipeIngredientsListList.add(recipeIngredientsList);

                                recipeDetailsList.add(recipeDetails);

                                ArrayList<String> recipeTagsList = new ArrayList<>();
                                for (int j = 0; j < recipeTags.length(); j++) {
                                    recipeTagsList.add(recipeTags.get(j).toString());
                                }
                                recipeTagsListList.add(recipeTagsList);

                                initRecipeListView();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewActivity.this, "Error while getting recipes", Toast.LENGTH_LONG).show();
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

    private void initRecipeListView() {

        adapter = new ArrayAdapter<>(this, R.layout.recipelistitem, R.id.btnRecipe, recipeNamesList);
        ListView listView = (ListView) findViewById(R.id.recipe_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent recipeIntent = new Intent(ViewActivity.this, RecipeActivity.class);
                recipeIntent.putExtra("id", recipeIdsList.get(i));
                recipeIntent.putExtra("name", recipeNamesList.get(i));
                recipeIntent.putExtra("steps", recipeStepsListList.get(i));
                recipeIntent.putExtra("ingredients", recipeIngredientsListList.get(i));
                recipeIntent.putExtra("details", recipeDetailsList.get(i));
                recipeIntent.putExtra("tags", recipeTagsListList.get(i));
                recipeIntent.putExtra("token", ACCESS_TOKEN);
                startActivity(recipeIntent);
                finish();
            }

        });
    }

}