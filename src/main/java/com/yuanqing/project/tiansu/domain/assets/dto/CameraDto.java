package com.yuanqing.project.tiansu.domain.assets.dto;

import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

/**
 * 摄像头DTO对象
 * Created by xucan on 2021-01-18 10:39
 * @author xucan
 */

@Data
public class CameraDto extends BaseCamera {

    /** 编号 */
    private String deviceCode;

    /** 域 */
    private String deviceDomain;

    /** MAC地址 */
    private String macAddress;

    /** 区域代码 */
    private Integer regionId;

    /** 区域名称 */
    private String regionName;

    /** 设备状态 */
    private Integer status;

    /** 设备类型 */
    private String deviceType;

    /** 域IP */
    private Long domainIp;

    /** 域端口 */
    private Long domainPort;

    /** 信令服务器ID */
    private Long sipServerId;

}
