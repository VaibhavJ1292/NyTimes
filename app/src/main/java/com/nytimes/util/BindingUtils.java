package com.nytimes.util;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import com.nytimes.api.Result;
import com.nytimes.ui.dash.feeds.FeedAdapter;

import java.util.List;

public class BindingUtils {


    @BindingAdapter("popularFeeds")
    public static void setPopularFeeds(RecyclerView recyclerView, List<Result> list) {
        if (recyclerView.getAdapter() != null) {
            FeedAdapter mAdapter = (FeedAdapter) recyclerView.getAdapter();
            mAdapter.updateData(list);
        } else {
            FeedAdapter mAdapter = new FeedAdapter(recyclerView.getContext(), list);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
