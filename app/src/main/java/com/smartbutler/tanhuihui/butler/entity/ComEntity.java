package com.smartbutler.tanhuihui.butler.entity;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.entity
 * 创建日期： 2017/6/16
 * 描  述：   快递公司类
 */

public class ComEntity {
    //快递公司名
    private String name;
    //代号
    private String code;

    public ComEntity() {
    }

    public ComEntity(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
