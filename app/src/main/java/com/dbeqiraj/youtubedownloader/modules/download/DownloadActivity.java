package com.dbeqiraj.youtubedownloader.modules.download;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.dbeqiraj.youtubedownloader.R;
import com.dbeqiraj.youtubedownloader.base.BaseActivity;
import com.dbeqiraj.youtubedownloader.di.components.DaggerVideoComponent;
import com.dbeqiraj.youtubedownloader.di.module.VideoModule;
import com.dbeqiraj.youtubedownloader.modules.download.service.DownloadService;
import com.dbeqiraj.youtubedownloader.mvp.model.Video;
import com.dbeqiraj.youtubedownloader.mvp.presenter.VideoPresenter;
import com.dbeqiraj.youtubedownloader.mvp.view.DownloadView;
import com.dbeqiraj.youtubedownloader.utilities.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class DownloadActivity extends BaseActivity implements DownloadView {

    @BindView(R.id.loading) protected SimpleDraweeView loading;
    @BindView(R.id.downloading) protected TextView downloading;

    @Inject
    protected VideoPresenter videoPresenter;

    private NotificationManager notificationManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_download;
    }

    @Override
    protected void resolveDaggerDependency() {
        DaggerVideoComponent.builder()
                .applicationComponent(getApplicationComponent())
                .videoModule(new VideoModule(this))
                .build().inject(this);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        downloading.setTypeface(Utils.fontQuicksandBold(this));
        loading.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithResourceId(R.drawable.cube).build())
                .setAutoPlayAnimations(true)
                .build());

        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!Utils.permissionIsGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && permissions.size() > 0) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
        } else {
            getVideo();
        }
    }

    private void getVideo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String link = extras.getString(Intent.EXTRA_TEXT);
            if (link != null) {
                int index = link.lastIndexOf("/") + 1;
                if ( index > -1 ) {
                    String id = link.substring(index);
                    videoPresenter.getVideo(id);
                } else {
                    onShowToast(getString(R.string.invalid_link));
                }
            } else {
                onShowToast(getString(R.string.invalid_link));
            }
        }
    }

    @Override
    public void onVideoInfoDownloaded(Video video) {
        video.setVidTitle(video.getVidTitle()
                .replaceAll("&quot;", "_")
                .replaceAll("&amp;", "&")
        );

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("video", video);
        startService(intent);

        finish();
        System.exit(0);
    }

    @Override
    public void onShowToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartVidInfDownload() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

            Notification notification = new Notification.Builder(this)
                    .setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_file_download_white_24dp)
                    .build();

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notification.flags |= Notification.FLAG_AUTO_CANCEL;

            if (notificationManager != null) {
                notificationManager.notify(0, notification);
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

        } else {
            onShowToast(getString(R.string.download_started));
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && ArrayUtils.contains(grantResults, PackageManager.PERMISSION_GRANTED)) {
            getVideo();
        } else {
            onShowToast(getString(R.string.permission_required));
        }
    }
}
