package com.smartbutler.tanhuihui.butler.tinker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.tinker
 * 创建日期： 2017/10/7
 * 描  述：
 */

public class PathInfo implements Parcelable {
    private String downloadUrl; //不为空则表明有更新

    private String versionName; //本次patch包的版本号

    private String patchMessage; //本次patch包含的相关信息，例如：主要做了那些改动

    protected PathInfo(Parcel in) {
        downloadUrl = in.readString();
        versionName = in.readString();
        patchMessage = in.readString();
    }

    public static final Creator<PathInfo> CREATOR = new Creator<PathInfo>() {
        @Override
        public PathInfo createFromParcel(Parcel in) {
            return new PathInfo(in);
        }

        @Override
        public PathInfo[] newArray(int size) {
            return new PathInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(downloadUrl);
        dest.writeString(versionName);
        dest.writeString(patchMessage);
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPatchMessage() {
        return patchMessage;
    }

    public void setPatchMessage(String patchMessage) {
        this.patchMessage = patchMessage;
    }
}
