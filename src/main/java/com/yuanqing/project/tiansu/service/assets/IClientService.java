package com.yuanqing.project.tiansu.service.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.framework.web.service.BaseService;
import com.yuanqing.project.tiansu.domain.assets.Client;

import java.util.List;
import java.util.Map;

/**
 * Created by xucan on 2021-01-19 10:51
 * @author xucan
 */
public interface IClientService extends BaseService<Client,Long> {
    /**
     * 修改选中数据的状态为已确认
     * @param list 集合
     * @return
     */
    boolean changStatus(List<Client> list);

    /**
     * 查找客户端
     * @param ipAddress  IP地址
     * @param username   登录账号
     * @return 如果存在返回客户端 否则返回NULL
     */
    Client findOne(Long ipAddress, String username);


    /**
     * 当天的客户端状态
     * @return
     */
    Map<Integer, Long> getCurrentStatus();

    /**
     * 获取首页客户端数据
     * @return
     */
    Map<String, Integer> getClientDatas();

    /**
     * 获取活跃客户端列表
     * @return 集合
     */
    List<Client> getActiveClient();

    /**
     * 根据域IP和域端口查找客户端信息
     * @param serverIp 服务器IP
     * @param serverPort 服务器端口
     * @return
     */
    List<Client> getByIpAndPort(Long serverIp,Long serverPort);


    /**
     * 获取List 转成报表
     * TODO: 报表相关暂时搁置
     * @param filters
     * @return
     */
    List<JSONObject> getAllToReport(JSONObject filters);

    /**
     * 获取客户端用户列表
     * @param filters 过滤
     * @return
     */
    List<Client> userPage(JSONObject filters);

    /**
     * TODO: 报表相关暂时搁置
     * @param filters
     * @return
     */
    List<JSONObject> getClientVisitDetailToReport(JSONObject filters);

    /**
     * 根据IP查询客户端
     * @param ip 客户端IP
     * @return 集合
     */
    List<Client> findByIp(Long ip);

    /**
     * 根据用户名删除客户端
     * @param username 用户名
     */
    void deleteByUsername(String username);

    /**
     * 根据IP地址删除客户端
     * @param IpAddress IP地址
     */
    void deleteByIpAddress(Long IpAddress);

    /**
     * 获取客户端下一个自增序列的值
     * @return 下一个自增序列的值
     */
    Long findId ();

    /**
     * 根据IP地址 去除打标（deviceType 设为null）
     * @param ipAddress IP地址
     */
    void updateMark(Long ipAddress);

    /**
     * 根据IP地址 批量去除打标（deviceType 设为null）
     * @param list 客户端集合
     */
    void batchUpdateMark(List<Client> list);


    /**
     * 获取终端相关用户数
     * @param ips  筛选条件
     * @return 列表
     */
     List<JSONObject> getUserNumByTerminal(List<Long> ips);

}
