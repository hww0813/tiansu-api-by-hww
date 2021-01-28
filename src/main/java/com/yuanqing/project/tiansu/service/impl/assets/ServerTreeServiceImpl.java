package com.yuanqing.project.tiansu.service.impl.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.queue.ServerTreeMap;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.mapper.assets.ServerTreeMapper;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xucan on 2021-01-19 10:29
 * @author xucan
 */

@Service
@Transactional(readOnly = true)
public class ServerTreeServiceImpl implements IServerTreeService {

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Autowired
    private ServerTreeMapper serverTreeMapper;

    @Autowired
    private ICameraService cameraService;


    @Override
    public ServerTree findOne(Long serverIp) {
        List<ServerTree> list = serverTreeMapper.findOne(serverIp);
        return (list != null && list.size() > 0 ) ? list.get(0) : null;
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        return null;
    }

    @Override
    public List<ServerTree> getSessionServerList(JSONObject filters) {

        List<ServerTree> list = serverTreeMapper.getSessionServerList(filters);

        return list.stream().filter(f -> f != null).collect(Collectors.toList());
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        return null;
    }

    @Override
    public void batchInsert(List<ServerTree> list) {
        serverTreeMapper.batchInsert(list);
        //新增缓存里的数据
        list.stream().forEach(f -> putServerTreeMap(f));
    }

    @Override
    public Long findId() {
        return serverTreeMapper.findId();
    }

    @Override
    public void insert(ServerTree serverTree) {
        Long IP = serverTree.getServerIp();
        //根据ip查找客户端
        List<Client> clientList = clientMapper.findByIP(IP);

        //根据ip查找终端,先缓存中查找
        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, IP);
        ClientTerminal clientTerminal = ClientTerminalMap.get(clientTerminalKey);

        //缓存中没找到终端，在数据库中进行查找
        if (clientTerminal == null) {
            clientTerminal = clientTerminalMapper.findByIpAddress(IP);
            //将数据放入缓存中
            if (clientTerminal != null) {
                ClientTerminalMap.put(clientTerminalKey, clientTerminal);
            }
        }
        //如果存在客户端/终端，则更新对应终端/客户端的设备类型
        handleClient(clientList,clientTerminal);

        //入数据库
        serverTreeMapper.insertServerTree(serverTree);
        //入缓存
        putServerTreeMap(serverTree,IP);
    }

    @Override
    public Long save(ServerTree serverTree) {
        Long IP = serverTree.getServerIp();

        //根据ip查找客户端
        List<Client> clientList = clientMapper.findByIP(IP);
        //根据ip查找终端
        ClientTerminal clientTerminal = clientTerminalMapper.findByIpAddress(IP);

        //如果存在客户端/中毒案，则更新对应终端/客户端的设备类型
        handleClient(clientList,clientTerminal);

        ServerTree server = findOne(IP);

        //入库
        if (serverTree.getId() == null && server == null) {
            serverTreeMapper.insert(serverTree);
        } else {
            serverTreeMapper.update(serverTree);
        }
        //入缓存
        putServerTreeMap(serverTree);
        return serverTree.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        ServerTree server = findById(id);
        if (server == null) {
            throw new RuntimeException("entity not existed");
        }
        serverTreeMapper.delete(id);

        //删除缓存里的数据
        String serverKey = String.format(Constants.TWO_FORMAT, Constants.SERVER_TREE, server.getServerIp());
        ServerTreeMap.remove(serverKey);

        //服务器删除后，需要标记摄像头，并且恢复终端和客户端
        Long serverIp = server.getServerIp();
        //终端更新
        clientTerminalMapper.updateMark(serverIp);
        //客户端更新
        clientMapper.updateMark(serverIp);
        //根据ip更新摄像头，设置为确认不是服务器
        cameraService.updateIsNotServer(serverIp);
    }

    @Override
    public ServerTree findById(Long id) {
        return serverTreeMapper.findById(id);
    }

    @Override
    public List<ServerTree> getList(ServerTree serverTree) {
        return serverTreeMapper.getList(serverTree);
    }

    /**
     * 单条clientTerminal 存入clientTerminalMap缓存
     * @param serverTree
     */
    private void putServerTreeMap(ServerTree serverTree){
        String serverKey = String.format(Constants.TWO_FORMAT, Constants.SERVER_TREE, serverTree.getServerIp());
        ServerTreeMap.put(serverKey, serverTree);
    }
    private void putServerTreeMap(ServerTree serverTree,Long ip){
        String serverKey = String.format(Constants.TWO_FORMAT, Constants.SERVER_TREE, ip);
        ServerTreeMap.put(serverKey, serverTree);
    }


    /**
     *
     * 如果客户端不为空，则更新对应客户端的设备类型
     * 如果终端不为空，则更新对应终端的设备类型
     *
     * @param clientList 客户端
     * @param clientTerminal 终端
     */
    private void handleClient(List<Client> clientList,ClientTerminal clientTerminal){
        if (clientList.size() > 0) {
            for (Client client : clientList) {
                client.setDeviceType(DeviceType.PROXY_SERVER.getValue());
                clientMapper.update(client);
            }
        }
        if (clientTerminal != null) {
            clientTerminal.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            clientTerminalMapper.updateClientTerminal(clientTerminal);
        }
    }
}
