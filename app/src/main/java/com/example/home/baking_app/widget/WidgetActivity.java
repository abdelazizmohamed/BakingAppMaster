package com.example.home.baking_app.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.home.baking_app.GetAPI;
import com.example.home.baking_app.R;
import com.example.home.baking_app.JsonModels.Ingredient;
import com.example.home.baking_app.JsonModels.Recipe;
import com.example.home.baking_app.JsonModels.Widget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WidgetActivity extends Activity {


    ProgressDialog progressDialog;
    private ArrayList<Recipe> recipes;
    private ListView list;
    int WidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.widget_configure);

        list =  findViewById(R.id.config_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Context context = WidgetActivity.this;

                Widget widget=new Widget(recipes.get(position).getName(),
                        (ArrayList<Ingredient>) recipes.get(position).getIngredients());
                WidgetProvider.databaseLibrary.putObject(String.valueOf(WidgetId),widget);
                AppWidgetManager Manager = AppWidgetManager.getInstance(context);
                WidgetProvider.updateAppWidget(context, Manager, WidgetId);
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("massage");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle != null) {
            WidgetId = bundle.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (WidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }



         getWidget();

        }

    public void getWidget() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GetAPI.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetAPI API = retrofit.create(GetAPI.class);
        Call<List<Recipe>> ReCall = API.getWidget();

        ReCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                int i=0;
                progressDialog.dismiss();
                recipes = (ArrayList<Recipe>) response.body();
                String[]values= new String[recipes.size()];
                while(i < recipes.size()){
                    values[i]=recipes.get(i).getName();
                    i++;
                }
                list.setAdapter(new ArrayAdapter<String>(WidgetActivity.this, android.R.layout.simple_list_item_1, values));
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }
}


