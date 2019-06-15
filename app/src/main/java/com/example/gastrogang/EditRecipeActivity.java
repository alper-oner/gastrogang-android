package com.example.gastrogang;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditRecipeActivity extends AppCompatActivity {
    ArrayList recipeStepList = new ArrayList<String>();
    ArrayList recipeIngredientList = new ArrayList<String>();
    String recipeName = new String();
    String recipeDetails = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        ListView stepLv = (ListView) findViewById(R.id.editListSteps);
        ListView ingredientsLv = (ListView) findViewById(R.id.editListIngredients);
        Button addStepBtn = (Button) findViewById(R.id.editBtAdd);
        Button updateRecipeBtn = (Button) findViewById(R.id.editBtnUpdate);
        Button addIngredientsBtn = (Button) findViewById(R.id.editBtAddIngredients);

        final EditText recipeNameText = (EditText) findViewById(R.id.editRecipeName);
        final EditText recipeDetailsText = (EditText) findViewById(R.id.editRecipeDetails);
        final EditText stepText = (EditText) findViewById(R.id.editTextStep);
        final EditText ingredientsText = (EditText) findViewById(R.id.editTextIngredients);

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> stepListAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepList);

        final ArrayAdapter<String> ingredientListAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeIngredientList);

        stepLv.setAdapter(stepListAdapter);
        ingredientsLv.setAdapter(ingredientListAdapter);

        //get old values
        recipeName = getIntent().getStringExtra("name");
        recipeDetails = getIntent().getStringExtra("details");

        ArrayList tempRecipeIngredientList = new ArrayList<String>();
        ArrayList tempRecipeStepList = new ArrayList<String>();
        tempRecipeIngredientList = getIntent().getStringArrayListExtra("ingredients");
        tempRecipeStepList = getIntent().getStringArrayListExtra("steps");

        recipeIngredientList.addAll(tempRecipeIngredientList);
        recipeStepList.addAll(tempRecipeStepList);

        recipeNameText.setText(recipeName);
        recipeDetailsText.setText(recipeDetails);
        stepListAdapter.notifyDataSetChanged();
        ingredientListAdapter.notifyDataSetChanged();


        // add step button
        addStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                if (stepText.length() > 0) {
                    recipeStepList.add(stepText.getText().toString());
                    stepListAdapter.notifyDataSetChanged();
                    stepText.setText("");
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Steps area is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //delete step list item
        stepLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Toast.makeText(EditRecipeActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                recipeStepList.remove(pos);
                stepListAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // add ingredients button
        addIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                if (ingredientsText.length() > 0) {
                    recipeIngredientList.add(ingredientsText.getText().toString());
                    ingredientListAdapter.notifyDataSetChanged();
                    ingredientsText.setText("");
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Ingredients area is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //delete ingredients list item
        ingredientsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Toast.makeText(EditRecipeActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                recipeIngredientList.remove(pos);
                ingredientListAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // update recipe button
        updateRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeName = recipeNameText.getText().toString();
                recipeDetails = recipeDetailsText.getText().toString();
                if (recipeName.length() <= 0 || recipeStepList.size() <= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditRecipeActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Error");
                    builder.setMessage("Please fill empty required fields");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("name", recipeName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray jsonArraySteps = new JSONArray();
                        for (int i = 0; i < recipeStepList.size(); i++) {
                            jsonArraySteps.put(recipeStepList.get(i));
                        }
                        obj.put("steps", jsonArraySteps);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONArray jsonArrayIngredient = new JSONArray();
                        for (int i = 0; i < recipeIngredientList.size(); i++) {
                            jsonArrayIngredient.put(recipeIngredientList.get(i));
                        }
                        obj.put("ingredients", jsonArrayIngredient);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        obj.put("details", recipeDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String ID = getIntent().getStringExtra("id");
                    String url = "http://192.168.1.24:8080/api/v1/recipes/" + ID; // TODO add correct url
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(EditRecipeActivity.this, "Successful: " + response.toString(), Toast.LENGTH_SHORT).show();
                                    Intent recipeIntent = new Intent(EditRecipeActivity.this, RecipeActivity.class);
                                    recipeIntent.putExtra("token", getIntent().getStringExtra("token"));
                                    recipeIntent.putExtra("name", recipeName);
                                    recipeIntent.putExtra("steps", recipeStepList);
                                    recipeIntent.putExtra("ingredients", recipeIngredientList);
                                    recipeIntent.putExtra("details", recipeDetails);
                                    recipeIntent.putExtra("id", getIntent().getStringExtra("id"));
                                    clearTextAreas();
                                    startActivity(recipeIntent);
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(EditRecipeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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

                }
            }
        });
    }

    private void clearTextAreas() {
        final EditText recipeNameText = (EditText) findViewById(R.id.recipeName);
        final EditText recipeDetailsText = (EditText) findViewById(R.id.recipeDetails);
        final EditText stepText = (EditText) findViewById(R.id.textStep);
        final EditText ingredientsText = (EditText) findViewById(R.id.textIngredients);

        recipeNameText.setText("");
        stepText.setText("");
        recipeDetailsText.setText("");
        ingredientsText.setText("");

        recipeStepList.clear();
        recipeIngredientList.clear();
        recipeDetails = "";
        recipeName = "";
    }


}
