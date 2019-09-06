package com.nytimes.network;

import android.annotation.SuppressLint;
import android.content.Context;
import com.nytimes.api.FeedDetailResponse;
import com.nytimes.ui.dash.detail.FeedDetailViewModel;
import com.nytimes.ui.dash.feeds.HomeViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NetworkClient {

    private NetWorkService mNetWorkService;


    public NetworkClient(NetWorkService netWorkService, Context context) {
        //super(context);
        mNetWorkService = netWorkService;

    }


    public Disposable doGetPopularFeedsApiCall(int timePeriod, String apiKey, HomeViewModel homeViewModel) {
        return mNetWorkService
                .doGetPopularFeedsApiCall(timePeriod, apiKey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(homeViewModel::handlePopularFeedResponse,
                        homeViewModel::handleErrors);

    }


    @SuppressLint("CheckResult")
    public void getFeedDetails(String url,String date, FeedDetailViewModel feedDetailViewModel) {
        FeedDetailResponse feedDetailModel = new FeedDetailResponse();
        Observable.fromCallable(() -> {
            Document document = Jsoup.connect(url).get();
            feedDetailModel.setDate(date);
            feedDetailModel.setTitle(document.title());
            feedDetailModel.setContent(document.select("p").text());
            return false;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> feedDetailViewModel.handleFeedDetailsResponse(feedDetailModel),
                        (error -> feedDetailViewModel.handleErrors(error)));
    }
}
