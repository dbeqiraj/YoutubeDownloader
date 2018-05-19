package com.dbeqiraj.youtubedownloader.mvp.presenter;

import com.dbeqiraj.youtubedownloader.api.VideoApiService;
import com.dbeqiraj.youtubedownloader.base.BasePresenter;
import com.dbeqiraj.youtubedownloader.mvp.model.Video;
import com.dbeqiraj.youtubedownloader.mvp.view.DownloadView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by d.beqiraj on 5/19/2018.
 */

public class VideoPresenter extends BasePresenter<DownloadView> implements Observer<Video> {

    @Inject
    protected VideoApiService mApiService;

    @Inject
    public VideoPresenter() {
    }

    public void getVideo(String vidID) {
        getView().onStartVidInfDownload();
        Observable<Video> cakesResponseObservable = mApiService.getVideo(vidID);
        subscribe(cakesResponseObservable, this);
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(Video video) {
        getView().onVideoInfoDownloaded(video);
        getView().onDismissNotification();
    }

    @Override
    public void onError(Throwable e) {
        if ( e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            getView().onShowConnectionError();
        } else {
            getView().onGeneralError();
        }
        getView().onDismissNotification();
    }

    @Override
    public void onComplete() {
    }
}
