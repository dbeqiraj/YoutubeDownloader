package com.dbeqiraj.youtubedownloader.di.module;

import com.dbeqiraj.youtubedownloader.api.VideoApiService;
import com.dbeqiraj.youtubedownloader.di.scope.PerActivity;
import com.dbeqiraj.youtubedownloader.mvp.view.DownloadView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

@Module
public class VideoModule {

    private DownloadView mView;

    public VideoModule(DownloadView view) {
        mView = view;
    }

    @PerActivity
    @Provides
    VideoApiService provideVideoApiService(Retrofit retrofit) {
        return retrofit.create(VideoApiService.class);
    }

    @PerActivity
    @Provides
    DownloadView provideView() {
        return mView;
    }
}
