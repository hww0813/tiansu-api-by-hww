package com.yuanqing.project.tiansu.domain.assets.dto;

import com.yuanqing.project.tiansu.domain.assets.base.BaseClientUser;
import lombok.Data;

/**
 * Created by xucan on 2021-02-03 22:58
 * @author xucan
 */

@Data
public class ClientUserDto extends BaseClientUser {

    private Integer terminalCnt;
}
