package com.yuanqing.project.tiansu.domain.assets.base;

import com.yuanqing.common.enums.DeviceStatus;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * Created by xucan on 2021-01-19 10:43
 * @author xucan
 */

@Data
public class BaseClient extends BaseEntity {

    /** 主键ID */
    private Long id;

    /** 设备编号 */
    private String deviceCode;

    /** IP地址 */
    private Long ipAddress;

    /** 设备名称 */
    private String deviceName;

    /** MAC地址 */
    private String macAddress;

    /** 设备状态 */
    private DeviceStatus status;

    /** 设备名称 */
    private Integer regionId;

    /** 设备类型 */
    private String deviceType;

    /** 域IP */
    private Long domainIp;

    /** 域端口 */
    private Long domainPort;

    private Long sipServerId;

    private Long mediaServerId;

    private Long userProxyServerId;
}
