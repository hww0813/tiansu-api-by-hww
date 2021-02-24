package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public boolean changStatus(String[] ids) {
        return clientTerminalMapper.changStatus(ids);
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
    public List<ClientTerminal> getActiveTerminal() {
        ClientTerminal clientTerminalFilter = (ClientTerminal)DateUtils.getDayTime(ClientTerminal.class);
        List<ClientTerminal> list = clientTerminalMapper.getList(clientTerminalFilter);
        return list;
    }


    //TODO:每一步进行判空
    @Override
    public List<ClientTerminalDto> handleTerminalUserNum(List<ClientTerminal> clientTerminalList) {

        if(CollectionUtils.isEmpty(clientTerminalList)){
            throw new CustomException("没有查询到结果");
        }

        //提取集合IP
        List<Long> ipList = clientTerminalList.stream().map(f -> f.getIpAddress()).collect(Collectors.toList());

        //获取IP 对应的用户数
        List<JSONObject> originalUserNum = clientMapper.getUserNumByTerminal(ipList);

        Map<Long,Integer> map = new HashMap<>();

        //格式化原始数据
        originalUserNum.stream().forEach(f -> map.put(f.getLong("ip_address"),f.getInteger("userCnt")));

        //将带有用户数的信息转换为dto
        List<ClientTerminalDto> dtoList = new ArrayList<>();

        clientTerminalList.stream().forEach(f -> {
            ClientTerminalDto dto = doBackward(f);
            Integer cnt = map.get(f.getIpAddress());
            dto.setUserCnt(cnt == null ? 0 : cnt);
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public List<ClientTerminal> getTerminalByIpList(List<Client> ipList) {
        List<ClientTerminal> list = null;
        if(!CollectionUtils.isEmpty(ipList)){
             list = clientTerminalMapper.getClientTerminalByIpList(ipList);
        }
        return list;
    }


    @Override
    public Long save(ClientTerminal clientTerminal, SaveType type) {

        if (type.getCode()==1) {
            clientTerminalMapper.insert(clientTerminal);
        } else if(type.getCode()==0){
            clientTerminalMapper.update(clientTerminal);
        }
        return clientTerminal.getId();
    }

    @Override
    public Long save(ClientTerminal clientTerminal) {
        throw new CustomException("暂不支持这种保存方式,需要传入SaveType");
    }

    @Override
    public void deleteById(@Valid @NotNull(message = "根据ID删除的ID不能为空") Long id) {
        clientTerminalMapper.delete(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id,Long ipAddress) {
        ClientTerminal clientTerminal = findById(id);
        if (clientTerminal == null) {
            throw new RuntimeException("entity not existed");
        }

        //删除数据库
        clientTerminalMapper.delete(id);
        clientMapper.deleteByIpAddress(ipAddress);
        //删除缓存中的数据
        delClientTerminalMap(clientTerminal);
    }

    @Override
    public ClientTerminal findById(Long id) {
        return clientTerminalMapper.findById(id);
    }


    @Override
    public List<ClientTerminal> getList(ClientTerminal clientTerminal) {
        return clientTerminalMapper.getList(clientTerminal);
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

    protected ClientTerminalDto doBackward(ClientTerminal clientTerminal) {
        ClientTerminalDto dto = new ClientTerminalDto();
        BeanUtils.copyProperties(clientTerminal, dto);
        return dto;
    }
}
