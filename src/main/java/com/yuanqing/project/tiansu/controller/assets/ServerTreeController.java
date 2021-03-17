package com.yuanqing.project.tiansu.controller.assets;

import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xucan
 * @version 1.0
 * @Date 2021-02-24 10:20
 */

@RestController
@RequestMapping("/api/server")
@Api(value = "server服务器接口", description = "server服务器相关API")
public class ServerTreeController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ServerTreeController.class);

    @Resource
    private IServerTreeService serverTreeService;

    @Autowired
    private OperationBehaviorMapper operationBehaviorMapper;


    @GetMapping("/list")
    @ApiOperation(value = "获取服务器列表", httpMethod = "GET")
    public AjaxResult getAll(@RequestParam(value = "serverCode", required = false) String serverCode,
                             @RequestParam(value = "serverDomain", required = false) String serverDomain,
                             @RequestParam(value = "serverIp", required = false) Long serverIp,
                             @RequestParam(value = "deviceType", required = false) String deviceType,
                             @RequestParam(value = "isDelete", defaultValue = "1") Short isDelete) {
        ServerTree serverTree = new ServerTree();
        serverTree.setServerCode(serverCode);
        serverTree.setServerDomain(serverDomain);
        serverTree.setServerIp(serverIp);
        serverTree.setServerType(deviceType);
        serverTree.setIsDelete(isDelete);
        List<ServerTree> list = serverTreeService.getList(serverTree);
        return AjaxResult.success(getDataTable(list));
    }

    @GetMapping("/sessionServerList")
    @ApiOperation(value = "获取会话相关的服务器列表", httpMethod = "GET")
    public AjaxResult getSessionServerList(@RequestParam(value = "startDate", required = false) String startDate,
                                           @RequestParam(value = "endDate", required = false) String endDate,
                                           @RequestParam(value = "sessionId", required = false) Long sessionId,
                                           @RequestParam(value = "serverCode", required = false) String serverCode,
                                           @RequestParam(value = "serverIp", required = false) String serverIp) {

        ServerTree serverTree = new ServerTree();
        serverTree.setServerCode(serverCode);
        serverTree.setServerIp(IpUtils.ipToLong(serverIp));
        serverTree.setstartDate(startDate);
        serverTree.setendDate(endDate);

        if (sessionId == null) {
            log.error("sessionId为空");
            return null;
        }

        //根据会话ID查询相关操作行为dstIp
        List<Long> serverIpList = operationBehaviorMapper.getDstIpBySessionId(sessionId);

        startPage();
        List<ServerTree> sessionServerList = serverTreeService.getSessionServerList(serverTree, serverIpList);

        return AjaxResult.success(getDataTable(sessionServerList));
    }


    @GetMapping("/serverList")
    @ApiOperation(value = "获取服务器列表", httpMethod = "GET")
    public AjaxResult getServerNumber() {
        List<ServerTree> list = serverTreeService.getList(new ServerTree());
        return AjaxResult.success(list);
    }

    @GetMapping("/serverCode")
    @ApiOperation(value = "获取服务器编号", httpMethod = "GET")
    public AjaxResult getAllServerCode() {
        List<ServerTree> list = serverTreeService.getList(new ServerTree());
        List<ServerTree> list1 = list.stream().filter(f -> f.getServerCode() != null).collect(Collectors.toList());
        return AjaxResult.success(list1);
    }

    @GetMapping("/getByType")
    @ApiOperation(value = "根据类型查询服务器", httpMethod = "GET")
    public AjaxResult getByType(@RequestParam(value = "serverType", required = false) String serverType) {
        ServerTree serverTree = new ServerTree();
        serverTree.setServerType(serverType);
        List<ServerTree> list = serverTreeService.getList(serverTree);
        return AjaxResult.success(list);
    }


    //导入excel
    @PostMapping(value = "/import")
    @ApiOperation(value = "导入服务器列表")
    public Map<String, Object> importExcel(@RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        String AjaxResult = serverTreeService.readExcelFile(file);
        map.put("message", AjaxResult);
        return map;
    }


    @PostMapping
    @ApiOperation(value = "新增一个服务器", httpMethod = "POST")
    public AjaxResult postServerTree(@Valid @RequestBody ServerTree dto) {

        serverTreeService.save(dto);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新一个服务器", httpMethod = "PUT")
    public AjaxResult putServerTree(@Valid @RequestBody ServerTree dto) {
        serverTreeService.save(dto);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除一个服务器", httpMethod = "DELETE")
    public AjaxResult deleteServerTree(@PathVariable(value = "id") Long id) {
        if (id == null) {
            return AjaxResult.error();
        }
        serverTreeService.deleteById(id);
        return AjaxResult.success();
    }
}
