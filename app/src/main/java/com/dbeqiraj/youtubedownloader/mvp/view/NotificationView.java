package com.dbeqiraj.youtubedownloader.mvp.view;

import okhttp3.ResponseBody;

/**
 * Created by d.beqiraj on 4/13/2018.
 */

public interface NotificationView extends BaseView{

    void onVideoStreamDownloaded(ResponseBody responseBody);

    void onFinishDownload(Boolean success);
}
