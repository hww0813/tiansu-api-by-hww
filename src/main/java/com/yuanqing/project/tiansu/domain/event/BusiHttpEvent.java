package com.yuanqing.project.tiansu.domain.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;


/**
 * 接口告警对象 busi_http_event
 *
 * @author lvjingjing
 * @date 2021-05-13
 */
public class BusiHttpEvent {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 访问地址
     */
    @Excel(name = "访问地址")
    private String httpUrl;

    /**
     * 终端IP
     */
    @Excel(name = "终端IP")
    private Long ipAddress;

    /**
     * 事件来源
     */
    @Excel(name = "事件来源")
    private String eventSource;

    /**
     * 接口执行状态
     */
    @Excel(name = "接口执行状态")
    private String httpStatus;

    /**
     * 告警等级
     */
    @Excel(name = "告警等级")
    private String eventLevel;

    /**
     * 告警状态
     */
    @Excel(name = "告警状态")
    private Integer eventStatus;

    /**
     * 响应时长(ms)
     */
    @Excel(name = "响应时长(ms)")
    private String httpResponseTime;

    /**
     * 接口数据时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "接口数据时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date httpStamp;

    /**
     * 告警事件生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "告警事件生成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "告警事件更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateTime;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setIpAddress(Long ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getIpAddress() {
        return ipAddress;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Integer getEventStatus() {
        return eventStatus;
    }

    public void setHttpResponseTime(String httpResponseTime) {
        this.httpResponseTime = httpResponseTime;
    }

    public String getHttpResponseTime() {
        return httpResponseTime;
    }

    public void setHttpStamp(Date httpStamp) {
        this.httpStamp = httpStamp;
    }

    public Date getHttpStamp() {
        return httpStamp;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("httpUrl", getHttpUrl())
                .append("ipAddress", getIpAddress())
                .append("eventSource", getEventSource())
                .append("httpStatus", getHttpStatus())
                .append("eventLevel", getEventLevel())
                .append("eventStatus", getEventStatus())
                .append("httpResponseTime", getHttpResponseTime())
                .append("httpStamp", getHttpStamp())
                .append("startTime", getStartTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
