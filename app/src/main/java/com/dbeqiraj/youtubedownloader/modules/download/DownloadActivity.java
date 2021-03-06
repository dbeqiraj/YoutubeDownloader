package com.dbeqiraj.youtubedownloader.modules.download;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class DownloadActivity extends BaseActivity implements DownloadView {

    private static final String NOTIF_CHANNEL_ID = "channel_download_activity";

    @BindView(R.id.loading)
    protected SimpleDraweeView loading;
    @BindView(R.id.downloading)
    protected TextView downloading;
    @BindView(R.id.warning)
    protected TextView warning;

    @Inject
    protected VideoPresenter videoPresenter;

    private NotificationManager notificationManager;

    private String title;

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

        setupViewComponents();
        checkPermissions();
    }

    private void setupViewComponents() {
        warning.setText(Html.fromHtml(getString(R.string.warning)));
        downloading.setTypeface(Utils.fontQuicksandBold(this));
        loading.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(ImageRequestBuilder.newBuilderWithResourceId(R.drawable.cube).build())
                .setAutoPlayAnimations(true)
                .build());
    }

    private void checkPermissions() {
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
            title = StringUtils.substringBetween(extras.getString(Intent.EXTRA_SUBJECT), "Watch \"", "\" on YouTube");
            String link = extras.getString(Intent.EXTRA_TEXT);
            if (link != null) {
                if (link.contains("https://youtu.be/"))
                    link = link.replace("https://youtu.be/", "https://www.youtube.com/watch?v=");
                videoPresenter.getVideo(link);
            } else {
                onShowToast(getString(R.string.invalid_link));
            }
        }
    }

    @Override
    public void onVideoInfoDownloaded(Video video) {
        title = title
                .replaceAll("&quot;", "_")
                .replaceAll("&#039;", "'")
                .replaceAll("/", "_")
                .replaceAll("\\\\", "_")
                .replaceAll("\"", "_")
                .replaceAll("&amp;", "&");

        video.setTitle(title);

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("video", video);
        startService(intent);

        finish();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
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

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "")
                    .setContent(contentView)
                    .setSmallIcon(R.mipmap.ic_file_download_white_24dp)
                    .setAutoCancel(true);

            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(NOTIF_CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
                notificationBuilder.setChannelId(NOTIF_CHANNEL_ID);
            }

            if (notificationManager != null) {
                Notification notification = notificationBuilder.build();
                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notificationManager.notify(0, notification);
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
