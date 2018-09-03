package com.dbeqiraj.youtubedownloader.application;

import android.app.Application;

import com.dbeqiraj.youtubedownloader.di.components.ApplicationComponent;
import com.dbeqiraj.youtubedownloader.di.components.DaggerApplicationComponent;
import com.dbeqiraj.youtubedownloader.di.module.ApplicationModule;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public class App extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeApplicationComponent();
        initializeFresco();
    }

    private void initializeFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }

    private void initializeApplicationComponent() {
        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this, "http://michaelbelgium.me"))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
