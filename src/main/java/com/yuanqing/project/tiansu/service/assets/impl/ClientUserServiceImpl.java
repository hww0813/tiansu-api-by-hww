package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientUser;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientUserDto;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientUserMapper;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xucan on 2021-01-19 17:52
 *
 * @author xucan
 */

@Service
public class ClientUserServiceImpl implements IClientUserService {

    private static final Logger log = LoggerFactory.getLogger(ClientUserServiceImpl.class);

    @Autowired
    private ClientUserMapper clientUserMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private IClientService clientService;

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        ClientUser condClientUser = new ClientUser();
        condClientUser.setUsername(filters.getString("username"));
        condClientUser.setStatus(filters.getInteger("status"));

        Long ipAddress = IpUtils.ipToLong(filters.getString("ipAddress"));

        List<ClientUser> list = null;
        //判断 ipAddress 是否为空
        if (ipAddress != null) {
            Client client = new Client();
            client.setIpAddress(ipAddress);

            // 根据用户名查询client列表  需要用IP
            List<Client> clientList = clientService.getList(client);

            list = getClientUserByUsername(clientList);

        } else {
            list = getList(condClientUser);
        }

        //查询终端数量
        List<ClientUserDto> clientUserDtoList = handleClientUserTerminalNum(list);

        List<JSONObject> reportList = new ArrayList<JSONObject>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (ClientUserDto clientUserDto : clientUserDtoList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", clientUserDto.getUsername());

            if (clientUserDto.getTerminalCnt() != null) {
                jsonObject.put("ipCnt", String.valueOf(clientUserDto.getTerminalCnt()));
            }
            if (clientUserDto.getStatus() != null && 0 == clientUserDto.getStatus()) {
                jsonObject.put("status", "已确认");
            } else if (1 == clientUserDto.getStatus()) {
                jsonObject.put("status", "新发现");
            }
            if (clientUserDto.getUpdateTime() != null) {
                jsonObject.put("updateTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, clientUserDto.getUpdateTime()));
            }
            reportList.add(jsonObject);
        }
        return reportList;
    }

    @Override
    public boolean changStatus(String[] ids) {
        return clientUserMapper.changStatus(ids);
    }

    @Override
    public List<ClientUserDto> handleClientUserTerminalNum(List<ClientUser> clientUserList) {

        if (CollectionUtils.isEmpty(clientUserList)) {
            log.error("用户列表为空");
            return null;
        }
        //提取集合用户名
        List<String> usernameList = clientUserList.stream().map(f -> f.getUsername()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(usernameList)) {
            log.error("提取集合用户名,集合为空");
            return null;
        }

        List<JSONObject> originalTerminalNum = clientMapper.getTerminalNumByUserName(usernameList);

        if (CollectionUtils.isEmpty(originalTerminalNum)) {
            log.error("获取用户名对应的终端数，结果为空");
            return null;
        }

        Map<String, Integer> map = new HashMap<>();

        originalTerminalNum.stream().forEach(f -> map.put(f.getString("username"), f.getInteger("terminalCnt")));

        if (CollectionUtils.isEmpty(map)) {
            log.error("终端数量结果格式化异常,map为空");
            return null;
        }

        List<ClientUserDto> dtoList = new ArrayList<>();

        clientUserList.stream().forEach(f -> {
            ClientUserDto dto = doBackward(f);
            Integer cnt = map.get(f.getUsername());
            dto.setTerminalCnt(cnt == null ? 0 : cnt);
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public String readExcelFile(MultipartFile file) {
        return null;
    }

    @Override
    public boolean findClientUser(String username) {
        return false;
    }

    @Override
    public Long insert(ClientUser clientUser) {
        return clientUserMapper.insert(clientUser);
    }

    @Override
    public List<ClientUser> getActiveClientUser() {
        ClientUser clientUser = (ClientUser) DateUtils.getDayTime(ClientUser.class);
        return clientUserMapper.getList(clientUser);
    }

    @Override
    public List<ClientUser> getClientUserByUsername(List<Client> clientList) {
        List<ClientUser> clientUserList = null;
        if (!CollectionUtils.isEmpty(clientList)) {
            clientUserList = clientUserMapper.getClientUserByUsername(clientList);
        }
        return clientUserList;
    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") ClientUser clientUser, SaveType type) {
        if (type.getCode() == 0) {
            return clientUserMapper.update(clientUser);
        } else if (type.getCode() == 1) {
            return clientUserMapper.insert(clientUser);
        }
        return clientUser.getId();
    }

    @Override
    public Long save(ClientUser clientUser) {
        throw new CustomException("暂不支持这种保存方式,需要传入SaveType");
    }

    @Override
    public void deleteById(@Valid @NotNull(message = "根据ID删除的ID不能为空") Long id) {
        clientUserMapper.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id, String username) {
        ClientUser clientUser = findById(id);
        if (clientUser == null) {
            throw new CustomException("该用户不存在");
        }
        deleteById(id);
        clientMapper.deleteByUsername(username);
    }

    @Override
    public ClientUser findById(@Valid @NotNull(message = "根据ID查询的ID不能为空") Long id) {
        return clientUserMapper.findById(id);
    }

    @Override
    public List<ClientUser> getList(ClientUser clientUser) {
        return clientUserMapper.getList(clientUser);
    }


    protected ClientUserDto doBackward(ClientUser clientUser) {
        ClientUserDto dto = new ClientUserDto();
        BeanUtils.copyProperties(clientUser, dto);
        return dto;
    }
}
