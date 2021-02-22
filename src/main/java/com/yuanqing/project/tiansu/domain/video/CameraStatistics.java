package com.yuanqing.project.tiansu.domain.video;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Dong.Chao
 * @Classname CameraStatistics
 * @Description
 * @Date 2021/2/4 22:06
 * @Version V1.0
 */
@Data
public class CameraStatistics {

    @JsonProperty("CAMERA_ID")
    private String cameraId;

    @JsonProperty("CAMERA_REGION")
    private String cameraRegion;

    @JsonProperty("CAMERA_NAME")
    private String cameraName;

    @JsonProperty("CLIENT_CNT")
    private String clientCnt;

    @JsonProperty("CAMERA_IP")
    private String cameraIp;

    @JsonProperty("USERNAME")
    private String userName;

    @JsonProperty("VISITED_CNT")
    private String visitedCnt;

    @JsonProperty("CAMERA_CODE")
    private String cameraCode;

}
