package com.yuanqing.project.tiansu.domain.analysis.dto;

import com.yuanqing.framework.web.domain.BaseEntity;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-08 15:44
 */
@Data
public class CameraVisitDto extends BaseEntity {

    private String id;

    private String deviceCode;

    private Long ipAddress;

    private String deviceName;

    private String regionName;

    private Integer action;

    private Long visitCnt;

    private Long terminalCnt;

    private List<CameraVisit> children = new ArrayList<>();

}
