package com.nytimes.ui;

import android.app.Application;
import com.nytimes.network.RetrofitClientHelper;

public class AppNyTimes extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setUpRetrofit();
    }

    /**
     * To setup retrofit in app
     */
    private void setUpRetrofit() {
        //create retrofit client
        RetrofitClientHelper.getInstance().setContext(this);
    }


}
