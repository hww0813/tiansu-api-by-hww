package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.page.TableDataInfo;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ClientTerminal;
import com.yuanqing.project.tiansu.domain.assets.dto.ClientTerminalDto;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IClientTerminalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 客户端
 *
 * @author xucan
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/api/device/sip-client")
@CrossOrigin
@Api(value = "客户端", description = "客户端相关Api")
public class ClientController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private IClientService clientService;

    @Autowired
    private IClientTerminalService clientTerminalService;


    @GetMapping("/list")
    @ApiOperation(value = "获取终端列表", httpMethod = "GET")
    public TableDataInfo getAll(@RequestParam(value = "ipAddress", required = false) String ipAddress,
                                @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                @RequestParam(value = "region[]", required = false) Integer regionId,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "sipServerId", required = false) Long sipServerId) {
        ClientTerminal clientTerminal = new ClientTerminal();
        clientTerminal.setStatus(status);
        clientTerminal.setIpAddress(IpUtils.ipToLong(ipAddress));
        clientTerminal.setDeviceCode(deviceCode);
        clientTerminal.setSipServerId(sipServerId);
        clientTerminal.setId(id);
        clientTerminal.setRegionId(regionId);

        startPage();
        List<ClientTerminal> list = clientTerminalService.getList(clientTerminal);

        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(list);

        return getDataTable(dtoList);
    }

    @GetMapping("/clientList")
    @ApiOperation(value = "获取客户端列表", httpMethod = "GET")
    public TableDataInfo getAll(@RequestParam(value = "ipAddress", required = false) String ipAddress,
                                @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                @RequestParam(value = "region[]", required = false) Integer regionId,
                                @RequestParam(value = "status", required = false) Integer status,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                @RequestParam(value = "username", required = false) String username) {
        Client client = new Client();
        client.setStatus(status);
        client.setIpAddress(IpUtils.ipToLong(ipAddress));
        client.setDeviceCode(deviceCode);
        client.setSipServerId(sipServerId);
        client.setId(id);
        client.setRegionId(regionId);
        client.setUsername(username);

        startPage();
        List<Client> list = clientService.getList(client);
        return getDataTable(list);

    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新终端状态为已确认", httpMethod = "PUT")
    public AjaxResult updateStatus(@Valid @RequestBody JSONObject jsonObject) {
        String ids = String.valueOf(jsonObject.get("id"));
        String str = ids.substring(1, ids.length() - 1);
        String[] array = str.split(",");
        clientTerminalService.changStatus(array);
        return AjaxResult.success();
    }

    @PostMapping
    @ApiOperation(value = "新增终端", httpMethod = "POST")
    public AjaxResult postSipClient(@RequestBody ClientTerminal dto) {
        clientTerminalService.save(dto, SaveType.INSERT);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新客户端", httpMethod = "PUT")
    public AjaxResult putSipClient(@Valid @RequestBody ClientTerminal dto) {
        clientTerminalService.save(dto,SaveType.UPDATE);
        return AjaxResult.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除客户端", httpMethod = "PUT")
    public AjaxResult deleteSipClient(@Valid @RequestParam(value = "id") Long id,
                                      @Valid @RequestParam(value = "ipAddress") Long ipAddress) {

        clientTerminalService.deleteById(id);
        clientService.deleteByIpAddress(ipAddress);
        return AjaxResult.success();
    }

    /**
     * 拆分
     *
     * @return
     */
    @GetMapping("/active")
    @ApiOperation(value = "活跃终端列表")
    public TableDataInfo getActiveClient() {

        startPage();
        List<ClientTerminal> activeTerminal = clientTerminalService.getActiveTerminal();

        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(activeTerminal);

        return getDataTable(dtoList);
    }

//    @GetMapping("/getClientDatas")
//    @ApiOperation(value = "获取首页客户端数据", httpMethod = "GET")
//    public AjaxResult getClientDatas() {
//        return AjaxResult.success(homePageManager.findObject("client","client_num"));
//    }


    @GetMapping("/getByIpAndPort")
    @ApiOperation(value = "根据IP和端口获取客户端数据", httpMethod = "GET")
    public TableDataInfo getByIpAndPort(@RequestParam(value = "serverIp", required = false) Long serverIp,
                                        @RequestParam(value = "serverPort", required = false) Long serverPort) {

        Client client = new Client();
        client.setIpAddress(serverIp);
        client.setDomainPort(serverPort);
        startPage();
        List<Client> pageInfo = clientService.getList(client);
        return getDataTable(pageInfo);
    }


}
