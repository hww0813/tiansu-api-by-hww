package com.yuanqing.project.tiansu.domain.analysis;

import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * 终端分析实体类
 * @author xucan
 * @version 1.0
 * @Date 2021-03-02 10:08
 */

@Data
public class TerminalVisit extends BaseEntity {

    private Long ipAddress;

    private String username;

    private Integer action;

    private Long visitCnt;

    private Long cameraCnt;

}
