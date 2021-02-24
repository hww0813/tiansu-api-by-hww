package com.yuanqing.project.tiansu.domain.assets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;

/**
 * 外部设备表对象 busi_external_device
 *
 * @author cq
 * @date 2021-02-24
 */
public class BusiExternalDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 组织ID */
    @Excel(name = "组织ID")
    private String domainId;

    /** 国标ID */
    @Excel(name = "国标ID")
    private String gbId;

    /** 设备IP */
    @Excel(name = "设备IP")
    private String ipAddress;

    /** 设备厂商 */
    @Excel(name = "设备厂商")
    private String manufacturer;

    /** 经度 */
    @Excel(name = "经度")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 别名 */
    @Excel(name = "别名")
    private String deviceAlias;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName()
    {
        return deviceName;
    }
    public void setDomainId(String domainId)
    {
        this.domainId = domainId;
    }

    public String getDomainId()
    {
        return domainId;
    }
    public void setGbId(String gbId)
    {
        this.gbId = gbId;
    }

    public String getGbId()
    {
        return gbId;
    }
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }
    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLongitude()
    {
        return longitude;
    }
    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
    public void setDeviceAlias(String deviceAlias)
    {
        this.deviceAlias = deviceAlias;
    }

    public String getDeviceAlias()
    {
        return deviceAlias;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("deviceName", getDeviceName())
            .append("domainId", getDomainId())
            .append("gbId", getGbId())
            .append("ipAddress", getIpAddress())
            .append("manufacturer", getManufacturer())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("deviceAlias", getDeviceAlias())
            .toString();
    }
}
