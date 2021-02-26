package com.yuanqing.project.tiansu.domain.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.common.enums.ConnectTypeEnum;
import com.yuanqing.common.enums.SessionStatusEnum;
import com.yuanqing.project.tiansu.domain.video.StatisticsSearch;
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
public class OperationBehaviorSession extends OperationBehaviorSearch {

    private Long id;
    /**客户端ID*/
    private Long clientId;
    /**源设备编码*/
    private String srcCode;
    /**源IP*/
    private Long srcIp;
    //源端口
    private Integer srcPort;
    //源MAC
    private String srcMac;
    //用户名
    private String username;
    //摄像头ID
    private Long cameraId;
    //目的设备编码
    private String dstCode;
    //目的IP
    private Long dstIp;
    //目的端口
    private Integer dstPort;
    //目的MAC
    private String dstMac;
    private Long upFlow;
    private Long downFlow;
    //时长
    private String totalTime;

    private ConnectTypeEnum connectType;

    private SessionStatusEnum status;

    private String orderType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime activeTime;
}
