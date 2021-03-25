package com.yuanqing.project.tiansu.domain.assets;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

import java.io.Serializable;

/**
 * Camera扩展对象
 * Created by xucan on 2021-01-15 15:35
 *
 * @author xucan
 */

@Data
public class Camera extends BaseCamera {

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
     * 区域代码
     */
    private Integer region;

    /**
     * 区域名称
     */
    private String regionName;

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

}
