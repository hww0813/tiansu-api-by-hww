package com.yuanqing.project.tiansu.domain.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.common.enums.SelfAuditSeverityEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TiansuLoginLog implements Serializable {

    private Long id;
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime stime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime etime;
    private String url;
    private String name;
    private String method;
    private String args;
    private String type;
    private String ipAddress;
    private Boolean result;
    private SelfAuditSeverityEnum severity;
    private String describe;
    private String modular;

    public TiansuLoginLog() {
    }

    public TiansuLoginLog(String username, String url, String name, String method, String args, String type, String ipAddress, Boolean result, SelfAuditSeverityEnum severity, String describe, String modular) {
        this.username = username;
        this.stime = LocalDateTime.now();
        this.url = url;
        this.name = name;
        this.method = method;
        this.args = args;
        this.type = type;
        this.ipAddress = ipAddress;
        this.result = result;
        this.severity = severity;
        this.describe = describe;
        this.modular = modular;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getStime() {
        return stime;
    }

    public void setStime(LocalDateTime stime) {
        this.stime = stime;
    }

    public LocalDateTime getEtime() {
        return etime;
    }

    public void setEtime(LocalDateTime etime) {
        this.etime = etime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public SelfAuditSeverityEnum getSeverity() {
        return severity;
    }

    public void setSeverity(SelfAuditSeverityEnum severity) {
        this.severity = severity;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getModular() {
        return modular;
    }

    public void setModular(String modular) {
        this.modular = modular;
    }

    @Override
    public String toString() {
        return "SelfAudit{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", method='" + method + '\'' +
                ", stime=" + stime +
                ", etime=" + etime +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", args='" + args + '\'' +
                ", type='" + type + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", result=" + result +
                ", severity=" + severity +
                ", describe=" + describe +
                ", modular=" + modular +
                '}';
    }
}
