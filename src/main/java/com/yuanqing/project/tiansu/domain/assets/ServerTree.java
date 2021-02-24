package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;


/**
 * Created by xucan on 2021-01-19 10:17
 * @author xucan
 */

@Data
public class ServerTree extends BaseEntity {

    /** 主键id */
    private Long id;

    /** 服务器编号 */
    private String serverCode;

    /** 服务器名称 */
    private String serverName;

    /** 服务器域名 */
    private String serverDomain;

    /** 服务器IP */
    private Long serverIp;

    /** 服务器端口 */
    private Long serverPort;

    /** 服务器MAC */
    private String serverMac;

    /** 服务器类型 */
    private String serverType;

    /** 是否开启远程审计 */
    private String isRangAudit;

    /** 远程审计端口 */
    private String rangAuditPort;

    /** 是否开启ssh审计 */
    private String isSshAudit;

    /** ssh审计端口 */
    private String sshAuditPort;

    /** 父级设备ID */
    private Long parentId;

    /** 是否删除 */
    private Short isDelete;
}
