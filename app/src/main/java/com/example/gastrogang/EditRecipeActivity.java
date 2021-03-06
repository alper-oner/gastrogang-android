package com.example.gastrogang;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditRecipeActivity extends AppCompatActivity {
    ArrayList recipeStepList = new ArrayList<String>();
    ArrayList recipeIngredientList = new ArrayList<String>();
    String recipeName = new String();
    String recipeDetails = new String();
    ArrayList recipeTagList = new ArrayList();
    private Button btnCapture;
    private ImageView imgCapture;
    private static final int Image_Capture_Code = 1;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        Toolbar actionbarLogin = findViewById(R.id.actionbarLogin);
        setSupportActionBar(actionbarLogin);
        getSupportActionBar().setTitle("EDIT RECIPE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionbarLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(EditRecipeActivity.this, RecipeActivity.class);
                recipeIntent.putExtra("token", getIntent().getStringExtra("token"));
                recipeIntent.putExtra("name", recipeName);
                recipeIntent.putExtra("steps", recipeStepList);
                recipeIntent.putExtra("ingredients", recipeIngredientList);
                recipeIntent.putExtra("details", recipeDetails);
                recipeIntent.putExtra("id", getIntent().getStringExtra("id"));
                recipeIntent.putExtra("tags", recipeTagList);
                startActivity(recipeIntent);
                finish();
            }
        });

        ListView stepLv = (ListView) findViewById(R.id.editListSteps);
        ListView ingredientsLv = (ListView) findViewById(R.id.editListIngredients);
        Button addStepBtn = (Button) findViewById(R.id.editBtAdd);
        Button updateRecipeBtn = (Button) findViewById(R.id.editBtnUpdate);
        Button addIngredientsBtn = (Button) findViewById(R.id.editBtAddIngredients);
        ListView tagLv = (ListView) findViewById(R.id.editListTags);
        Button addTagBtn = (Button) findViewById(R.id.editBtAddTags);

        final EditText recipeNameText = (EditText) findViewById(R.id.editRecipeName);
        final EditText recipeDetailsText = (EditText) findViewById(R.id.editRecipeDetails);
        final EditText stepText = (EditText) findViewById(R.id.editTextStep);
        final EditText ingredientsText = (EditText) findViewById(R.id.editTextIngredients);
        final EditText tagsText = findViewById(R.id.editTextTag);

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> stepListAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepList);

        final ArrayAdapter<String> ingredientListAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeIngredientList);

        final ArrayAdapter<String> tagListAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeTagList);

        stepLv.setAdapter(stepListAdapter);
        ingredientsLv.setAdapter(ingredientListAdapter);
        tagLv.setAdapter(tagListAdapter);

        //get old values
        recipeName = getIntent().getStringExtra("name");
        recipeDetails = getIntent().getStringExtra("details");

        ArrayList tempRecipeIngredientList = new ArrayList<String>();
        ArrayList tempRecipeStepList = new ArrayList<String>();
        tempRecipeIngredientList = getIntent().getStringArrayListExtra("ingredients");
        tempRecipeStepList = getIntent().getStringArrayListExtra("steps");
        ArrayList tempRecipeTagList = getIntent().getStringArrayListExtra("tags");

        recipeIngredientList.addAll(tempRecipeIngredientList);
        recipeStepList.addAll(tempRecipeStepList);
        recipeTagList.addAll(tempRecipeTagList);

        recipeNameText.setText(recipeName);
        recipeDetailsText.setText(recipeDetails);
        stepListAdapter.notifyDataSetChanged();
        ingredientListAdapter.notifyDataSetChanged();
        tagListAdapter.notifyDataSetChanged();

        btnCapture = (Button) findViewById(R.id.btnTakePicture);
        imgCapture = (ImageView) findViewById(R.id.capturedImage);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt, Image_Capture_Code);
            }
        });

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

        // add tags button
        addTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                if (tagsText.length() > 0) {
                    recipeTagList.add(tagsText.getText().toString());
                    tagListAdapter.notifyDataSetChanged();
                    tagsText.setText("");
                } else {
                    Toast.makeText(EditRecipeActivity.this, "Tags area is empty.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //delete tags list item
        tagLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Toast.makeText(EditRecipeActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
                recipeTagList.remove(pos);
                tagListAdapter.notifyDataSetChanged();
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
                    try {
                        JSONArray jsonArrayTag = new JSONArray();
                        for (int i = 0; i < recipeTagList.size(); i++) {
                            jsonArrayTag.put(recipeTagList.get(i));
                        }
                        obj.put("tags", jsonArrayTag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String ID = getIntent().getStringExtra("id");
                    String url = "https://gastrogang.herokuapp.com/api/v1/recipes/" + ID;
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Toast.makeText(EditRecipeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                    Intent recipeIntent = new Intent(EditRecipeActivity.this, RecipeActivity.class);
                                    recipeIntent.putExtra("token", getIntent().getStringExtra("token"));
                                    recipeIntent.putExtra("name", recipeName);
                                    recipeIntent.putExtra("steps", recipeStepList);
                                    recipeIntent.putExtra("ingredients", recipeIngredientList);
                                    recipeIntent.putExtra("details", recipeDetails);
                                    recipeIntent.putExtra("id", getIntent().getStringExtra("id"));
                                    recipeIntent.putExtra("tags", recipeTagList);
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
                    queue.add(jsObjRequest);


                    JSONArray arr = new JSONArray();
                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("type", "step1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonParam.put("img", encodedImage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    arr.put(jsonParam);
                    JSONObject mainObj = new JSONObject();
                    try {
                        mainObj.put("photos", arr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    url = "https://gastrogang.herokuapp.com/api/v1/recipes/" + ID + "/photo";
                    queue = Volley.newRequestQueue(getApplicationContext());
                    JsonObjectRequest request_json = new JsonObjectRequest(Request.Method.POST, url, mainObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    VolleyLog.e("Success: ");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            VolleyLog.e("Error: ", volleyError.getMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            String ACCESS_TOKEN = getIntent().getStringExtra("token");
                            params.put("Authorization", "Bearer " + ACCESS_TOKEN);
                            return params;
                        }

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
                    queue.add(request_json);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                imgCapture.setImageBitmap(bp);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
