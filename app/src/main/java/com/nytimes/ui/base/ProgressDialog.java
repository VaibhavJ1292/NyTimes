package com.nytimes.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import com.nytimes.R;


public class ProgressDialog extends Dialog {
    private static ProgressDialog progressDialog;

    public static ProgressDialog getInstance(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        return progressDialog;
    }

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.progress_dialog);
    }

    public void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }

    }

    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
