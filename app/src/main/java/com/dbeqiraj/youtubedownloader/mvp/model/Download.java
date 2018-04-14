package com.dbeqiraj.youtubedownloader.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by d.beqiraj on 4/12/2018.
 */

public class Download implements Parcelable {

    public Download(){

    }

    private int progress;
    private double currentFileSize;
    private double totalFileSize;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(double currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public double getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(double totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(progress);
        dest.writeDouble(currentFileSize);
        dest.writeDouble(totalFileSize);
    }

    private Download(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readDouble();
        totalFileSize = in.readDouble();
    }

    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
