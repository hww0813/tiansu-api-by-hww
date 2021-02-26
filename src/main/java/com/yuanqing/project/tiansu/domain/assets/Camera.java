package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

import java.io.Serializable;

/**
 * Camera扩展对象
 * Created by xucan on 2021-01-15 15:35
 *
 * @author xucan
 */

public class Camera extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * IP地址
     */
    @Excel(name = "IP地址")
    private Long ipAddress;

    /**
     * 厂商
     */
    @Excel(name = "设备厂商")
    private String manufacturer;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 来源
     */
    private Long source;

    /**
     * 编号
     */
    @Excel(name = "设备编码")
    private String deviceCode;

    /**
     * 域
     */
    private String deviceDomain;

    /**
     * MAC地址
     */
    private String macAddress;

    /**
     * 城市
     */
    private Integer region;

    /**
     * 设备状态
     */
    @Excel(name = "设备状态")
    private Integer status;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 域IP
     */
    private Long domainIp;

    /**
     * 域端口
     */
    private Long domainPort;

    /**
     * 信令服务器ID
     */
    private Long sipServerId;

    /**
     * 审计 0或1
     */
    private String isProbe;

    /**
     * 是否导入 0或1
     */
    private String isImport;

    /**
     * 是否确认不是服务器 0或1
     */
    private Long isCheck;

    /**
     * 是否国标
     */
    private Integer isGb;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Long getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(Long ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceDomain() {
        return deviceDomain;
    }

    public void setDeviceDomain(String deviceDomain) {
        this.deviceDomain = deviceDomain;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Long getDomainIp() {
        return domainIp;
    }

    public void setDomainIp(Long domainIp) {
        this.domainIp = domainIp;
    }

    public Long getDomainPort() {
        return domainPort;
    }

    public void setDomainPort(Long domainPort) {
        this.domainPort = domainPort;
    }

    public Long getSipServerId() {
        return sipServerId;
    }

    public void setSipServerId(Long sipServerId) {
        this.sipServerId = sipServerId;
    }

    public String getIsProbe() {
        return isProbe;
    }

    public void setIsProbe(String isProbe) {
        this.isProbe = isProbe;
    }

    public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }

    public Long getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Long isCheck) {
        this.isCheck = isCheck;
    }

    public Integer getIsGb() {
        return isGb;
    }

    public void setIsGb(Integer isGb) {
        this.isGb = isGb;
    }
}
