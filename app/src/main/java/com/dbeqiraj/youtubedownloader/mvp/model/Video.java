package com.dbeqiraj.youtubedownloader.mvp.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public class Video implements Serializable {

    private HashMap<String, VidInfo> vidInfo;
    private String vidTitle;
    private String vidID;

    public HashMap<String, VidInfo> getVidInfo() {
        return vidInfo;
    }

    public void setVidInfo(HashMap<String, VidInfo> vidInfo) {
        this.vidInfo = vidInfo;
    }

    public String getVidTitle() {
        return vidTitle;
    }

    public void setVidTitle(String vidTitle) {
        this.vidTitle = vidTitle;
    }

    public String getVidID() {
        return vidID;
    }

    public void setVidID(String vidID) {
        this.vidID = vidID;
    }


}
