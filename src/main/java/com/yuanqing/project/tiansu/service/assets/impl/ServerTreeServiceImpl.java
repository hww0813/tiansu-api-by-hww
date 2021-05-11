package com.yuanqing.project.tiansu.service.assets.impl;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.Constants;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.exception.CustomException;
import com.yuanqing.common.queue.ServerTreeMap;
import com.yuanqing.common.utils.SequenceIdGenerator;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.http.HttpUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.report.ReadServerExcel;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.assets.ClientMapper;
import com.yuanqing.project.tiansu.mapper.assets.ClientTerminalMapper;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.mapper.assets.ServerTreeMapper;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xucan on 2021-01-19 10:29
 *
 * @author xucan
 */

@Service
@Transactional(readOnly = false)
public class ServerTreeServiceImpl implements IServerTreeService {

    private static final Logger log = LoggerFactory.getLogger(ServerTreeServiceImpl.class);

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientTerminalMapper clientTerminalMapper;

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;

    @Autowired
    private ServerTreeMapper serverTreeMapper;

    @Autowired
    private ICameraService cameraService;

    private static SequenceIdGenerator serverIdGenerator = new SequenceIdGenerator(1, 1);

    @Override
    public ServerTree findOne(Long serverIp) {
        List<ServerTree> list = serverTreeMapper.findOne(serverIp);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public List<JSONObject> getAllToReport(JSONObject filters) {
        ServerTree condServerTree = new ServerTree();
        condServerTree.setServerCode(filters.getString("serverCode"));
        condServerTree.setServerDomain(filters.getString("serverDomain"));
        condServerTree.setServerIp(IpUtils.ipToLong(filters.getString("serverIp")));
        condServerTree.setServerType(filters.getString("deviceType"));
        condServerTree.setIsDelete(filters.getShort("isDelete"));
        List<ServerTree> serverTreeList = serverTreeMapper.getList(condServerTree);

        List<JSONObject> reportList = new ArrayList<JSONObject>();
        if (!CollectionUtils.isEmpty(serverTreeList)) {
            for (ServerTree serverTree : serverTreeList) {
                JSONObject jsonObject = new JSONObject();
                if (!StringUtils.isEmpty(serverTree.getServerCode())) {
                    jsonObject.put("serverCode", serverTree.getServerCode());
                }else{
                    jsonObject.put("serverCode", "");
                }
                if (!StringUtils.isEmpty(serverTree.getServerName())) {
                    jsonObject.put("serverName", serverTree.getServerName());
                } else {
                    jsonObject.put("serverName", "");
                }
                if (serverTree.getServerIp() != null) {
                    jsonObject.put("serverIp", IpUtils.longToIPv4(serverTree.getServerIp()));
                }else{
                    jsonObject.put("serverIp", "");
                }
                if (!StringUtils.isEmpty(serverTree.getServerDomain())) {
                    jsonObject.put("serverDomain", serverTree.getServerDomain());
                }else{
                    jsonObject.put("serverDomain", "");
                }
                if (!StringUtils.isEmpty(serverTree.getServerType())) {
                    jsonObject.put("serverType", DeviceType.getLabel(serverTree.getServerType()));
                }else{
                    jsonObject.put("serverType", "");
                }
                reportList.add(jsonObject);
            }
        }
        return reportList;
    }


    @Override
    public List<ServerTree> getSessionServerList(ServerTree serverTree, List<Long> serverIpList) {

        if (CollectionUtils.isEmpty(serverIpList)) {
            log.error("服务器IP集合为空");
            return null;
        }
        List<ServerTree> sessionServerList = serverTreeMapper.getServerByServerIpList(serverIpList, serverTree);

        return sessionServerList;
    }


    @Override
    public String readExcelFile(MultipartFile file) {
        String result = "";
        //创建处理EXCEL的类
        ReadServerExcel readExcel = new ReadServerExcel();
        int count = 0;
        //解析excel，获取上传的事件单
        List<Map<String, Object>> serverList = readExcel.getExcelInfo(file);
        //至此已经将excel中的数据转换到list里面了,接下来就可以操作list,可以进行保存到数据库,或者其他操作,

        for (Map<String, Object> server : serverList) {
            ServerTree serverTree = new ServerTree();
            try {
                serverTree.setServerCode(server.get("servercode").toString());
            } catch (NullPointerException e) {
            }
            try {
                serverTree.setServerName(server.get("serverName").toString());
            } catch (NullPointerException e) {
            }
            try {
                serverTree.setServerIp(IpUtils.ipToLong(server.get("serverIp").toString()));
            } catch (NullPointerException e) {
            }
            try {
                serverTree.setServerDomain(server.get("serverDomain").toString());
            } catch (NullPointerException e) {
                serverTree.setServerDomain("");
            }
            try {
                if (server.get("serverType") != null && server.get("serverType").equals("信令服务器")) {
                    serverTree.setServerType(DeviceType.CENTER_CONTROL_SERVER.getValue());
                } else {
                    serverTree.setServerType(DeviceType.OTHER.getValue());
                }
            } catch (NullPointerException e) {
                serverTree.setServerType(DeviceType.OTHER.getValue());
            }

            try {

                Long id = this.save(serverTree);

                if (id > 0) {
                    count++;
                    result = "上传成功";
                } else {
                    result = "上传失败";
                }
            } catch (Exception e) {
                result = "上传失败";
                log.error(e.getMessage());
            }

             /*int ret=cameraMapper.insertCamera(camera1);
             if(ret == 0){
                 result = "插入数据库失败";
             }*/
        }
//        if (cameraList != null && !cameraList.isEmpty()) {
        if (count > 0) {
            result = "上传成功";
        } else {
            result = "上传失败";
        }
        return result;
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
        throw new CustomException("暂不支持这种保存方式");
    }


//    @Override
//    public void insert(ServerTree serverTree) {
//        Long IP = serverTree.getServerIp();
//        //根据ip查找客户端
//        List<Client> clientList = clientMapper.findByIP(IP);
//
//        //根据ip查找终端,先缓存中查找
//        String clientTerminalKey = String.format(Constants.TWO_FORMAT, Constants.CLIENT_TERMINAL, IP);
//        ClientTerminal clientTerminal = ClientTerminalMap.get(clientTerminalKey);
//
//        //缓存中没找到终端，在数据库中进行查找
//        if (clientTerminal == null) {
//            clientTerminal = clientTerminalMapper.findByIpAddress(IP);
//            //将数据放入缓存中
//            if (clientTerminal != null) {
//                ClientTerminalMap.put(clientTerminalKey, clientTerminal);
//            }
//        }
//        //如果存在客户端/终端，则更新对应终端/客户端的设备类型
//        handleClient(clientList, clientTerminal);
//
//        //入数据库
//        serverTreeMapper.insertServerTree(serverTree);
//        //入缓存
//        putServerTreeMap(serverTree, IP);
//    }

    @Override
    public Long save(@Valid @NotNull(message = "保存或更新的实体不能为空") ServerTree entity, SaveType type) {
        throw new CustomException("暂不支持这种保存方式,无需SaveType");
    }

    @Override
    public Long save(ServerTree serverTree) {
        Long IP = serverTree.getServerIp();

        //根据ip查找客户端
        List<Client> clientList = clientMapper.findByIP(IP);
        //根据ip查找终端
        ClientTerminal clientTerminal = clientTerminalMapper.findByIpAddress(IP);

        //如果存在客户端/中毒案，则更新对应终端/客户端的设备类型
        handleClient(clientList, clientTerminal);

        ServerTree server = findOne(IP);

        //入库
        if (serverTree.getId() == null && server == null) {
            serverTree.setId(serverIdGenerator.nextId());
            serverTree.setIsDelete(Short.valueOf("1"));
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
     *
     * @param serverTree
     */
    private void putServerTreeMap(ServerTree serverTree) {
        String serverKey = String.format(Constants.TWO_FORMAT, Constants.SERVER_TREE, serverTree.getServerIp());
        ServerTreeMap.put(serverKey, serverTree);
    }

    private void putServerTreeMap(ServerTree serverTree, Long ip) {
        String serverKey = String.format(Constants.TWO_FORMAT, Constants.SERVER_TREE, ip);
        ServerTreeMap.put(serverKey, serverTree);
    }


    /**
     * 如果客户端不为空，则更新对应客户端的设备类型
     * 如果终端不为空，则更新对应终端的设备类型
     *
     * @param clientList     客户端
     * @param clientTerminal 终端
     */
    private void handleClient(List<Client> clientList, ClientTerminal clientTerminal) {
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
