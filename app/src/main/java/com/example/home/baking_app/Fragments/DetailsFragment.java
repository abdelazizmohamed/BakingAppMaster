package com.example.home.baking_app.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.home.baking_app.Acivities.MainActivity;
import com.example.home.baking_app.Adapters.IngredientAdapter;
import com.example.home.baking_app.Adapters.StepsAdapter;
import com.example.home.baking_app.JsonModels.Ingredient;
import com.example.home.baking_app.JsonModels.Recipe;
import com.example.home.baking_app.JsonModels.Step;
import com.example.home.baking_app.R;

import java.util.ArrayList;
/**
 * Created by Home on 3/3/2018.
 */

public class DetailsFragment extends Fragment {

    Recipe recipe;

    ArrayList<Step>steps;
    ArrayList<Ingredient>ingredients;

    RecyclerView recyclerIngredient;
    RecyclerView recyclerSteps;

    IngredientAdapter ingredientAdapter;
    StepsAdapter stepsAdapter;

    ScrollView scrollView;
    Parcelable ingradientParcelable;
    Parcelable stepParcelable;
    int id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

            recyclerIngredient=view.findViewById(R.id.recylerview_ingredient);
            recyclerSteps=view.findViewById(R.id.recylerview_steps);
            scrollView =view.findViewById(R.id.scrollrecipe);

     if( savedInstanceState == null ){

         if((getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                 ||(getActivity().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
           {
              // recipe = getIntent().getExtras().getParcelable("recipe");
              // steps = getIntent().getExtras().getParcelableArrayList("steps");
             //  id= getIntent().getExtras().getInt("id");
              // Bundle bundle = new Bundle();
              // bundle.putParcelable("recipe", recipe);
              // bundle.putParcelableArrayList("steps", steps);
              // bundle.putInt("id", 0);
             //  setArguments(bundle);

               recipe = getArguments().getParcelable("recipe");
           }
           else
           {
               recipe = getActivity().getIntent().getExtras().getParcelable("recipe");
           }
         //  recipe = getArguments().getParcelable("recipe");
         // initialization for ingredient and steps value before app rotate
           ingredients = recipe.getIngredients();
           steps = recipe.getSteps();

           ingredientAdapter = new IngredientAdapter(getActivity(), ingredients);
           LinearLayoutManager ingManager = new LinearLayoutManager(getContext());
           recyclerIngredient.setLayoutManager(ingManager);
           recyclerIngredient.setAdapter(ingredientAdapter);
           recyclerIngredient.setNestedScrollingEnabled(false);
           recyclerIngredient.getLayoutManager().onRestoreInstanceState(ingradientParcelable);

           stepsAdapter = new StepsAdapter(getActivity(),steps );
           LinearLayoutManager StepManager = new LinearLayoutManager(getContext());
           recyclerSteps.setLayoutManager(StepManager);
           recyclerSteps.setAdapter(stepsAdapter);
           recyclerSteps.setNestedScrollingEnabled(false);
           recyclerSteps.getLayoutManager().onRestoreInstanceState(stepParcelable);

       }else{

         recipe = savedInstanceState.getParcelable("recipe");

         // initialization for ingredient and steps value after app rotated
         ingredients = (ArrayList<Ingredient>) savedInstanceState.getSerializable("ingredients");
         steps =(ArrayList<Step>) savedInstanceState.getSerializable("Steps");

         ingredientAdapter = new IngredientAdapter
                 (getActivity(), ingredients);
         LinearLayoutManager ingManager = new LinearLayoutManager(getContext());
         recyclerIngredient.setLayoutManager(ingManager);
         recyclerIngredient.setAdapter(ingredientAdapter);
         recyclerIngredient.setNestedScrollingEnabled(false);
         recyclerIngredient.getLayoutManager().onRestoreInstanceState(ingradientParcelable);

         stepsAdapter = new StepsAdapter
                 (getActivity(),steps );
         LinearLayoutManager stepManager = new LinearLayoutManager(getContext());
         recyclerSteps.setLayoutManager(stepManager);
         recyclerSteps.setAdapter(stepsAdapter);
         recyclerSteps.setNestedScrollingEnabled(false);
         recyclerSteps.getLayoutManager().onRestoreInstanceState(stepParcelable);

     }

        return view;
    }

    public static int scrollX = 0;
    public static int scrollY = 1;

    @Override
     public void onPause() {
        super.onPause();
        scrollX = scrollView.getScrollX();
        scrollY = scrollView.getScrollY();

    }
    @Override
    public void onResume() {
        super.onResume();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(scrollX, scrollY);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("recyclerIngredient", recyclerIngredient.getLayoutManager().onSaveInstanceState());
        outState.putParcelable("recyclerSteps", recyclerSteps.getLayoutManager().onSaveInstanceState());
        outState.putParcelable("recipe", recipe);

        outState.putSerializable("ingredients", ingredients);
        outState.putSerializable("Steps", steps);

        outState.putIntArray("SCROLL_POSITION",
                new int[]{ scrollX,scrollY });

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState !=null) {

            ingradientParcelable = savedInstanceState.getParcelable("recyclerIngredient");
             stepParcelable = savedInstanceState.getParcelable("recyclerSteps");

            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(scrollX, scrollY);
                }
            });
        }
    }
}



