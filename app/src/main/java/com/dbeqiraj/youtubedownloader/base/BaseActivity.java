package com.dbeqiraj.youtubedownloader.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dbeqiraj.youtubedownloader.application.App;
import com.dbeqiraj.youtubedownloader.di.components.ApplicationComponent;

import butterknife.ButterKnife;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        resolveDaggerDependency();
        //To be used by child activities
    }

    @Override
    protected void onDestroy() {
        //ButterKnife.unbind(this);
        super.onDestroy();
    }

    protected void resolveDaggerDependency() {}

    protected ApplicationComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    protected abstract int getContentView();
}
