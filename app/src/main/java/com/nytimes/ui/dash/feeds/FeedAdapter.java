package com.nytimes.ui.dash.feeds;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nytimes.BR;
import com.nytimes.R;
import com.nytimes.api.Result;
import io.reactivex.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.RecyclerBaseViewHolder> {

    private List<Result> data = new ArrayList<Result>();
    private Context mContext;
    private MutableLiveData<Integer> onFeedClicked = new MutableLiveData();

    // Provide a suitable constructor (depends on the kind of dataset)
    public FeedAdapter(Context mContext, List<Result> mData) {
        data = mData;
        this.mContext = mContext;
    }

    public void updateData(List<Result> mDataList) {
        List<Result> data;
        data = (List<Result>) mDataList;
        data.clear();
        data.addAll(mDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_feed_popular, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new RecyclerBaseViewHolder(binding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FeedAdapter.RecyclerBaseViewHolder holder, int position) {
        holder.bind(data.get(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedClicked.setValue(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class RecyclerBaseViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private final ViewDataBinding binding;

        RecyclerBaseViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Result obj) {
            binding.setVariable(BR.feed, obj);
            binding.executePendingBindings();
        }
    }

    /**
     * To notify selected position
     * @return selected positions int value
     */
    public LiveData<Integer> getOnViewItemClick(){
        return onFeedClicked;
    }
}
