package com.nytimes.ui.base;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import com.nytimes.R;

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    private ProgressDialog progressDialog;
    protected T mViewDataBinding;
    protected V mViewModel;

    /**
     * To check activity needs a toolbar
     */
    protected Boolean mToolbarRequired = true;

    /**
     * To handle actions on toolbar
     */
    private Toolbar mToolbar;

    protected abstract String getToolbarTitle();

    private boolean mRequireToolbar = true;
    private TextView mToolbarTitle;

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    protected abstract V getViewModel();

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    protected abstract int getBindingVariable();

    /**
     * Override for set views
     */
    protected abstract void performUiOperations();


    /**
     * To initialise live data observables
     */
    protected abstract void initLiveDataObservables();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        performDataBinding();
        performUiOperations();
        initLiveDataObservables();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mViewModel.getProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    showLoading();
                } else {
                    hideLoading();
                }
            }
        });

        mViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(BaseActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * To perform data binding operation
     */
    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = getViewModel();
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();

        //To initialise toolbar
        initializeToolbar();
    }

    private void initializeToolbar() {
        //set toolbar
        if (mToolbarRequired) {
            setToolBar();
        }
    }

    /**
     * To set tool bar
     */
    private void setToolBar() {
        mToolbar = mViewDataBinding.getRoot().findViewById(R.id.toolbarId);
        setSupportActionBar(mToolbar);

        if (getToolbarTitle() != null) {
            mToolbarTitle = (TextView) mToolbar.findViewById(R.id.title);
            mToolbarTitle.setText(getToolbarTitle());
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * To hide showing dialog
     */
    private void hideLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    /**
     * To load progress dialog on screen
     */
    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.getInstance(this);
        }
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

}
