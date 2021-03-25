package com.yuanqing.project.tiansu.domain.assets.base;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yuanqing.framework.web.domain.BaseEntity;
import lombok.Data;

/**
 * Created by xucan on 2021-02-03 22:58
 * @author xucan
 */
@Data
public class BaseClientUser extends BaseEntity {

    /** id */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /** 登录账号 */
    private String username;

    /** 用户状态 */
    private Integer status;

    /** 用户来源 */
    private String source;
}
