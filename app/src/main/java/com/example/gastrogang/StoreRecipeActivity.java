package com.example.gastrogang;

import android.content.DialogInterface;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreRecipeActivity extends AppCompatActivity {
    ArrayList recipeStepList = new ArrayList<String>();
    String recipeName = new String();
    String recipeDetails = new String();
    String url = "https://webhook.site/3e631a91-708d-43bc-8e48-b449514272c9"; // TODO add true url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_recipe);

        ListView lv = (ListView) findViewById(R.id.list);
        Button addBtn = (Button) findViewById(R.id.btAdd);
        Button crtBtn = (Button) findViewById(R.id.btnCreate);
        final EditText recipeNameText = (EditText) findViewById(R.id.recipeName);
        final EditText recipeDetailsText = (EditText) findViewById(R.id.recipeDetails);
        final EditText stepText = (EditText) findViewById(R.id.textStep);

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepList);

        lv.setAdapter(arrayAdapter);

        // add button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                if (stepText.length() > 0) {
                    recipeStepList.add(stepText.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    stepText.setText("");
                } else {
                    Toast.makeText(StoreRecipeActivity.this, "Steps area is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //delete list item
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Toast.makeText(StoreRecipeActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                recipeStepList.remove(pos);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });

        // create recipe button
        crtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recipeName = recipeNameText.getText().toString();
                recipeDetails = recipeDetailsText.getText().toString();
                if (recipeName.length() <= 0 || recipeStepList.size() <= 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreRecipeActivity.this);
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
                        obj.put("steps", recipeStepList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        obj.put("details", recipeDetails);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(StoreRecipeActivity.this, "Successful: " + response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(StoreRecipeActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();

                                }
                            });
                    queue.add(jsObjRequest);
                    clearTextAreas();

                }
            }
        });


    }

    private void clearTextAreas() {
        final EditText recipeNameText = (EditText) findViewById(R.id.recipeName);
        final EditText recipeDetailsText = (EditText) findViewById(R.id.recipeDetails);
        final EditText stepText = (EditText) findViewById(R.id.textStep);

        recipeNameText.setText("");
        stepText.setText("");
        recipeDetailsText.setText("");
        recipeStepList.clear();
        recipeDetails = "";
        recipeName = "";
    }

}
