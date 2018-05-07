package com.dbeqiraj.youtubedownloader.mvp.view;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

public interface DownloadView extends BaseView {

    void onVideoInfoDownloaded(String html);

    void onStartVidInfDownload();

}