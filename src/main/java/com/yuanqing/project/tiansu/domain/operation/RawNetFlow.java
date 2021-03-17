package com.yuanqing.project.tiansu.domain.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

public class RawNetFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 源IP */
    @Excel(name = "源IP")
    private Long srcIp;

    /** 源端口 */
    @Excel(name = "源端口")
    private String srcPort;

    /** 源MAC */
    @Excel(name = "源MAC")
    private String srcMac;

    /** 目的IP */
    @Excel(name = "目的IP")
    private Long dstIp;

    /** 目的端口 */
    @Excel(name = "目的端口")
    private String dstPort;

    /** 目的MAC */
    @Excel(name = "目的MAC")
    private String dstMac;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", locale = "zh", timezone = "GMT+8")
//    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime stamp;

    /** 包大小 */
    @Excel(name = "包大小")
    private Long packetSize;

    /** 包类型 */
    @Excel(name = "包类型")
    private String packetType;

    /** 包数量 */
    @Excel(name = "包数量")
    private Long packetCount;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setSrcIp(Long srcIp)
    {
        this.srcIp = srcIp;
    }

    public Long getSrcIp()
    {
        return srcIp;
    }
    public void setSrcPort(String srcPort)
    {
        this.srcPort = srcPort;
    }

    public String getSrcPort()
    {
        return srcPort;
    }
    public void setSrcMac(String srcMac)
    {
        this.srcMac = srcMac;
    }

    public String getSrcMac()
    {
        return srcMac;
    }
    public void setDstIp(Long dstIp)
    {
        this.dstIp = dstIp;
    }

    public Long getDstIp()
    {
        return dstIp;
    }
    public void setDstPort(String dstPort)
    {
        this.dstPort = dstPort;
    }

    public String getDstPort()
    {
        return dstPort;
    }
    public void setDstMac(String dstMac)
    {
        this.dstMac = dstMac;
    }

    public String getDstMac()
    {
        return dstMac;
    }
    public void setStamp(LocalDateTime stamp)
    {
        this.stamp = stamp;
    }

    public LocalDateTime getStamp()
    {
        return stamp;
    }
    public void setPacketSize(Long packetSize)
    {
        this.packetSize = packetSize;
    }

    public Long getPacketSize()
    {
        return packetSize;
    }
    public void setPacketType(String packetType)
    {
        this.packetType = packetType;
    }

    public String getPacketType()
    {
        return packetType;
    }
    public void setPacketCount(Long packetCount)
    {
        this.packetCount = packetCount;
    }

    public Long getPacketCount()
    {
        return packetCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("srcIp", getSrcIp())
                .append("srcPort", getSrcPort())
                .append("srcMac", getSrcMac())
                .append("dstIp", getDstIp())
                .append("dstPort", getDstPort())
                .append("dstMac", getDstMac())
                .append("stamp", getStamp())
                .append("packetSize", getPacketSize())
                .append("packetType", getPacketType())
                .append("packetCount", getPacketCount())
                .toString();
    }
}
