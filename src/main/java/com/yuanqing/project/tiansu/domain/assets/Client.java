package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.project.tiansu.domain.assets.base.BaseClient;
import lombok.Data;

/**
 * Created by xucan on 2021-01-19 10:41
 * @author xucan
 */

@Data
public class Client extends BaseClient {

    /** 登录账号 */
    private String username;

}
