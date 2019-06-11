package com.example.gastrogang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class StoreRecipeActivity extends AppCompatActivity {
    ArrayList recipeStepList = new ArrayList<String>();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_recipe);


        ListView lv = (ListView) findViewById(R.id.list);
        Button addBtn = (Button) findViewById(R.id.btAdd);
        final EditText stepText = (EditText) findViewById(R.id.textStep) ;

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepList);

        lv.setAdapter(arrayAdapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add new Items to List
                recipeStepList.add(stepText.getText().toString());
                arrayAdapter.notifyDataSetChanged();
                stepText.setText("");
            }
        });

    }
}
