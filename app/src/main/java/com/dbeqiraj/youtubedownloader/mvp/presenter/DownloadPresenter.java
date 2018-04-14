package com.dbeqiraj.youtubedownloader.mvp.presenter;

import com.dbeqiraj.youtubedownloader.api.DownloadApiService;
import com.dbeqiraj.youtubedownloader.base.BasePresenter;
import com.dbeqiraj.youtubedownloader.mvp.view.NotificationView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

public class DownloadPresenter extends BasePresenter<NotificationView> implements Observer<ResponseBody> {

    @Inject
    protected DownloadApiService mApiService;

    @Inject
    public DownloadPresenter() {
    }

    public void download(String url) {
        Observable<ResponseBody> cakesResponseObservable = mApiService.download(url);
        subscribe(cakesResponseObservable, this);
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        getView().onVideoStreamDownloaded(responseBody);
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
