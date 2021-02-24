package com.yuanqing.project.tiansu.domain.macs;

import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统配置表对象 macs_config
 *
 * @author xucan
 * @date 2020-10-21
 */
@ApiModel("系统配置表对象")
public class MacsConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty("ID")
    private Long id;

    /** 类型 */
    @ApiModelProperty("类型")
    private String type;

    /** 名称 */
    @ApiModelProperty("名称")
    private String name;

    /** 描述信息 */
    @ApiModelProperty("描述信息")
    private String desp;

    /** 值 */
    @ApiModelProperty("值")
    private String value;

    /** 默认值 */
    @ApiModelProperty("默认值")
    private String defaultValue;

    /** 是否展示 */
    @ApiModelProperty("是否展示")
    private Long visible;

    /** 是否可修改 */
    @ApiModelProperty("是否可修改")
    private Long modifiable;

    /** 字段类型( */
    @ApiModelProperty("字段类型")
    private String fieldType;

    /** 字段注释 */
    @ApiModelProperty("字段注释")
    private String fieldRemark;

    /** 字段值 */
    @ApiModelProperty("字段值")
    private String fieldValues;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setDesp(String desp)
    {
        this.desp = desp;
    }

    public String getDesp()
    {
        return desp;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }
    public void setVisible(Long visible)
    {
        this.visible = visible;
    }

    public Long getVisible()
    {
        return visible;
    }
    public void setModifiable(Long modifiable)
    {
        this.modifiable = modifiable;
    }

    public Long getModifiable()
    {
        return modifiable;
    }
    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }

    public String getFieldType()
    {
        return fieldType;
    }
    public void setFieldRemark(String fieldRemark)
    {
        this.fieldRemark = fieldRemark;
    }

    public String getFieldRemark()
    {
        return fieldRemark;
    }
    public void setFieldValues(String fieldValues)
    {
        this.fieldValues = fieldValues;
    }

    public String getFieldValues()
    {
        return fieldValues;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("name", getName())
            .append("desp", getDesp())
            .append("value", getValue())
            .append("defaultValue", getDefaultValue())
            .append("visible", getVisible())
            .append("modifiable", getModifiable())
            .append("fieldType", getFieldType())
            .append("fieldRemark", getFieldRemark())
            .append("fieldValues", getFieldValues())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
