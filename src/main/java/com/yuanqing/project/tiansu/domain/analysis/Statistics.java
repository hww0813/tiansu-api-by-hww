package com.yuanqing.project.tiansu.domain.analysis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.framework.web.domain.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-03-11 16:06
 */

@Data
public class Statistics extends BaseEntity {

    private Long srcIp;

    private String username;

    private String dstCode;

    private Integer action;

    private Long count;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stamp;


}
