package com.dbeqiraj.youtubedownloader.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

public interface DownloadApiService {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
