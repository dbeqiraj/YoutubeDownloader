package com.dbeqiraj.youtubedownloader.di.module;

import com.dbeqiraj.youtubedownloader.api.DownloadApiService;
import com.dbeqiraj.youtubedownloader.di.scope.PerService;
import com.dbeqiraj.youtubedownloader.mvp.view.NotificationView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by d.beqiraj on 4/13/2018.
 */

@Module
public class DownloadServiceModule {


    private NotificationView mView;

    public DownloadServiceModule(NotificationView view) {
        mView = view;
    }

    @PerService
    @Provides
    DownloadApiService provideDownloadApiService(Retrofit retrofit) {
        return retrofit.create(DownloadApiService.class);
    }

    @PerService
    @Provides
    NotificationView provideView() {
        return mView;
    }
}
