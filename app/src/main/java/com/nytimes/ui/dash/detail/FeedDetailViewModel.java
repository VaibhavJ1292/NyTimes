package com.nytimes.ui.dash.detail;

import android.app.Application;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import com.nytimes.R;
import com.nytimes.BR;
import com.nytimes.api.FeedDetailResponse;
import com.nytimes.ui.base.BaseViewModel;

public class FeedDetailViewModel extends BaseViewModel {

    private String mDate = "";
    private String mTitle = "";
    private String mContents = "";


    public FeedDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void doGetFeedDetails(String url, String date) {
        showProcess.setValue(true);
        mNetworkClient.getFeedDetails(
                url,date,
                this);

    }

    public void handleFeedDetailsResponse(FeedDetailResponse feedDetailResponse) {
        if (feedDetailResponse != null) {
            setDate(feedDetailResponse.getDate());
            setTitle(feedDetailResponse.getTitle());
            setContents(feedDetailResponse.getContent());
        } else {
            handleApiErrors(app.getResources().getString(R.string.error_something_went_wrong));
        }
        showProcess.setValue(false);
    }


    @Bindable
    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getContents() {
        return mContents;
    }

    public void setContents(String contents) {
        this.mContents = contents;
        notifyPropertyChanged(BR.contents);
    }
}
