package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xucan on 2021-01-18 16:29
 * @author xucan
 */

@Service
public class ClientTerminalServiceImpl implements IClientTerminalService {

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Autowired
    private IServerTreeService serverTreeService;

    @Override
    public boolean changStatus(List<ClientTerminal> list) {
        return clientTerminalMapper.changStatus(list);
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        return null;
    }

    @Override
    public Long insertInto(ClientTerminal clientTerminal) {
        //根据IP查找终端
        ClientTerminal clientTerminal1 = clientTerminalMapper.findByIpAddress(clientTerminal.getIpAddress());
        //根据IP查找服务器
        Long IP = clientTerminal.getIpAddress();
        ServerTree serverTree = serverTreeService.findOne(IP);
        if (clientTerminal1 != null) {
            //如果服务器不为空，且终端不为空，则改IP对应的设备为服务器。则在终端表中删除对应的终端。
            if (serverTree != null) {
                clientTerminal.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            }
            //如果终端为空，服务器为空，则更新对应的终端
            clientTerminalMapper.updateClientTerminal(clientTerminal);
        } else {
            //如果终端为空，服务器不为空，则该IP对应的设备为服务器，不增加对应的终端
            if (serverTree != null) {
                clientTerminal.setDeviceType(DeviceType.PROXY_SERVER.getValue());
            }
            //如果终端为空，服务器为空，则该IP对应的设备为终端，在终端表新增加终端
            clientTerminalMapper.insertInto(clientTerminal);
        }
        //新增缓存
        putClientTerminalMap(clientTerminal);
        return clientTerminal.getId();
    }

    @Override
    public List<JSONObject> getDistinctClientId() {
        JSONObject filters = DateUtils.getDayTime();
        List<JSONObject> list = clientTerminalMapper.getDistinctClientId(filters);
        return list;
    }

    @Override
    public Long save(ClientTerminal clientTerminal) {
        if (clientTerminal.getId() == null) {
            System.out.println(clientTerminal + "to manager save");
            clientTerminalMapper.insert(clientTerminal);
        } else {
            System.out.println(clientTerminal + "to manager update");
            clientTerminalMapper.update(clientTerminal);
        }
        return clientTerminal.getId();
    }

    @Override
    public void deleteById(Long id) {
        ClientTerminal clientTerminal = findById(id);
        if (clientTerminal == null) {
            throw new RuntimeException("entity not existed");
        }

        //删除数据库
        clientTerminalMapper.delete(id);
        //删除缓存中的数据
        delClientTerminalMap(clientTerminal);
    }

    @Override
    public ClientTerminal findById(Long id) {
        return clientTerminalMapper.findById(id);
    }


    @Override
    public List<ClientTerminal> getList(ClientTerminal clientTerminal) {
        return null;
    }

    /**
     * 单条clientTerminal 存入clientTerminalMap缓存
     * @param clientTerminal
     */
    private void putClientTerminalMap(ClientTerminal clientTerminal){
        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, clientTerminal.getIpAddress());
        ClientTerminalMap.put(clientTerminalKey,clientTerminal);
    }

    /**
     * 单条clientTerminal 删除clientTerminalMap缓存
     * @param clientTerminal
     */
    private void delClientTerminalMap(ClientTerminal clientTerminal){
        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, clientTerminal.getIpAddress());
        ClientTerminalMap.remove(clientTerminalKey);
    }
}
