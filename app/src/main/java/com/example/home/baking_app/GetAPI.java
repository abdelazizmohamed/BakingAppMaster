package com.example.home.baking_app;

import com.example.home.baking_app.JsonModels.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Home on 3/1/2018.
 */

public interface GetAPI {

    String API_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipes();

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getWidget();

}
