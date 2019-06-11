package com.example.gastrogang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewActivity extends AppCompatActivity {

    String[] recipeArray = {"jamaican rum","fresh lime juice","simple syrup","Angostura Aromatic Bitters"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //TODO: HTTP REQUEST FOR VIEWING ALL RECIPES FOR THE USER

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.recipelistitem, R.id.btnRecipe, recipeArray);

        ListView listView = (ListView) findViewById(R.id.recipe_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent recipe = new Intent(ViewActivity.this, RecipeActivity.class);
                recipe.putExtra("recipeName",recipeArray[i]);
                startActivity(recipe);
            }

        });

    }
}
