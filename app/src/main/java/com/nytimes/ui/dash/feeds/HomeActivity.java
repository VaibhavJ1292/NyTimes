package com.nytimes.ui.dash.feeds;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.nytimes.BR;
import com.nytimes.R;
import com.nytimes.api.Result;
import com.nytimes.databinding.ActivityHomeBinding;
import com.nytimes.ui.base.BaseActivity;
import com.nytimes.ui.dash.detail.FeedDetailActivity;
import com.nytimes.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    private FeedAdapter mAdapter;
    private List<Result> mFeedsList = new ArrayList<>();

    @Override
    protected String getToolbarTitle() {
        return getResources().getString(R.string.app_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected HomeViewModel getViewModel() {
        return ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    protected void performUiOperations() {
        mAdapter = new FeedAdapter(this, mFeedsList);
        mViewDataBinding.recycler.setAdapter(mAdapter);
        mViewModel.doGetPopularFeedsApiCall(1);
    }


    @Override
    protected void initLiveDataObservables() {
        mViewModel.getPopularFeedData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {
                if (results != null) {
                    mFeedsList.clear();
                    mFeedsList.addAll(results);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mAdapter.getOnViewItemClick().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null) {
                    Intent intent = new Intent(HomeActivity.this, FeedDetailActivity.class);
                    intent.putExtra(AppConstants.KEY_FEED_URL, mFeedsList.get(integer).getUrl());
                    intent.putExtra(AppConstants.KEY_FEED_DATE, mFeedsList.get(integer).getPublishedDate());
                    startActivity(intent);
                } else {
                    mViewModel.handleApiErrors(getResources().getString(R.string.error_try_again));
                }
            }
        });
    }
}
