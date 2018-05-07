package com.dbeqiraj.youtubedownloader.di.module;

import com.dbeqiraj.youtubedownloader.api.ButtonsApiService;
import com.dbeqiraj.youtubedownloader.di.scope.PerActivity;
import com.dbeqiraj.youtubedownloader.mvp.view.DownloadView;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

@Module
public class ButtonsModule {

    private DownloadView mView;

    public ButtonsModule(DownloadView view) {
        mView = view;
    }

    @PerActivity
    @Provides
    ButtonsApiService provideButtonsApiService(Retrofit retrofit) {
        return retrofit.create(ButtonsApiService.class);
    }

    @PerActivity
    @Provides
    DownloadView provideView() {
        return mView;
    }
}
