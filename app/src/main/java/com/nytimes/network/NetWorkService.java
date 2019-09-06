package com.nytimes.network;

import com.nytimes.api.PopularFeedResponse;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetWorkService {

    @GET(ApiEndPoints.ENDPOINT_GET_POPULAR_EVENTS)
    Observable<PopularFeedResponse> doGetPopularFeedsApiCall(@Path("period") int path,
                                                             @Query("api-key") String apiKey);

}
