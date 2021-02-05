package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
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

    /**
     * 批量更新终端状态
     * @param ids
     * @return
     */
    boolean changStatus(String[] ids);

    /**
     *
     * @param entity
     * @return
     */
    Long insertInto(ClientTerminal entity);

    /**
     *
     * @param ipAddress
     * @return
     */
    ClientTerminal findByIpAddress(@Param("ipAddress") Long ipAddress);

    /**
     *
     * @param entity
     */
    void  updateClientTerminal(ClientTerminal entity);

    /**
     *
     * @param IpAddress
     */
    void deleteByIpAddress(Long IpAddress);

    /**
     *
     * @param filters
     * @return
     */
    List<JSONObject> getTotal(JSONObject filters);

    /**
     *
     * @param filters
     * @return
     */
    List<JSONObject> getDistinctClientId(JSONObject filters);

    /**
     * 根据摄像头ip匹配终端,更新
     * @param list
     */
    void batchUpdateMark(List<ClientTerminal> list);

    /**
     * 根据ip更新终端设备类型
     * @param ipAddress
     */
    void updateMark(Long ipAddress);


}
