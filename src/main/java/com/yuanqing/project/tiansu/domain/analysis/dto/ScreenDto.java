package com.yuanqing.project.tiansu.domain.analysis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-02 16:36
 */
@ApiModel("大屏对象")
@Data
public class ScreenDto {

    @ApiModelProperty("周期")
    private Integer dateType;

    @ApiModelProperty("地区编码")
    private String areaCode;
}
