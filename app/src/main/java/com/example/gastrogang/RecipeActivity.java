package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        TextView recipeName = findViewById(R.id.txtrcpRecipeName);

        String[] recipeStepList = {"Slice", "Blend", "Age", "Cook"};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("recipeName");
            recipeName.setText(value);
            //TODO: SET recipeStepList and RecipeDetails
        }

        ListView lv = findViewById(R.id.rcpSteplist);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepList);

        lv.setAdapter(arrayAdapter);

        Button deleteRecipe = findViewById(R.id.btnDeleteRecipe);

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: HTTP REQUEST FOR DELETE

                Intent intent = new Intent(RecipeActivity.this, ViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
