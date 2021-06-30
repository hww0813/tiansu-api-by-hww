package com.yuanqing.project.tiansu.domain.event;

/**
 * @author lvjingjing
 * @date 2021/6/29 10:02
 */
public class BusiHttpEventDto extends BusiHttpEvent{
    /*服务名*/
    private String serverName;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
