package com.smartbutler.tanhuihui.butler.entity;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.entity
 * 创建日期： 2017/6/18
 * 描  述：   回答类
 */

public class ChatEntity {

    //类型，文本在左边还是右边
    private int type;
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
