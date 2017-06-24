package com.yibaitaoke.x.model;

import java.io.Serializable;

/**
 * Created by x on 2017/6/24.
 */

public class UserModel implements Serializable {


    private String openId;
    private String pid;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPid() {
        pid = pid == null ? "" : pid;
        return pid;
    }

    public void setPid(String pid) {

        this.pid = pid;
    }


}
