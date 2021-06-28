package com.yuanqing.project.tiansu.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Event implements Serializable {


    private Long id;

    /**
     * 事件类别
     */
    private String eventCategory;

    /**
     * 报警级别
     */
    private String eventLevel;

    /**
     * 事件来源
     */
    private String eventSource;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime endTime;

    /**
     * 生成告警事件时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime stamp;

    /**
     * 事件内容
     */
    private String content;

    /**
     * 事件状态
     */
    private Long status;

    /**
     * 客户端id
     */
    private Long clientId;

    /**
     * 摄像头id
     */
    private Long cameraId;

    /**
     * 操作行为的uuid
     */
    private String operUuid;

    /**
     * 客户端ip
     */
    private Long clientIp;

    /**
     * 用户名
     */
    private String username;

    /**
     * 摄像头名字
     */
    private String cameraName;

    /**
     * 动作
     */
    private Long action;

    /**
     * 连接类型
     */
    private Long connectType;

    //排序
    private String orderType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getOperUuid() {
        return operUuid;
    }

    public void setOperUuid(String operUuid) {
        this.operUuid = operUuid;
    }

    public Long getClientIp() {
        return clientIp;
    }

    public void setClientIp(Long clientIp) {
        this.clientIp = clientIp;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Long getAction() {
        return action;
    }

    public void setAction(Long action) {
        this.action = action;
    }

    public Long getConnectType() {
        return connectType;
    }

    public void setConnectType(Long connectType) {
        this.connectType = connectType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getStamp() {
        return stamp;
    }

    public void setStamp(LocalDateTime stamp) {
        this.stamp = stamp;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", eventCategory='" + eventCategory + '\'' +
                ", eventLevel='" + eventLevel + '\'' +
                ", eventSource='" + eventSource + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", stamp=" + stamp +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", clientId=" + clientId +
                ", cameraId=" + cameraId +
                ", operUuid='" + operUuid + '\'' +
                ", clientIp=" + clientIp +
                ", username='" + username + '\'' +
                ", cameraName='" + cameraName + '\'' +
                ", action=" + action +
                ", connectType=" + connectType +
                '}';
    }
}
