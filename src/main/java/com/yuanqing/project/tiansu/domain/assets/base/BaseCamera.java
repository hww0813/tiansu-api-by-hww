package com.yuanqing.project.tiansu.domain.assets.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 摄像头基础对象
 * Created by xucan on 2021-01-15 15:29
 *
 * @author xucan
 */

@ApiModel("摄像头基础对象")
@Data
public class BaseCamera extends BaseEntity {

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    @Excel(name = "IP地址")
    private Long ipAddress;

    /**
     * 厂商
     */
    @ApiModelProperty("厂商")
    @Excel(name = "设备厂商")
    private String manufacturer;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double latitude;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private Long source;

}
