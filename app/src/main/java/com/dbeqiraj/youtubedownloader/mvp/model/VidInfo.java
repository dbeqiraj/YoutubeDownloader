package com.dbeqiraj.youtubedownloader.mvp.model;

import java.io.Serializable;

/**
 * Created by d.beqiraj on 3/25/2018.
 */

public class VidInfo implements Serializable {

    private String dloadUrl;
    private int bitrate;
    private String mp3size;

    public String getDloadUrl() {
        return dloadUrl;
    }

    public void setDloadUrl(String dloadUrl) {
        this.dloadUrl = dloadUrl;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public String getMp3size() {
        return mp3size;
    }

    public void setMp3size(String mp3size) {
        this.mp3size = mp3size;
    }
}
