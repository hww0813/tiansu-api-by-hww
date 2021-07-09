package com.yuanqing.project.tiansu.domain.macs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 探针列表对象 macs_probe
 *
 * @author xucan
 * @date 2020-10-15
 */
@ApiModel("探针列表对象")
@Data
public class MacsProbe extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("ID")
    private Long id;

    /** 名称 */
    @ApiModelProperty("名称")
    @Excel(name = "名称")
    private String name;

    /** IP地址 */
    @ApiModelProperty("IP地址")
    @Excel(name = "IP地址")
    private Long ipAddress;

    /** 状态 */
    @ApiModelProperty("状态")
    @Excel(name = "状态")
    private Integer status;

    /** 鉴权码 */
    @ApiModelProperty("系统码")
    @Excel(name = "系统码")
    private String code;

    /** uuid */
    @ApiModelProperty("uuid")
    @Excel(name = "uuid")
    private String uuid;

    /** 过期时间 */
    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireDay;

    /** 机器码 */
    @ApiModelProperty("机器码")
    @Excel(name = "机器码")
    private String hdInfo;

    /** 探针名称 */
    @ApiModelProperty("探针名称")
    @Excel(name = "探针名称")
    private String probeName;




}
