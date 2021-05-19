package com.yuanqing.project.tiansu.domain.event;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 接口告警阈值配置对象 macs_http_config
 *
 * @author lvjingjing
 * @date 2021-05-12
 */
public class MacsHttpConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 阈值名称 */
    @Excel(name = "阈值名称")
    private String configname;

    /** 阈值值 */
    @Excel(name = "阈值值")
    private String configvalue;

    /** 告警等级 */
    @Excel(name = "告警等级")
    private String eventLevel;

    /** 是否开启该告警(0:关闭;1:开启) */
    @Excel(name = "是否开启该告警(0:关闭;1:开启)")
    private Integer onWork;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setConfigname(String configname)
    {
        this.configname = configname;
    }

    public String getConfigname()
    {
        return configname;
    }
    public void setConfigvalue(String configvalue)
    {
        this.configvalue = configvalue;
    }

    public String getConfigvalue()
    {
        return configvalue;
    }
    public void setEventLevel(String eventLevel)
    {
        this.eventLevel = eventLevel;
    }

    public String getEventLevel()
    {
        return eventLevel;
    }
    public void setOnWork(Integer onWork)
    {
        this.onWork = onWork;
    }

    public Integer getOnWork()
    {
        return onWork;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("configname", getConfigname())
                .append("configvalue", getConfigvalue())
                .append("eventLevel", getEventLevel())
                .append("updateTime", getUpdateTime())
                .append("onWork", getOnWork())
                .toString();
    }
}
