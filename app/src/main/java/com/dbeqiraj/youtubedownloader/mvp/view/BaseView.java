package com.dbeqiraj.youtubedownloader.mvp.view;

/**
 * Created by d.beqiraj on 3/26/2018.
 */

public interface BaseView {

    void onDismissNotification();

    void onShowConnectionError();

    void onGeneralError();

    void onShowToast(String message);
}
