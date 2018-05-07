package com.dbeqiraj.youtubedownloader.modules.download;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.dbeqiraj.youtubedownloader.R;
import com.dbeqiraj.youtubedownloader.base.BaseActivity;
import com.dbeqiraj.youtubedownloader.di.components.DaggerButtonsComponent;
import com.dbeqiraj.youtubedownloader.di.module.ButtonsModule;
import com.dbeqiraj.youtubedownloader.modules.download.service.DownloadService;
import com.dbeqiraj.youtubedownloader.mvp.presenter.ButtonsPresenter;
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
    @BindView(R.id.response) protected WebView response;

    @Inject
    protected ButtonsPresenter buttonsPresenter;

    private NotificationManager notificationManager;

    @Override
    protected int getContentView() {
        return R.layout.activity_download;
    }

    @Override
    protected void resolveDaggerDependency() {
        DaggerButtonsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .buttonsModule(new ButtonsModule(this))
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

        prepareResponse();

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

    @SuppressLint("SetJavaScriptEnabled")
    private void prepareResponse() {
        response.setWebChromeClient(new WebChromeClient());
        response.setWebViewClient(new WebViewClient());
        response.getSettings().setJavaScriptEnabled(true);
        response.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                String title = contentDisposition.substring(contentDisposition.indexOf("\"") + 1, contentDisposition.lastIndexOf("\""));
                title = title.replaceAll(" - \\[youtube-mp3.info\\].mp3", "");
                title = title
                        .replaceAll("&quot;", "_")
                        .replaceAll("&#039;", "'")
                        .replaceAll("/", "_")
                        .replaceAll("\\\\", "_")
                        .replaceAll("\"", "_")
                        .replaceAll("&amp;", "&");


                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                startService(intent);
                finish();
                System.exit(0);
            }
        });
    }

    private void getVideo() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String link = extras.getString(Intent.EXTRA_TEXT);
            if (link != null) {
                int index = link.lastIndexOf("/") + 1;
                if ( index > -1 ) {
                    String id = link.substring(index);
                    buttonsPresenter.getVideo(id);
                } else {
                    onShowToast(getString(R.string.invalid_link));
                }
            } else {
                onShowToast(getString(R.string.invalid_link));
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onVideoInfoDownloaded(String html) {
        if ( html.contains("invalid") ) {
            onDismissNotification();
            getVideo();
        } else {
            int index = html.indexOf("<a");
            html = html.substring(0, index) + " <a id = 'maxbitrate' " + html.substring(index + 2, html.length());
            html = html + "<script type='text/javascript'>location.href = document.getElementById('maxbitrate').href;</script>";
            response.loadData(html, "text/html; charset=utf-8", "UTF-8");
        }
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
