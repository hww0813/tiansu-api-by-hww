package com.yuanqing.project.tiansu.domain.assets;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;

/**
 * 服务标注对象 busi_server_remark
 *
 * @author xucan
 * @date 2021-05-18
 */

@ApiModel("服务标注对象")
public class BusiServerRemark extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @ApiModelProperty("id")
    private Long id;

    /** 服务名称 */
    @Excel(name = "服务名称")
    @ApiModelProperty("服务名称")
    private String serverName;

    /** 服务IP */
    @Excel(name = "服务IP")
    @ApiModelProperty("服务IP")
    private Long serverIp;

    /** 服务端口 */
    @Excel(name = "服务端口")
    @ApiModelProperty("服务端口")
    private Long serverPort;



    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setServerIp(Long serverIp)
    {
        this.serverIp = serverIp;
    }

    public Long getServerIp()
    {
        return serverIp;
    }
    public void setServerPort(Long serverPort)
    {
        this.serverPort = serverPort;
    }

    public Long getServerPort()
    {
        return serverPort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("serverIp", getServerIp())
            .append("serverPort", getServerPort())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
