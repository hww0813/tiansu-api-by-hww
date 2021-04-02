package com.yuanqing.project.tiansu.service.authorize;

import com.alibaba.fastjson.JSONObject;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-04-01 10:13
 */
public interface IAuthorizeService {

    /**
     * 验证摄像头和用户许可数
     * @return
     */
    boolean checkPermit();

    /**
     * 新增限制条件
     */
    JSONObject replaceFilter(JSONObject filter);

    JSONObject replaceUserFilter(JSONObject filters);

    JSONObject getPermitNum();

}
