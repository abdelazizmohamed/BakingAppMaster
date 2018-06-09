package com.example.home.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.example.home.*;
import com.example.home.baking_app.Acivities.DetailsActivity;
import com.example.home.baking_app.JsonModels.Widget;
import com.example.home.baking_app.R;
import com.example.home.baking_app.Acivities.MainActivity;


public class WidgetProvider extends AppWidgetProvider {

    public  static WidgetDatabaseLibrary databaseLibrary;
    public static Widget widget;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetIds) {

        widget = new Widget();
        databaseLibrary= new WidgetDatabaseLibrary(context);
        RemoteViews views= new RemoteViews(context.getPackageName(),R.layout.widget);
        String title=widget.getRecipeTitle(appWidgetIds);
         views.setTextViewText(R.id.recipe_name,title);

        Intent intent= new Intent(context,Service.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.listViewWidget, intent);

        Intent appIntent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0
                ,appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listViewWidget,pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, views);

    }
}