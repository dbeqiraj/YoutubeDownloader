package com.dbeqiraj.youtubedownloader.di.components;

import com.dbeqiraj.youtubedownloader.di.module.ButtonsModule;
import com.dbeqiraj.youtubedownloader.di.scope.PerActivity;
import com.dbeqiraj.youtubedownloader.modules.download.DownloadActivity;

import dagger.Component;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

@PerActivity
@Component(modules = ButtonsModule.class, dependencies = ApplicationComponent.class)
public interface ButtonsComponent {

    void inject(DownloadActivity activity);
}
