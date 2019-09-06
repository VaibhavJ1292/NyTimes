package com.nytimes.ui.dash.feeds;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import com.nytimes.BR;
import com.nytimes.BuildConfig;
import com.nytimes.R;
import com.nytimes.api.PopularFeedResponse;
import com.nytimes.api.Result;
import com.nytimes.ui.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends BaseViewModel {
    private Application app;
    private MutableLiveData<List<Result>> mDataList = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
        app = application;
    }

    public void doGetPopularFeedsApiCall(int days) {
        showProcess.setValue(true);
        mDisposable = mNetworkClient.doGetPopularFeedsApiCall(
                days, BuildConfig.API_KEY,
                this);
    }

    public void handlePopularFeedResponse(PopularFeedResponse popularFeedResponse) {
        showProcess.setValue(false);
        if (popularFeedResponse.getStatus().equals(app.getResources().getString(R.string.status_ok))) {
            if (popularFeedResponse.getResults().isEmpty()) {
                handleApiErrors(app.getResources().getString(R.string.error_no_data_found));
            } else {
                setPopularFeedData(popularFeedResponse.getResults());
            }
        } else {
            handleApiErrors(app.getResources().getString(R.string.error_something_went_wrong));
        }
    }

    @Bindable
    public LiveData<List<Result>> getPopularFeedData() {
        return mDataList;
    }

    public void setPopularFeedData(List<Result> items) {
        //mDataList.clear();
        //mDataList.addAll(items);
        //notifyPropertyChanged(BR.popularFeedData);
        mDataList.setValue(items);
    }

}
