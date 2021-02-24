package com.yuanqing.project.tiansu.domain.assets;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.project.tiansu.domain.assets.base.BaseClient;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端
 *
 * @author jqchu
 * @version 1.0
 **/
public class SessionClient extends BaseClient {
    //登录账号
    private String username;

    private int count;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }




}
