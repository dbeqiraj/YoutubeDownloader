package com.dbeqiraj.youtubedownloader.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public interface ButtonsApiService {

    @GET("/yt-api.php")
    Observable<ResponseBody> getVideo(@Query("id") String vidID);
}
