package com.yuanqing.project.tiansu.domain.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 操作行为
 *
 * @author jqchu
 * @version 1.0
 * @since 2017/11/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationBehavior extends BaseEntity {

    private Long id;

    private Long clientId; //客户端ID

    private String srcCode; //源设备编码

    private String srcDomain; //源设备域

    private Long srcIp; //源IP

    private String username; //用户名

    private Integer srcPort; //源端口

    private String dstDomain; //目的设备域

    private String srcMac; //源MAC

    private Long cameraId; //摄像头ID


    private Integer dstPort; //目的端口

    private String dstMac; //目的MAC

    private String action; //操作类型

    private String actionDetail; //操作详细类型


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stamp;//操作时间

    private String content; //内容

    private Long sessionId;

    private String dstDeviceName; //目的设备名

    private Long dstDeviceIp; //目的设备ip

    private String result;

    private Long upFlow = 0L;

    private Long downFlow = 0L;

    private String callId;

    private String uuid;

    private String platformKey;

    private Integer transfered;

    private String platformName;

    private String probeHdInfo;

    private String fileUrl;

    private String bitRate;

    private Long serverIp;

    /** 相关动作uuid */
    private String relationOperUuid;

    // 回放/下载开始、结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime endTime;

    /** 动作开始时间 */
    private String operStart;

    /** 动作结束时间 */
    private String operEnd;

    private String[] regionList;

    private String provinceRegion;

    private String cityRegion;

    private String countryRegion;

    private String connectType;

    private String orderType;

    private String orderValue;

    private String dstCode; //目的设备编码

    private Long dstIp; //目的IP

    private Integer num;

    private Integer size;

}
