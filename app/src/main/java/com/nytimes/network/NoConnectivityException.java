package com.nytimes.network;

import android.content.Context;
import com.nytimes.R;

import java.io.IOException;


public class NoConnectivityException extends IOException {

    private Context mContext;

    public NoConnectivityException(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public String getMessage() {
        return mContext.getResources().getString(R.string.error_no_network);
    }
}
