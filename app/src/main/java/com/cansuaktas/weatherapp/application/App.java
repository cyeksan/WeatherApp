package com.cansuaktas.weatherapp.application;

import androidx.appcompat.app.AppCompatActivity;


public class App extends AppCompatActivity {

    public static App instance = null;

    static {
        System.loadLibrary("libnative_lib");
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public native String getApiKey(int id);

}
