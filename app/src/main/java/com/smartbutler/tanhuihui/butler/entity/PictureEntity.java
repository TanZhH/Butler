package com.smartbutler.tanhuihui.butler.entity;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.entity
 * 创建日期： 2017/7/1
 * 描  述：
 */

public class PictureEntity {
    private String imageURL;

    public PictureEntity(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
