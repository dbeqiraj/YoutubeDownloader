package com.dbeqiraj.youtubedownloader.di.components;

import android.content.Context;

import com.dbeqiraj.youtubedownloader.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Retrofit exposeRetrofit();

    Context exposeContext();
}
