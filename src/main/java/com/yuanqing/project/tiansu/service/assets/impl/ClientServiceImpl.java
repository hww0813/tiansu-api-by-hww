package com.yuanqing.project.tiansu.service.impl.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceStatus;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.queue.ClientMap;
import com.yuanqing.common.queue.ServerTreeMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.event.OperationBehavior;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.event.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xucan on 2021-01-19 10:51
 * @author xucan
 */

@Service
public class ClientServiceImpl implements IClientService {

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private IServerTreeService serverTreeService;

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;

    @Override
    public boolean changStatus(List<Client> list) {
        return clientMapper.changStatus(list);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findOne(Long ipAddress, String username) {
        return clientMapper.findOne(ipAddress, username);
    }

    @Override
    public Map<DeviceStatus, Long> getCurrentStatus() {
        JSONObject filters = DateUtils.getDayTime();
        List<Client> list = clientMapper.getList(filters);
        return list.stream().collect(Collectors.groupingBy(Client::getStatus, Collectors.counting()));
    }

    @Override
    public Map<String, Integer> getClientDatas() {
        //获取新发现客户端
        JSONObject discoveryFilters = new JSONObject();
        discoveryFilters.put("status", 1);
        List<JSONObject> discoveryList = clientMapper.getTotal(discoveryFilters);
        int discovery = Integer.parseInt(discoveryList.get(0).get("COUNT(*)").toString());
        //获取活跃客户端
        JSONObject activeFilters = DateUtils.getDayTime();
        int active = operationBehaviorMapper.getDistinctClientNum(activeFilters);
        //获取客户端总量
        JSONObject countFilters = new JSONObject();
        List<JSONObject> countList = clientMapper.getTotal(countFilters);
        int count = Integer.parseInt(countList.get(0).get("COUNT(*)").toString());
        Map<String, Integer> map = new HashMap<>();
        map.put("discovery", discovery);
        map.put("active", active);
        map.put("count", count);
        return map;
    }

    @Override
    public List<Client> getActiveClient() {
        JSONObject filters = DateUtils.getDayTime();
        List<OperationBehavior> activeList = operationBehaviorMapper.getDistinctClientId(filters);
        List<Client> list = new ArrayList<>();
        for (int i = 0; i < activeList.size(); i++) {
            Long clientId = activeList.get(i).getClientId();
            Client client = clientMapper.findById(clientId);
            if (client != null) {
                list.add(client);
            }
        }
        return list;
    }

    @Override
    public List<Client> getByIpAndPort(Long serverIp,Long serverPort) {
        List<Client> list = clientMapper.getByIpAndPort(serverIp,serverPort);
        return list;
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        return null;
    }


    @Override
    public List<Client> userPage(JSONObject filters) {
        return clientMapper.getUserList(filters);
    }

    @Override
    public List<JSONObject> getClientVisitDetailToReport(JSONObject filters) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findByIp(Long ip) {
        return clientMapper.findByIP(ip);
    }

    @Override
    public void deleteByUsername(String username) {
        clientMapper.deleteByUsername(username);
    }

    @Override
    public void deleteByIpAddress(Long IpAddress) {
        clientMapper.deleteByIpAddress(IpAddress);
    }

    @Override
    public Long findId() {
        return clientMapper.findId();
    }

    @Override
    public void insert(Client client) {

        findServerByClientIp(client);
        //服务器为空，且客户端为空，则该ip对应的设备为客户端，在客户端表添加一条新纪录
        clientMapper.insertClient(client);
        //存入缓存中
        String clientKey = String.format(Constants.THREE_FORMAT,Constants.CLIENT,client.getIpAddress(),client.getUsername());
        ClientMap.put(clientKey,client);

    }

    @Override
    public void update(Client client) {

        findServerByClientIp(client);
        //如果服务器为空，且客户端表对应的客户端不为空，则该ip对应的设备为客户端，在客户端表更新对应的客户端
        clientMapper.update(client);
        //更新缓存
        String clientKey = String.format(Constants.THREE_FORMAT,Constants.CLIENT,client.getIpAddress(),client.getUsername());
        ClientMap.put(clientKey,client);

    }

    @Override
    public void updateMark(Long ipAddress) {
        clientMapper.updateMark(ipAddress);
    }

    @Override
    public void batchUpdateMark(List<Client> list) {
        clientMapper.batchUpdateMark(list);
    }

    @Override
    public Long save(Client entity) {
        //根据IP查找服务器
        Long IP = entity.getIpAddress();
        ServerTree serverTree = serverTreeService.findOne(IP);
        if (entity.getId() == null) {
            //如果服务器不为空，则该ip对应的设备为服务器，在客户端表将对应得客户端设备类型设置为服务器
            if (serverTree != null) {
                entity.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            }
            //服务器为空，且客户端为空，则该ip对应的设备为客户端，在客户端表添加一条新纪录
            clientMapper.insert(entity);
        } else {
            //如果服务器不为空，且客户端表对应的客户端不为空，则该ip对应的设备为服务器，在客户端表删除对应的客户端
            if (serverTree != null) {
                entity.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            }
            //如果服务器为空，且客户端表对应的客户端不为空，则该ip对应的设备为客户端，在客户端表更新对应的客户端
            clientMapper.update(entity);

        }
        return entity.getId();
    }

    @Override
    public void deleteById(Long id) {
        Client entity = findById(id);
        if (entity == null) {
            throw new RuntimeException("entity not existed");
        }
        clientMapper.delete(id);
        //删除缓存中里的数据
        String clientKey = String.format(Constants.THREE_FORMAT,Constants.CLIENT,entity.getIpAddress(),entity.getUsername());
        ClientMap.remove(clientKey);
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientMapper.findById(id);
    }

    @Override
    public List<Client> getList(JSONObject filters) {
        return clientMapper.getList(filters);
    }

    /**
     * 根据客户端IP 查找服务器(数据库/缓存)，如果有就将client.deviceType 设置为PROXY_SERVER
     * @param client
     */
    private void findServerByClientIp(Client client){
        //根据IP查找服务器
        Long IP = client.getIpAddress();
        //根据ip查找缓存
        String serverTreeKey = String.format(Constants.TWO_FORMAT,Constants.SERVER_TREE , IP);
        ServerTree serverTree = ServerTreeMap.get(serverTreeKey);
        //缓存中没有，在数据库中进行查找
        if (serverTree == null) {
            serverTree = serverTreeService.findOne(IP);
            //在数据库中找到了，将数据存入缓存中
            if (serverTree != null) {
                ServerTreeMap.put(serverTreeKey,serverTree);
                //如果服务器不为空，且客户端表对应的客户端不为空，则该ip对应的设备为服务器，在客户端表删除对应的客户端
                client.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            }
        }
    }
}
