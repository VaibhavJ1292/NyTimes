package com.nytimes.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {
    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
