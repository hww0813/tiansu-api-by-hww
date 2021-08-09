package com.yuanqing.project.tiansu.controller.assets;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.SaveType;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.redis.RedisCache;
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
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

import static com.yuanqing.common.constant.Constants.INDEX_CLIENT_COUNTS_CACHE;

/**
 * 客户端
 *
 * @author xucan
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/api/device/sip-client")
@CrossOrigin
@Api(value = "终端", description = "客户端相关Api")
public class ClientController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private IClientService clientService;

    @Autowired
    private IClientTerminalService clientTerminalService;

    @Autowired
    private RedisCache redisCache;


    @GetMapping("/list")
    @ApiOperation(value = "获取终端列表", httpMethod = "GET")
    public AjaxResult getAll(@ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                             @ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                             @ApiParam("地域ID")@RequestParam(value = "region[]", required = false) Integer regionId,
                             @ApiParam("状态")@RequestParam(value = "status", required = false) Integer status,
                             @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                             @ApiParam("用户名")@RequestParam(value = "username", required = false) String username,
                             @ApiParam("信令服务器ID")@RequestParam(value = "sipServerId", required = false) Long sipServerId,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue) {

        ClientTerminal clientTerminal = new ClientTerminal();
        clientTerminal.setStatus(status);
        clientTerminal.setIpAddress(IpUtils.ipToLong(ipAddress));
        clientTerminal.setDeviceCode(deviceCode);
        clientTerminal.setSipServerId(sipServerId);
        clientTerminal.setId(id);
        clientTerminal.setRegionId(regionId);

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = StringUtils.humpToUnderline(orderValue) + " " + orderType;
        }

        List<ClientTerminal> list = null;
        //判断 username 是否为空
        if (StringUtils.isNotEmpty(username)) {
            Client client = new Client();
            client.setUsername(username);

            // 根据用户名查询client列表  需要用IP
            List<Client> clientList = clientService.getList(client);

            startPage();
            list = clientTerminalService.getTerminalByClientList(clientList);

        } else {
            startPage();
            list = clientTerminalService.getListWithOrder(clientTerminal, orderStr);
        }

        //用户数
        List<ClientTerminalDto> clientTerminalDtoList = clientTerminalService.handleTerminalUserNum(list);
        if (clientTerminalDtoList == null) {
            clientTerminalDtoList = new ArrayList<>();
        }

        return AjaxResult.success(getDataTable(clientTerminalDtoList, list));
    }

    @GetMapping("/clientList")
    @ApiOperation(value = "获取客户端列表", httpMethod = "GET")
    public AjaxResult getAll(@ApiParam("IP地址")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                             @ApiParam("设备编码")@RequestParam(value = "deviceCode", required = false) String deviceCode,
                             @ApiParam("区域")@RequestParam(value = "region[]", required = false) Integer regionId,
                             @ApiParam("状态")@RequestParam(value = "status", required = false) Integer status,
                             @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                             @ApiParam("信令服务器ID")@RequestParam(value = "sipServerId", required = false) Long sipServerId,
                             @ApiParam("用户名")@RequestParam(value = "username", required = false) String username) {
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
        return AjaxResult.success(getDataTable(list));

    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "更新终端状态为已确认", httpMethod = "PUT")
    public AjaxResult updateStatus(@ApiParam("勾选的终端ID")@Valid @RequestBody JSONObject jsonObject) {
        String ids = String.valueOf(jsonObject.get("id"));
        String str = ids.substring(1, ids.length() - 1);
        String[] array = str.split(",");
        clientTerminalService.changStatus(array);
        return AjaxResult.success();
    }

    @PostMapping
    @ApiOperation(value = "新增终端", httpMethod = "POST")
    public AjaxResult postSipClient(@ApiParam("终端")@RequestBody ClientTerminal dto) {
        clientTerminalService.save(dto, SaveType.INSERT);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新客户端", httpMethod = "PUT")
    public AjaxResult putSipClient(@ApiParam("客户端")@Valid @RequestBody ClientTerminal dto) {
        clientTerminalService.save(dto, SaveType.UPDATE);
        return AjaxResult.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除客户端", httpMethod = "PUT")
    public AjaxResult deleteSipClient(@Valid @ApiParam("ID")@RequestParam(value = "id") Long id,
                                      @Valid @ApiParam("IP地址")@RequestParam(value = "ipAddress") Long ipAddress) {

        clientTerminalService.delete(id, ipAddress);
        return AjaxResult.success();
    }


    @GetMapping("/active")
    @ApiOperation(value = "活跃终端列表")
    public TableDataInfo getActiveClient() {
        startPage();
        List<ClientTerminal> activeTerminal = clientTerminalService.getActiveTerminal();

        List<ClientTerminalDto> dtoList = clientTerminalService.handleTerminalUserNum(activeTerminal);

        return getDataTable(dtoList,activeTerminal);
    }


    @GetMapping("/getByIpAndPort")
    @ApiOperation(value = "根据IP和端口获取客户端数据", httpMethod = "GET")
    public TableDataInfo getByIpAndPort(@ApiParam("客户端IP")@RequestParam(value = "serverIp", required = false) Long clientIp,
                                        @ApiParam("客户端端口")@RequestParam(value = "serverPort", required = false) Long clientPort) {

        Client client = new Client();
        client.setIpAddress(clientIp);
        client.setDomainPort(clientPort);
        startPage();
        List<Client> pageInfo = clientService.getList(client);
        return getDataTable(pageInfo);

    }


    @GetMapping("/getClientDatas")
    @ApiOperation(value = "获取客户端数据", httpMethod = "GET")
    public AjaxResult getClientDatas() {
        return AjaxResult.success("success", redisCache.getCacheObject(INDEX_CLIENT_COUNTS_CACHE));
    }

    @GetMapping("/getClientOperationTrend")
    @ApiOperation(value = "获取终端动作趋势图", httpMethod = "GET")
    public AjaxResult getClientOperationTrend(@ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp) throws ParseException {
        //ip地址转换
        Long ip = IpUtils.ipToLong(srcIp);
        //获得开始结束时间时间段
        Date endTime = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(endTime);
        c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 23);
        Date startTime = c.getTime();

        Map<Long, List<JSONObject>> map = clientService.getClientOperationTrend(ip, startTime, endTime);
        return AjaxResult.success(map);
    }


}
