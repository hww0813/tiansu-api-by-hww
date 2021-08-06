package com.yuanqing.project.tiansu.domain.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.aspectj.lang.annotation.Excel;
import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("原始流量对象")
@Data
public class RawNetFlow extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 源IP
     */
    @ApiModelProperty("源IP")
    @Excel(name = "源IP")
    private Long srcIp;

    /**
     * 源端口
     */
    @ApiModelProperty("源端口")
    @Excel(name = "源端口")
    private String srcPort;

    /**
     * 源MAC
     */
    @ApiModelProperty("源MAC")
    @Excel(name = "源MAC")
    private String srcMac;

    /**
     * 目的IP
     */
    @ApiModelProperty("目的IP")
    @Excel(name = "目的IP")
    private Long dstIp;

    /**
     * 目的端口
     */
    @ApiModelProperty("目的端口")
    @Excel(name = "目的端口")
    private String dstPort;

    /**
     * 目的MAC
     */
    @ApiModelProperty("目的MAC")
    @Excel(name = "目的MAC")
    private String dstMac;

    /**
     * 时间
     */
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
//    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd")
    private LocalDateTime stamp;

    /**
     * 包大小
     */
    @ApiModelProperty("包大小")
    @Excel(name = "包大小")
    private Long packetSize;

    /**
     * 包类型
     */
    @ApiModelProperty("包类型")
    @Excel(name = "包类型")
    private String packetType;

    /**
     * 包数量
     */
    @ApiModelProperty("包数量")
    @Excel(name = "包数量")
    private Long packetCount;


    private String OrderType;

    public Long getPacketCount() {
        return packetCount;
    }

    public void setPacketCount(Long packetCount) {
        this.packetCount = packetCount;
    }
}
