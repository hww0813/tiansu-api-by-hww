package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 终端
 *
 * @author Ypengfei
 *
 * @since 2019/09/05
 */

@Component
public interface ClientTerminalMapper extends BaseMapper<ClientTerminal, Long> {

    /**
     * 批量更新终端状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);


    /**
     * 根据IP list 查询终端
     */

    List<ClientTerminal> getClientTerminalByIpList(List<Client> list);

    ClientTerminal findByIpAddress(Long ip);

    void updateClientTerminal(ClientTerminal clientTerminal);

    void insertInto(ClientTerminal clientTerminal);

    void updateMark(Long serverIp);
}
