package com.yuanqing.project.tiansu.domain.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSearch
 * @Description 操作行为检索实体类
 * @Date 2021/1/29 10:14
 * @Version V1.0
 */
public class OperationBehaviorSearch {


    /** 页面搜索开始时间和结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date etime;


    private String dstCode; //目的设备编码

    private Long dstIp; //目的IP


    private Integer num;

    private Integer size;


    public String getDstCode() {
        return dstCode;
    }

    public void setDstCode(String dstCode) {
        this.dstCode = dstCode;
    }

    public Long getDstIp() {
        return dstIp;
    }

    public void setDstIp(Long dstIp) {
        this.dstIp = dstIp;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getStime() {
        return stime;
    }

    public void setStime(Date stime) {
        this.stime = stime;
    }

    public Date getEtime() {
        return etime;
    }

    public void setEtime(Date etime) {
        this.etime = etime;
    }
}
