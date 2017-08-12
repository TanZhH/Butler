package com.smartbutler.tanhuihui.butler.entity;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.entity
 * 创建日期： 2017/6/17
 * 描  述：   订单类
 */

public class CourierEntity {

    //物流事件发生的时间
    private String datetime;
    //物流事件的描述
    private String remark;

    public CourierEntity() {
    }

    public CourierEntity(String datetime, String remark) {
        this.datetime = datetime;
        this.remark = remark;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
