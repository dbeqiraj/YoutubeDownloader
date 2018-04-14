package com.dbeqiraj.youtubedownloader.api;

import com.dbeqiraj.youtubedownloader.mvp.model.Video;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public interface VideoApiService {

    @GET("/@api/json/mp3/{vidID}")
    Observable<Video> getVideo(@Path("vidID") String vidID);
}
