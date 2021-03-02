package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.queue.ClientTerminalMap;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xucan on 2021-01-18 16:29
 *
 * @author xucan
 */

@Service
public class ClientTerminalServiceImpl implements IClientTerminalService {

    private static final Logger log = LoggerFactory.getLogger(ClientTerminalServiceImpl.class);

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Autowired
    private IServerTreeService serverTreeService;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private IClientService clientService;

    @Override
    public boolean changStatus(String[] ids) {
        return clientTerminalMapper.changStatus(ids);
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        ClientTerminal condClientTerminal = new ClientTerminal();
        condClientTerminal.setIpAddress(IpUtils.ipToLong(filters.getString("ipAddress")));
        condClientTerminal.setStatus(filters.getInteger("status"));
//        condClientTerminal.setDeviceCode(deviceCode);
//        condClientTerminal.setSipServerId(sipServerId);
//        condClientTerminal.setId(id);
//        condClientTerminal.setRegionId(regionId);

        String username = filters.getString("username");

        List<ClientTerminal> list = null;
        //判断 username 是否为空
        if (StringUtils.isNotEmpty(username)) {
            Client client = new Client();
            client.setUsername(username);

            // 根据用户名查询client列表  需要用IP
            List<Client> clientList = clientService.getList(client);

            list = getTerminalByIpList(clientList);

        } else {
            list = getList(condClientTerminal);
        }

        //用户数
        List<ClientTerminalDto> clientTerminalDtoList = handleTerminalUserNum(list);

        List<JSONObject> reportList = new ArrayList<JSONObject>();
        if (!CollectionUtils.isEmpty(clientTerminalDtoList)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (ClientTerminalDto clientTerminalDto : clientTerminalDtoList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceCode", clientTerminalDto.getDeviceCode());
                if (clientTerminalDto.getIpAddress() != null) {
                    jsonObject.put("ipAddress", IpUtils.longToIPv4(clientTerminalDto.getIpAddress()));
                }
                jsonObject.put("macAddress", clientTerminalDto.getMacAddress());
                if (clientTerminalDto.getUserCnt() != null) {
                    jsonObject.put("usercnt", String.valueOf(clientTerminalDto.getUserCnt()));
                } else {
                    jsonObject.put("usercnt", "0");
                }
                if (clientTerminalDto.getStatus() != null && 0 == clientTerminalDto.getStatus()) {
                    jsonObject.put("status", "已确认");
                } else if (1 == clientTerminalDto.getStatus()) {
                    jsonObject.put("status", "新发现");
                }
                if (clientTerminalDto.getUpdateTime() != null) {
                    jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, clientTerminalDto.getUpdateTime()));
                }
                reportList.add(jsonObject);
            }
        }

        return reportList;
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
        ClientTerminal clientTerminalFilter = (ClientTerminal) DateUtils.getDayTime(ClientTerminal.class);
        List<ClientTerminal> list = clientTerminalMapper.getList(clientTerminalFilter);
        return list;
    }


    @Override
    public List<ClientTerminalDto> handleTerminalUserNum(List<ClientTerminal> clientTerminalList) {

        if (CollectionUtils.isEmpty(clientTerminalList)) {
            log.error("终端列表为空");
            return null;
        }

        //提取集合IP
        List<Long> ipList = clientTerminalList.stream().map(f -> f.getIpAddress()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ipList)) {
            log.error("提取集合IP异常,集合为空");
            return null;
        }

        //获取IP 对应的用户数
        List<JSONObject> originalUserNum = clientMapper.getUserNumByTerminal(ipList);

        if (CollectionUtils.isEmpty(originalUserNum)) {
            log.error("获取IP对应的用户数结果为空");
            return null;
        }

        Map<Long, Integer> map = new HashMap<>();

        //格式化原始数据
        originalUserNum.stream().forEach(f -> map.put(f.getLong("ip_address"), f.getInteger("userCnt")));

        if (CollectionUtils.isEmpty(map)) {
            log.error("用户数量结果格式化异常,map为空");
            return null;
        }

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
        if (!CollectionUtils.isEmpty(ipList)) {
            list = clientTerminalMapper.getClientTerminalByIpList(ipList);
        }
        return list;
    }


    @Override
    public Long save(ClientTerminal clientTerminal, SaveType type) {

        if (type.getCode() == 1) {
            clientTerminalMapper.insert(clientTerminal);
        } else if (type.getCode() == 0) {
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
    public void delete(Long id, Long ipAddress) {
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
     *
     * @param clientTerminal
     */
    private void putClientTerminalMap(ClientTerminal clientTerminal) {
        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, clientTerminal.getIpAddress());
        ClientTerminalMap.put(clientTerminalKey, clientTerminal);
    }

    /**
     * 单条clientTerminal 删除clientTerminalMap缓存
     *
     * @param clientTerminal
     */
    private void delClientTerminalMap(ClientTerminal clientTerminal) {
        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, clientTerminal.getIpAddress());
        ClientTerminalMap.remove(clientTerminalKey);
    }

    protected ClientTerminalDto doBackward(ClientTerminal clientTerminal) {
        ClientTerminalDto dto = new ClientTerminalDto();
        BeanUtils.copyProperties(clientTerminal, dto);
        return dto;
    }
}
