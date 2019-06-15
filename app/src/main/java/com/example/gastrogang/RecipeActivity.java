package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
            ACCESS_TOKEN = extras.getString("token");
            recipeName = extras.getString("name");
            recipeDetails = extras.getString("details");
            recipeIngredientsList = extras.getStringArrayList("ingredients");
            recipeStepsList = extras.getStringArrayList("steps");
        }
        else {
            Toast.makeText(RecipeActivity.this, "Unexpected Error" , Toast.LENGTH_LONG).show();
        }
        txtviewRecipeName.setText(recipeName);
        txtviewRecipeDetails.setText(recipeDetails);

        ListView lvIngredients = findViewById(R.id.rcpSteplist);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeIngredientsList);
        lvIngredients.setAdapter(ingredientsAdapter);

        ListView lvSteps = findViewById(R.id.rcpIngredientList);
        ArrayAdapter<String> stepsAdapter = new ArrayAdapter<String>
                (getApplicationContext(), android.R.layout.simple_list_item_1, recipeStepsList);
        lvSteps.setAdapter(stepsAdapter);

        Button deleteRecipe = findViewById(R.id.btnDeleteRecipe);

        deleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: HTTP REQUEST FOR DELETE

                Intent intent = new Intent(RecipeActivity.this, ViewActivity.class);
                intent.putExtra("token", ACCESS_TOKEN);
                startActivity(intent);
                finish();
            }
        });
    }
}