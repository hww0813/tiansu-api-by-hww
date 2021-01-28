package com.yuanqing.project.tiansu.domain.assets.dto;

import com.yuanqing.common.enums.DeviceStatus;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * Created by xucan on 2021-01-19 17:50
 * @author xucan
 */

@Data
public class ClientUser extends BaseEntity {

    /** id */
    private Long id;

    /** 登录账号 */
    private String username;

    /** 设备状态 */
    private DeviceStatus status;
}
