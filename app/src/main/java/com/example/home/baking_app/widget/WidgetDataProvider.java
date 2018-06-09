package com.example.home.baking_app.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.home.baking_app.JsonModels.Ingredient;
import com.example.home.baking_app.JsonModels.Widget;

import com.example.home.baking_app.R;

import java.util.ArrayList;


public class WidgetDataProvider  implements RemoteViewsService.RemoteViewsFactory {

    private Context context=null;
    ArrayList<Ingredient> ingredients;
    private int appWidgetId;

    public WidgetDataProvider(Context context, Intent intent) {
        this.context=context;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews view=new RemoteViews(context.getPackageName(), R.layout.widget_item);
        view.setTextViewText(R.id.widget_recipe, ingredients.get(position).getIngredient());
        view.setTextViewText(R.id.widget_measure, ingredients.get(position).getQuantity()
                + " " + ingredients.get(position).getMeasure());

        return(view);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        ingredients= new ArrayList<>();
        Widget model= (Widget) WidgetProvider.databaseLibrary.getObject(String.valueOf(appWidgetId),Widget.class);
        ingredients=model.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return(ingredients.size());
    }
    @Override
    public RemoteViews getLoadingView() {return(null);}

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

}