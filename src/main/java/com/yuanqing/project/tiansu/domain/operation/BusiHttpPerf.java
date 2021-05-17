package com.yuanqing.project.tiansu.domain.operation;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;

/**
 * http接口审计对象 busi_http_perf
 *
 * @author xucan
 * @date 2021-05-17
 */
public class BusiHttpPerf extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 源ip */
    @Excel(name = "源ip")
    private Long srcIp;

    /** 源端口 */
    @Excel(name = "源端口")
    private String srcPort;

    /** 响应时间 */
    @Excel(name = "响应时间")
    private String httpResponseTime;

    /** 包大小 */
    @Excel(name = "包大小")
    private String dataSize;

    /** 包内容 */
    @Excel(name = "包内容")
    private String content;

    /** 目的ip */
    @Excel(name = "目的ip")
    private Long dstIp;

    /** 目的端口 */
    @Excel(name = "目的端口")
    private String dstPort;

    /** 访问地址 */
    @Excel(name = "访问地址")
    private String httpUrl;

    /** 响应状态 */
    @Excel(name = "响应状态")
    private String httpStatus;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date stamp;

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
    public void setHttpResponseTime(String httpResponseTime)
    {
        this.httpResponseTime = httpResponseTime;
    }

    public String getHttpResponseTime()
    {
        return httpResponseTime;
    }
    public void setDataSize(String dataSize)
    {
        this.dataSize = dataSize;
    }

    public String getDataSize()
    {
        return dataSize;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
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
    public void setHttpUrl(String httpUrl)
    {
        this.httpUrl = httpUrl;
    }

    public String getHttpUrl()
    {
        return httpUrl;
    }
    public void setHttpStatus(String httpStatus)
    {
        this.httpStatus = httpStatus;
    }

    public String getHttpStatus()
    {
        return httpStatus;
    }
    public void setStamp(Date stamp)
    {
        this.stamp = stamp;
    }

    public Date getStamp()
    {
        return stamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("srcIp", getSrcIp())
            .append("srcPort", getSrcPort())
            .append("httpResponseTime", getHttpResponseTime())
            .append("dataSize", getDataSize())
            .append("content", getContent())
            .append("dstIp", getDstIp())
            .append("dstPort", getDstPort())
            .append("httpUrl", getHttpUrl())
            .append("httpStatus", getHttpStatus())
            .append("updateTime", getUpdateTime())
            .append("stamp", getStamp())
            .toString();
    }
}
