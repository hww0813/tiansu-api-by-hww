package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

/**
 * 外部设备表实体(废弃类)
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

    /**
     * 别名
     */
    private String deviceAlias;


}


