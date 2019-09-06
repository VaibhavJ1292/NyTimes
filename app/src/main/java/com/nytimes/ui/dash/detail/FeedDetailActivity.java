package com.nytimes.ui.dash.detail;

import android.arch.lifecycle.ViewModelProviders;
import com.nytimes.BR;
import com.nytimes.R;
import com.nytimes.databinding.ActivityFeedDetailsBinding;
import com.nytimes.ui.base.BaseActivity;
import com.nytimes.util.AppConstants;

public class FeedDetailActivity extends BaseActivity<ActivityFeedDetailsBinding, FeedDetailViewModel> {
    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_details;
    }

    @Override
    protected FeedDetailViewModel getViewModel() {
        return ViewModelProviders.of(this).get(FeedDetailViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    protected void performUiOperations() {
        if (getIntent().getExtras() != null) {
            mViewModel.doGetFeedDetails(getIntent().getExtras().getString(AppConstants.KEY_FEED_URL), getIntent().getExtras().getString(AppConstants.KEY_FEED_DATE));
        } else {
            mViewModel.handleApiErrors(getResources().getString(R.string.error_something_went_wrong));
            finish();
        }
    }

    @Override
    protected void initLiveDataObservables() {

    }
}
