package com.yuanqing.project.tiansu.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.common.enums.DeviceStatus;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created by xucan on 2021-01-18 16:13
 * @author xucan
 */

@Data
public class ClientTerminal extends BaseEntity {

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
