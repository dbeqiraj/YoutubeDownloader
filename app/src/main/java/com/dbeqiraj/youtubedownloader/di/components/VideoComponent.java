package com.dbeqiraj.youtubedownloader.di.components;

import com.dbeqiraj.youtubedownloader.di.module.VideoModule;
import com.dbeqiraj.youtubedownloader.di.scope.PerActivity;
import com.dbeqiraj.youtubedownloader.modules.download.DownloadActivity;

import dagger.Component;

/**
 * Created by d.beqiraj on 5/19/2018.
 */

@PerActivity
@Component(modules = VideoModule.class, dependencies = ApplicationComponent.class)
public interface VideoComponent {

    void inject(DownloadActivity activity);
}

