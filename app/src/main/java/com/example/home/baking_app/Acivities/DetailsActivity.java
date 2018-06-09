package com.example.home.baking_app.Acivities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.home.baking_app.JsonModels.Recipe;
import com.example.home.baking_app.JsonModels.Step;
import com.example.home.baking_app.R;
import com.example.home.baking_app.Fragments.DetailsFragment;
import com.example.home.baking_app.Fragments.StepsFragment;

import java.util.ArrayList;
/**
 * Created by Home on 3/3/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    Recipe recipe;
    ArrayList<Step> steps;
   // int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        recipe = getIntent().getExtras().getParcelable("recipe");
        steps = getIntent().getExtras().getParcelableArrayList("steps");
       // id= getIntent().getExtras().getInt("id");

      if(savedInstanceState==null){

        if((DetailsActivity.this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                ||(DetailsActivity.this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            // show part ingredient tablet
            DetailsFragment ingredientPart = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", recipe);
            bundle.putParcelableArrayList("steps", steps);
            bundle.putInt("id", 0);
            ingredientPart.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame1, ingredientPart)
                    .commit();

/*
            // show part steps tablet
            DetailsFragment fragment = new DetailsFragment();
            Bundle arg = new Bundle();
            arg.putParcelableArrayList("steps", steps);
            arg.putInt("id", 0);
            fragment.setArguments(arg);
            FragmentManager stepsFragment = getSupportFragmentManager();
            stepsFragment.beginTransaction()
                    .replace(R.id.frame1, fragment)
                    .commit();
*/

            // show part steps tablet
            StepsFragment stepsFragment1 = new StepsFragment();
            Bundle argss = new Bundle();
            argss.putParcelableArrayList("steps", steps);
            argss.putInt("id", 0);
            stepsFragment1.setArguments(argss);
            FragmentManager steps = getSupportFragmentManager();
            steps.beginTransaction()
                    .replace(R.id.frame2, stepsFragment1)
                    .commit();
        }
        else {
            // show part ingredient
            DetailsFragment ingredientPart = new DetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("recipe", recipe);
            ingredientPart.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame0, ingredientPart)
                    .commit();

            // show part steps
            DetailsFragment fragment = new DetailsFragment();
            Bundle arg = new Bundle();
            arg.putParcelableArrayList("steps", steps);
            arg.putInt("id", 0);
            fragment.setArguments(arg);
            FragmentManager stepsFragment = getSupportFragmentManager();
            stepsFragment.beginTransaction()
                    .replace(R.id.frame0, fragment)
                    .commit();
           }
        }
    }
}
