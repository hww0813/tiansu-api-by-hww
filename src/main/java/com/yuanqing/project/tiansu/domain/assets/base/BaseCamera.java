package com.yuanqing.project.tiansu.domain.assets.base;

import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 摄像头基础对象
 * Created by xucan on 2021-01-15 15:29
 * @author xucan
 */

@Data
public class BaseCamera extends BaseEntity implements Serializable {

    /** 主键ID */
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** IP地址 */
    @Excel(name = "IP地址")
    private Long ipAddress;

    /** 厂商 */
    @Excel(name = "设备厂商")
    private String manufacturer;

    /** 经度 */
    private Double longitude;

    /** 纬度 */
    private Double latitude;

}
