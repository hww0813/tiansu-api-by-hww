package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.assets.base.BaseCamera;
import lombok.Data;

/**
 * 摄像头历史对象 busi_camera_history
 *
 * @author cq
 * @date 2021-02-26
 */
@Data
public class BusiCameraHistory extends BaseCamera
{
    private static final long serialVersionUID = 1L;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceCode;
}
