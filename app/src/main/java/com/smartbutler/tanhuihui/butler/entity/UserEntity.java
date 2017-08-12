package com.smartbutler.tanhuihui.butler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.entity
 * 创建日期： 2017/6/10
 * 描  述：
 */

public class UserEntity extends BmobUser {
    private String birth;
    private Boolean man;

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Boolean getMan() {
        return man;
    }

    public void setMan(Boolean man) {
        this.man = man;
    }
}
