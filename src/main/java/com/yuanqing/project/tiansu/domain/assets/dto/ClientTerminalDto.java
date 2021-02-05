package com.yuanqing.project.tiansu.domain.assets.dto;

import com.yuanqing.project.tiansu.domain.assets.base.BaseClient;
import lombok.Data;

/**
 *
 * 终端Dto
 * Created by xucan on 2021-02-03 21:13
 * @author xucan
 */

@Data
public class ClientTerminalDto extends BaseClient {

    private Integer userCnt;

}
