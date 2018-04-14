package com.dbeqiraj.youtubedownloader.mvp.view;

import com.dbeqiraj.youtubedownloader.mvp.model.Video;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

public interface DownloadView extends BaseView {

    void onVideoInfoDownloaded(Video video);

    void onStartVidInfDownload();

}