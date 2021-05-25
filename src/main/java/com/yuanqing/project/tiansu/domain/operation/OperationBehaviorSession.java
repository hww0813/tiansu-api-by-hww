package com.yuanqing.project.tiansu.domain.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yuanqing.common.enums.ConnectTypeEnum;
import com.yuanqing.common.enums.SessionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorSession
 * @Description 操作行为回话
 * @Date 2021/2/25 14:44
 * @Version V1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("操作行为会话对象")
public class OperationBehaviorSession extends OperationBehaviorSearch {

    @ApiModelProperty("ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 客户端ID
     */
    @ApiModelProperty("客户端ID")
    private Long clientId;

    /**
     * 源设备编码
     */
    @ApiModelProperty("源设备编码")
    private String srcCode;

    /**
     * 源IP
     */
    @ApiModelProperty("源IP")
    private Long srcIp;

    /**
     * 源端口
     */
    @ApiModelProperty("源端口")
    private Integer srcPort;

    /**
     * 源MAC
     */
    @ApiModelProperty("源MAC")
    private String srcMac;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 摄像头ID
     */
    @ApiModelProperty("摄像头ID")
    private Long cameraId;

    /**
     * 目的设备编码
     */
    @ApiModelProperty("目的设备编码")
    private String dstCode;

    /**
     * 目的IP
     */
    @ApiModelProperty("目的IP")
    private Long dstIp;

    /**
     * 目的端口
     */
    @ApiModelProperty("目的端口")
    private Integer dstPort;

    /**
     * 目的MAC
     */
    @ApiModelProperty("目的MAC")
    private String dstMac;

    @ApiModelProperty("上行流量")
    private Long upFlow;

    @ApiModelProperty("下行流量")
    private Long downFlow;

    //时长
    @ApiModelProperty("时长")
    private String totalTime;

    @ApiModelProperty("联结类型")
    private ConnectTypeEnum connectType;

    @ApiModelProperty("会话状态")
    private SessionStatusEnum status;

    @ApiModelProperty("排序")
    private String orderType;

    @ApiModelProperty("活跃时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime activeTime;

}
