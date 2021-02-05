package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.common.enums.DeviceStatus;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.assets.base.BaseClientUser;
import lombok.Data;

/**
 * Created by xucan on 2021-01-19 17:50
 * @author xucan
 */

@Data
public class ClientUser extends BaseClientUser {

    /** IP地址 */
    private Long ipAddress;

}
