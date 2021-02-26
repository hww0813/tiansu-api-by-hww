package com.yuanqing.project.tiansu.domain.operation;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.common.enums.ConnectTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawSignal extends OperationBehaviorSearch {

    private Long id;
    private Long srcIp;
    private String srcMac;
    private Integer srcPort;
    private Long dstIp;
    private String dstMac;
    private Integer dstPort;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
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
