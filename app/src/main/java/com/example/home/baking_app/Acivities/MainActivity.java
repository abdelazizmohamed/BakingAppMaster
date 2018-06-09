package com.example.home.baking_app.Acivities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.home.baking_app.Adapters.RecipeAdapter;
import com.example.home.baking_app.GetAPI;
import com.example.home.baking_app.JsonModels.Recipe;
import com.example.home.baking_app.R;
import com.example.home.baking_app.ResourceId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Home on 3/2/2018.
 */

public class MainActivity extends AppCompatActivity {

    private ResourceId resourceId;
    @VisibleForTesting
    @NonNull
    public android.support.test.espresso.IdlingResource getIdlingResouce(){
        if(resourceId == null)
            resourceId = new ResourceId();
        return resourceId;
    }

    GetAPI API;
    ArrayList<Recipe> homeArrayList = new ArrayList<>();
    private RecyclerView mainRecycler;
    private RecipeAdapter recipeAdapter;
    Parcelable parcelable;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainRecycler = findViewById(R.id.home_recyler);
        scrollView=findViewById(R.id.scrollView);

        if (savedInstanceState != null) {
            homeArrayList = (ArrayList<Recipe>) savedInstanceState.getSerializable("listAfterRotation");
            openCall();
            if (parcelable != null) {
                mainRecycler.getLayoutManager().onRestoreInstanceState(parcelable);
            }
        }

        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        mainRecycler.setLayoutManager(manager);
        recipeAdapter = new RecipeAdapter(MainActivity.this, homeArrayList);

        openCall();
    }

    public void openCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetAPI.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API = retrofit.create(GetAPI.class);
        Call<ArrayList<Recipe>> ReCall = API.getRecipes();
        ReCall.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                homeArrayList = response.body();

                Log.d("OK", "on " + homeArrayList.size());

                if((MainActivity.this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                        ||(MainActivity.this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE)
                {
                    mainRecycler.setLayoutManager( new GridLayoutManager(MainActivity.this,1));
                    mainRecycler.setAdapter(new RecipeAdapter(MainActivity.this, homeArrayList));
                }
                else{
                    mainRecycler.setLayoutManager( new LinearLayoutManager(MainActivity.this));
                    mainRecycler.setAdapter(new RecipeAdapter(MainActivity.this, homeArrayList));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Snackbar.make(mainRecycler,
                        " Check internet connection!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static int scrollX = 0;
    public static int scrollY = 1;

    @Override
    protected void onPause() {
        super.onPause();
        scrollX = scrollView.getScrollX();
        scrollY = scrollView.getScrollY();

    }
    @Override
    protected void onResume() {
        super.onResume();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(scrollX, scrollY);
            }
        });
    }

    //save value on onSaveInstanceState
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("listAfterRotation",  homeArrayList);

        outState.putParcelable("position", mainRecycler.getLayoutManager().onSaveInstanceState());

        outState.putIntArray("SCROLL_POSITION",
                new int[]{ scrollX, scrollY});

    }

    //Restore them on onRestoreInstanceState
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        parcelable = savedInstanceState.getParcelable("position");

        final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");

        if(position != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(scrollX,scrollY);
                }
            });
    }


}
