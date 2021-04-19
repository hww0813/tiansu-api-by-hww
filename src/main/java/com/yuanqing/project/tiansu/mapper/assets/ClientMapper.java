package com.yuanqing.project.tiansu.mapper.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.mapper.BaseMapper;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xucan on 2021-01-19 10:49
 * @author xucan
 */
@Repository
public interface ClientMapper extends BaseMapper<Client,Long> {

    /**
     * 修改选中数据的状态为已确认
     * @param list 集合
     * @return 操作是否成功
     */
    boolean changStatus(List<Client> list);

    /**
     * 查找最新登录的客户端
     * @param ipAddress  ip地址
     * @param username   登录用户名
     * @return 客户端
     */
    Client findOne(@Param("ipAddress") Long ipAddress, @Param("username") String username);

    /**
     * 根据域IP和域端口查找客户端信息
     * @param serverIp 服务器IP
     * @param serverPort 服务器端口
     * @return 集合
     */
    List<Client> getByIpAndPort(@Param("serverIp") Long serverIp,
                                @Param("serverPort") Long serverPort);


    /**
     * 获取活跃客户端列表
     * @param jsonObject 过滤
     * @return 集合
     */
    List<Client> getActiveClient(JSONObject jsonObject);

    /**
     * 获取最大的ID
     * @return 最大的ID
     */
    Client maxId();

    /**
     * 根据IP查询客户端
     * @param ipAddress 客户端IP
     * @return 集合
     */
    List<Client> findByIP(@Param("ipAddress")Long ipAddress);

    /**
     * 获取客户端用户列表
     * @param filters 过滤
     * @return 集合
     */
    List<Client> getUserList(JSONObject filters);

    /**
     * 获取客户端总数
     * @param filters 过滤
     * @return 统计结果
     */
    List<JSONObject> getTotal(JSONObject filters);

    /**
     * 获取客户端访问集合
     * @param filters 过滤
     * @return 集合
     */
    List<JSONObject> getClientVisitList(JSONObject filters);

    /**
     * 获取客户端访问详情
     * @param filters 过滤
     * @return 操作行为集合
     */
    List<OperationBehavior> getClientVisitDetailList(JSONObject filters);

    /**
     * 获取客户端访问相关摄像头集合
     * @param filters 过滤
     * @return 集合
     */
    List<JSONObject> getClientVisitRelatedCameraList(JSONObject filters);


    /**
     * 根据用户名删除客户端
     * @param username 用户名
     */
    void deleteByUsername(String username);

    /**
     * 根据IP地址删除客户端
     * @param ipAddress IP地址
     */
    void deleteByIpAddress(Long ipAddress);

    /**
     * 根据IP地址 批量去除打标（deviceType 设为null）
     * @param list 客户端集合
     */
    void batchUpdateMark(List<Client> list);

    /**
     * 根据IP地址 去除打标（deviceType 设为null）
     * @param ipAddress IP地址
     */
    void updateMark(Long ipAddress);

    /**
     * 获取客户端下一个自增序列的值
     * @return 下一个自增序列的值
     */
    Long findId ();

    /**
     * 新增一个客户端
     * @param client 客户端
     */
    void insertClient(Client client);

    /**
     * 查询终端IP对应的用户数
     * @param ips IP集合
     * @return
     */
    List<JSONObject> getUserNumByTerminal(List<Long> ips);

    /**
     * 查询用户名插对应的终端数
     * @param usernameList 用户名集合
     * @return
     */
    List<JSONObject> getTerminalNumByUserName(List<String> usernameList);


    List<Client> getClientByIpList(@Param("list") List<Long> ipList,
                                   @Param("filter") Client client);

    List<JSONObject> getClientOperationTrend(Client entity);
}
