package com.example.gastrogang;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class StoreRecipeActivity extends AppCompatActivity {
    ArrayList recipeStepList = new ArrayList<String>();
    String recipeName = new String();
    String recipeDetails = new String();

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
                recipeStepList.add(stepText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                stepText.setText("");
            }
        });

        //delete list item
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
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
                if (recipeName.length()<=0 || recipeDetails.length() <=0 || recipeStepList.size()<=0) {
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
                }
                Log.e("MyApp",recipeName + " " + recipeDetails);
            }
        });


    }
}
