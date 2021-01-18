package com.yuanqing.project.tiansu.domain;

import com.yuanqing.project.tiansu.domain.base.BaseCamera;
import lombok.Data;

/**
 * 外部设备表实体
 * Created by xucan on 2021-01-18 10:57
 * @author xucan
 */

@Data
public class ExternalDevice extends BaseCamera {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 组织ID
     */
    private String domainId;

    /**
     * 国标ID
     */
    private String gbId;

}


