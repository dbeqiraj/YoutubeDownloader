package com.dbeqiraj.youtubedownloader.mvp.presenter;

import com.dbeqiraj.youtubedownloader.api.ButtonsApiService;
import com.dbeqiraj.youtubedownloader.base.BasePresenter;
import com.dbeqiraj.youtubedownloader.mvp.view.DownloadView;

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

public class ButtonsPresenter extends BasePresenter<DownloadView> implements Observer<ResponseBody> {

    @Inject
    protected ButtonsApiService mApiService;

    @Inject
    public ButtonsPresenter() {
    }

    public void getVideo(String vidID) {
        getView().onStartVidInfDownload();
        Observable<ResponseBody> responseBodyObservable = mApiService.getVideo(vidID);
        subscribe(responseBodyObservable, this);
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            String html = responseBody.string();
            getView().onVideoInfoDownloaded(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
