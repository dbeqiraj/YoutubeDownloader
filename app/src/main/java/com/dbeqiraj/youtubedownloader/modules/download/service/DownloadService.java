package com.dbeqiraj.youtubedownloader.modules.download.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.dbeqiraj.youtubedownloader.R;
import com.dbeqiraj.youtubedownloader.application.App;
import com.dbeqiraj.youtubedownloader.di.components.DaggerDownloadServiceComponent;
import com.dbeqiraj.youtubedownloader.di.module.DownloadServiceModule;
import com.dbeqiraj.youtubedownloader.mvp.model.Download;
import com.dbeqiraj.youtubedownloader.mvp.presenter.DownloadPresenter;
import com.dbeqiraj.youtubedownloader.mvp.view.NotificationView;
import com.dbeqiraj.youtubedownloader.utilities.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by d.beqiraj on 4/12/2018.
 */

public class DownloadService extends IntentService implements NotificationView {

    @Inject
    protected DownloadPresenter downloadPresenter;

    private String url;
    private String title;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private double totalFileSize;

    public DownloadService() {
        super("Download Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerDownloadServiceComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .downloadServiceModule(new DownloadServiceModule( this))
                .build().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        notificationBuilder = new NotificationCompat.Builder(this, "")
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(title)
                .setContentText(getString(R.string.download_started))
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload(url);

    }

    private void initDownload(String url){

        downloadPresenter.download(url);
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getApplicationContext().getString(R.string.app_name);

            File root = new File(rootDir );
            if ( !(root.exists()) ) {
                root.mkdir();
            }

            if ( root.exists() ) {
                File file = new File(rootDir + File.separator + title + ".mp3");

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    byte[] buffer = new byte[1024 * 24];

                    inputStream = new BufferedInputStream(body.byteStream(), 1024 * 32);
                    outputStream = new FileOutputStream(file);

                    long fileSize = body.contentLength();
                    long total = 0;
                    long startTime = System.currentTimeMillis();
                    int timeCount = 1;
                    int bufferLength = 0;
                    while ( (bufferLength = inputStream.read(buffer)) > 0 ) {


                        total += bufferLength;
                        totalFileSize = (fileSize / (Math.pow(1024, 2)));
                        double current = total / (Math.pow(1024, 2));

                        int progress = (int) ((total * 100) / fileSize);

                        long currentTime = System.currentTimeMillis() - startTime;

                        Download download = new Download();
                        download.setTotalFileSize(totalFileSize);

                        if (currentTime > 1000 * timeCount) {

                            download.setCurrentFileSize(current);
                            download.setProgress(progress);
                            sendNotification(download);
                            timeCount++;
                        }

                        outputStream.write(buffer, 0, bufferLength);
                    }

                    outputStream.flush();

                    // Music file
                    Utils.isMusic(getApplicationContext(), file, title);

                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }

                    onDownloadComplete();
                }
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendNotification(Download download){

        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText(String.format(getString(R.string.downloading_file), download.getCurrentFileSize(), totalFileSize));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void onDownloadComplete(){

        Download download = new Download();
        download.setProgress(100);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText(getString(R.string.file_saved));
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancelAll();
    }


    @Override
    public void onDismissNotification() {
        notificationManager.cancelAll();
    }

    @Override
    public void onShowConnectionError() {
        onShowToast(getString(R.string.no_connection));
    }

    @Override
    public void onGeneralError() {
        onShowToast(getString(R.string.could_not_proceed));
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVideoStreamDownloaded(final ResponseBody responseBody) {
        Observable.defer(new Callable<ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> call() throws Exception {
                return Observable.just(writeResponseBodyToDisk(responseBody));
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Boolean success) {
                        onFinishDownload(success);
                    }
                    @Override
                    public void onError(Throwable e) {
                        onDismissNotification();
                        onShowToast(getString(R.string.file_not_saved));
                    }
                    @Override
                    public void onComplete() {
                        onDismissNotification();
                    }
                });
    }

    @Override
    public void onFinishDownload(Boolean success) {
        if ( success ) {
            onShowToast(getString(R.string.file_saved));
        } else {
            onShowToast(getString(R.string.file_not_saved));
        }
    }
}
