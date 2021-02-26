package com.yuanqing.project.tiansu.domain.assets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;

/**
 * 摄像头对象 busi_camera
 *
 * @author cq
 * @date 2021-02-25
 */
public class BusiCamera extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceCode;

    /** 设备域 */
    @Excel(name = "设备域")
    private String deviceDomain;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** MAC地址 */
    @Excel(name = "MAC地址")
    private String macAddress;

    /** 经度 */
    @Excel(name = "经度")
    private Double longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double latitude;

    /** 行政区域 */
    @Excel(name = "行政区域")
    private Long region;

    /** IP地址 */
    @Excel(name = "IP地址")
    private Long ipAddress;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 信令服务器ID */
    @Excel(name = "信令服务器ID")
    private Long sipServerId;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 域IP */
    @Excel(name = "域IP")
    private Long domainIp;

    /** 域端口 */
    @Excel(name = "域端口")
    private Long domainPort;

    /** 是否国标 */
    @Excel(name = "是否国标")
    private Integer isGb;

    /** 制造商 */
    @Excel(name = "制造商")
    private String manufacturer;

    /** 审计 */
    @Excel(name = "审计")
    private String isProbe;

    /** 导入 */
    @Excel(name = "导入")
    private String isImport;

    /** 是否确认不是服务器 */
    @Excel(name = "是否确认不是服务器")
    private Integer isCheck;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeviceCode(String deviceCode)
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode()
    {
        return deviceCode;
    }
    public void setDeviceDomain(String deviceDomain)
    {
        this.deviceDomain = deviceDomain;
    }

    public String getDeviceDomain()
    {
        return deviceDomain;
    }
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }

    public String getMacAddress()
    {
        return macAddress;
    }
    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }
    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }
    public void setRegion(Long region)
    {
        this.region = region;
    }

    public Long getRegion()
    {
        return region;
    }
    public void setIpAddress(Long ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public Long getIpAddress()
    {
        return ipAddress;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setSipServerId(Long sipServerId)
    {
        this.sipServerId = sipServerId;
    }

    public Long getSipServerId()
    {
        return sipServerId;
    }
    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType()
    {
        return deviceType;
    }
    public void setDomainIp(Long domainIp)
    {
        this.domainIp = domainIp;
    }

    public Long getDomainIp()
    {
        return domainIp;
    }
    public void setDomainPort(Long domainPort)
    {
        this.domainPort = domainPort;
    }

    public Long getDomainPort()
    {
        return domainPort;
    }
    public void setIsGb(Integer isGb)
    {
        this.isGb = isGb;
    }

    public Integer getIsGb()
    {
        return isGb;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setIsProbe(String isProbe)
    {
        this.isProbe = isProbe;
    }

    public String getIsProbe()
    {
        return isProbe;
    }
    public void setIsImport(String isImport)
    {
        this.isImport = isImport;
    }

    public String getIsImport()
    {
        return isImport;
    }
    public void setIsCheck(Integer isCheck)
    {
        this.isCheck = isCheck;
    }

    public Integer getIsCheck()
    {
        return isCheck;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceCode", getDeviceCode())
            .append("deviceDomain", getDeviceDomain())
            .append("deviceName", getDeviceName())
            .append("macAddress", getMacAddress())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("region", getRegion())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("ipAddress", getIpAddress())
            .append("status", getStatus())
            .append("sipServerId", getSipServerId())
            .append("deviceType", getDeviceType())
            .append("domainIp", getDomainIp())
            .append("domainPort", getDomainPort())
            .append("isGb", getIsGb())
            .append("manufacturer", getManufacturer())
            .append("isProbe", getIsProbe())
            .append("isImport", getIsImport())
            .append("isCheck", getIsCheck())
            .toString();
    }
}
