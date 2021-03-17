package com.yuanqing.project.tiansu.domain.assets;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuanqing.project.tiansu.domain.assets.base.BaseClient;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户端
 *
 * @author jqchu
 * @version 1.0
 **/

@Data
public class SessionClient extends BaseClient {

    private String username;

    private int count;

}
