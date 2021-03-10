package com.yuanqing.project.tiansu.domain.analysis;

import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-08 15:44
 */
@Data
public class CameraVisit extends BaseEntity {

    private String deviceCode;

    private Long ipAddress;

    private String deviceName;

    private String regionName;

    private Integer action;

    private Long visitCnt;

    private Long terminalCnt;

}
