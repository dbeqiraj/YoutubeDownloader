package com.dbeqiraj.youtubedownloader.mvp.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by d.beqiraj on 5/19/2018.
 */

public class Video implements Serializable {

    private Boolean error;
    private String title;
    private Long duration;
    private String file;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
