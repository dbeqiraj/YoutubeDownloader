package com.dbeqiraj.youtubedownloader.di.components;

import com.dbeqiraj.youtubedownloader.di.module.DownloadServiceModule;
import com.dbeqiraj.youtubedownloader.di.scope.PerService;
import com.dbeqiraj.youtubedownloader.modules.download.service.DownloadService;

import dagger.Component;

/**
 * Created by d.beqiraj on 4/13/2018.
 */

@PerService
@Component(modules = DownloadServiceModule.class, dependencies = ApplicationComponent.class)
public interface DownloadServiceComponent {

    void inject(DownloadService downloadService);
}