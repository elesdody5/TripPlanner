package com.tripplanner.data_layer.local_data;

import android.app.Application;

public class App extends Application {
    public static Room tripDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        tripDatabase = Room.getInstance(this);
    }
}
