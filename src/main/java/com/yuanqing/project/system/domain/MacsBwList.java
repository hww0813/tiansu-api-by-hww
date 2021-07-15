package com.yuanqing.project.system.domain;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 黑白名单对象 macs_bw_list
 *
 * @author xucan
 * @date 2020-10-21
 */

@ApiModel("黑白名单对象")
public class MacsBwList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("ID")
    private Long id;

    /** 客户端ip范围 */
    @ApiModelProperty("客户端ip范围")
    @Excel(name = "客户端ip范围")
    private String ipRange;

    /** 备注 */
    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String description;

    /** IP全局设置黑/白名单/关闭,Black,White,Closed */
    @ApiModelProperty("IP全局 设置黑白名单")
    @Excel(name = "IP全局设置黑白名单")
    private String globalConfig;

    /** 非工作时访问白名单 0:true; 1:false */
    @ApiModelProperty("非工作时访问白名单")
    @Excel(name = "非工作时访问白名单")
    private Integer nonwork;

    /** 异常扫描白名单 0:true; 1:false */
    @ApiModelProperty("异常扫描白名单")
    @Excel(name = "异常扫描白名单")
    private Integer scan;

    /** 告警事件白名单 0:true; 1:false */
    @ApiModelProperty("异常扫描白名单")
    @Excel(name = "异常扫描白名单")
    private Integer rule;

    /** 跨区域访问 0:true; 1:false */
    @ApiModelProperty("跨区域访问")
    @Excel(name = "跨区域访问")
    private Integer crossOrigin;

    public Integer getCrossOrigin() {
        return crossOrigin;
    }

    public void setCrossOrigin(Integer crossOrigin) {
        this.crossOrigin = crossOrigin;
    }

    public Integer getRule() {
        return rule;
    }

    public void setRule(Integer rule) {
        this.rule = rule;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIpRange(String ipRange)
    {
        this.ipRange = ipRange;
    }

    public String getIpRange()
    {
        return ipRange;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setGlobalConfig(String globalConfig)
    {
        this.globalConfig = globalConfig;
    }

    public String getGlobalConfig()
    {
        return globalConfig;
    }
    public void setNonwork(Integer nonwork)
    {
        this.nonwork = nonwork;
    }

    public Integer getNonwork()
    {
        return nonwork;
    }
    public void setScan(Integer scan)
    {
        this.scan = scan;
    }

    public Integer getScan()
    {
        return scan;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("ipRange", getIpRange())
            .append("description", getDescription())
            .append("updateTime", getUpdateTime())
            .append("globalConfig", getGlobalConfig())
            .append("crossOrigin", getCrossOrigin())
            .append("nonwork", getNonwork())
            .append("scan", getScan())
            .append("rule", getRule())
            .toString();
    }
}
