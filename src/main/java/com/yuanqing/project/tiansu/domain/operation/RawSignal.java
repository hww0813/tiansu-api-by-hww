package com.yuanqing.project.tiansu.domain.operation;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.common.enums.ConnectTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("原始信令对象")
public class RawSignal extends OperationBehaviorSearch {

    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("源IP")
    private Long srcIp;
    @ApiModelProperty("源MAC")
    private String srcMac;
    @ApiModelProperty("源端口")
    private Integer srcPort;
    @ApiModelProperty("目的IP")
    private Long dstIp;
    @ApiModelProperty("目的MAC")
    private String dstMac;
    @ApiModelProperty("目的端口")
    private Integer dstPort;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    @ApiModelProperty("时间")
    private LocalDateTime stamp;
    private String requestLineType;

    private String requestLineContent;

    private String fromCode;

    private String fromDomain;

    private String toCode;

    private String toDomain;

    private String contentLength;

    private String contact;

    private String cseq;

    private String callId;

    private String via;

    private String subject;

    private String contentType;

    private String maxForwards;

    private String sipAuthorization;

    private String expires;

    private String wwwAuthenticate;

    private String ownerCode;

    private String ownerIp;

    private String deviceId;

    private String action;

    private String content;

    private Long operId;

    private Long dataSize;

    private ConnectTypeEnum connectType;


    private String orderType;


}
