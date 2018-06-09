package com.example.home.baking_app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;



public class Service extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return(new WidgetDataProvider(this.getApplicationContext(), intent));
    }
}
