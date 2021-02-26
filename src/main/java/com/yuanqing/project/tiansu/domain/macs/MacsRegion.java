package com.yuanqing.project.tiansu.domain.macs;

import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 地区配置对象 macs_region
 *
 * @author xucan
 * @date 2020-10-14
 */
@Data
@ApiModel("地区配置对象")
public class MacsRegion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 城市代码 */
    @ApiModelProperty("城市代码")
    private String id;

    /** 上级城市代码 */
    @ApiModelProperty("上级城市代码")
    private String parentId;

    /** 城市名称*/
    @ApiModelProperty("城市名称")
    private String name;

    /** 城市级别 */
    @ApiModelProperty("城市级别")
    private Integer level;


}
