package com.yuanqing.project.tiansu.service.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 终端接口
 * @author xucan
 *
 */

@Validated
public interface IClientTerminalService extends BaseService<ClientTerminal, Long> {

    /**
     *
     * @param list
     * @return
     */
    boolean changStatus(List<ClientTerminal> list);

    /**
     *
     * @param filters
     * @return
     */
    List<JSONObject> getAllToReport(JSONObject filters);

    /**
     *
     * @param clientTerminal
     * @return
     */
    Long insertInto(ClientTerminal clientTerminal);

    /**
     * @return
     */
    List<JSONObject> getDistinctClientId();


}
