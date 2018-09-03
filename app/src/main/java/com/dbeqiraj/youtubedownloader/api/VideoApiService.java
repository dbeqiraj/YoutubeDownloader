package com.dbeqiraj.youtubedownloader.api;

import com.dbeqiraj.youtubedownloader.mvp.model.Video;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by d.beqiraj on 5/19/2018.
 */

public interface VideoApiService {

    @GET("/ytconverter/convert.php")
    Observable<Video> getVideo(@Query("youtubelink") String youtubelink);
}
