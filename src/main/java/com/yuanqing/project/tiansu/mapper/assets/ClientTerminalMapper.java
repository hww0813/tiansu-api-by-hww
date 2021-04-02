package com.yuanqing.project.tiansu.mapper.assets;

import com.yuanqing.framework.web.mapper.BaseMapper;
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

    List<ClientTerminal> getListWithOrder(@Param("clientTerminal") ClientTerminal clientTerminal, @Param("orderStr") String orderStr);

    /**
     * 批量更新终端状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);


    /**
     * 根据IP list 查询终端
     * @param list IP list
     * @param clientTerminal 过滤条件
     * @return
     */
    List<ClientTerminal> getClientTerminalByIpList(@Param("list") List<Long> list,
                                                   @Param("filter") ClientTerminal clientTerminal);

    ClientTerminal findByIpAddress(Long ip);

    void updateClientTerminal(ClientTerminal clientTerminal);


    void updateMark(Long serverIp);

    Integer getRealTotal();
}
