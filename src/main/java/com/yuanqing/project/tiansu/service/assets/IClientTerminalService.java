package com.yuanqing.project.tiansu.service.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 终端接口
 * @author xucan
 *
 */

@Validated
public interface IClientTerminalService extends BaseService<ClientTerminal, Long> {

    List<ClientTerminal> getListWithOrder(ClientTerminal clientTerminal, String orderStr);

    /**
     *
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);

    /**
     *
     * @param filters
     * @return
     */
    List<JSONObject> getAllToReport(JSONObject filters);


    /**
     * 获取活跃终端
     * @param clientTerminal
     * @return
     */
    List<ClientTerminal> getActiveTerminal(ClientTerminal clientTerminal);


    /**
     * client/terminal 同步删除
     * @param id
     * @param ipAddress
     */
    void delete(Long id,Long ipAddress);

    /**
     * 数据处理
     * 将终端列表根据client 关联出用户数并转换为dto对象
     * @param list 终端集合
     * @return 包含用户数的终端集合
     */
    List<ClientTerminalDto> handleTerminalUserNum(List<ClientTerminal> list);

    /**
     * 根据终端 list 获取终端列表
     * @param list 终端list
     * @return
     */
    List<ClientTerminal> getTerminalByClientList(List<Client> list);


    /**
     * 根据ip list 获取终端列表
     * @param ipList IP list
     * @return
     */
    List<ClientTerminal> getTerminalByIpList(List<Long> ipList,ClientTerminal clientTerminal);

    /**
     * 根据ip找终端
     * @param ip
     * @return
     */
    ClientTerminal findByIpAddress(Long ip);
}
