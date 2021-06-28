package com.yuanqing.project.tiansu.controller.assets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.constant.TokenConstants;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import com.yuanqing.project.tiansu.service.feign.PmcFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
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

    @Resource
    private PmcFeignClient pmcFeignClient;

//    @Value("${tiansu.pmchost}")
//    private String prefix;


    @GetMapping("/list")
    @ApiOperation(value = "获取服务器列表", httpMethod = "GET")
    public AjaxResult getAll(@ApiParam("服务器编号")@RequestParam(value = "serverCode", required = false) String serverCode,
                             @ApiParam("服务器域名")@RequestParam(value = "serverDomain", required = false) String serverDomain,
                             @ApiParam("服务器IP")@RequestParam(value = "serverIp", required = false) Long serverIp,
                             @ApiParam("设备类型")@RequestParam(value = "deviceType", required = false) String deviceType,
                             @ApiParam("是否删除")@RequestParam(value = "isDelete", defaultValue = "1") Short isDelete) {
        ServerTree serverTree = new ServerTree();
        serverTree.setServerCode(serverCode);
        serverTree.setServerDomain(serverDomain);
        serverTree.setServerIp(serverIp);
        serverTree.setServerType(deviceType);
        serverTree.setIsDelete(isDelete);
        startPage();
        List<ServerTree> list = serverTreeService.getList(serverTree);

//        String result = HttpUtils.getHttpRequest(prefix + "/pmc/consul/getConsulIp");
        String result = pmcFeignClient.getConsulIp(TokenConstants.PMC_TOKEN);
        if (StringUtils.isNotEmpty(result)) {
            List<String> ipList = new ArrayList<>();
            JSONObject syslogJson = JSON.parseObject(result);
            ipList = (List<String>) syslogJson.get("data");
            //serverTree的remark字段用于标记是否可以查看服务器资源性能详情，yes：表示可以，no:表示不可以
            for (ServerTree server : list) {
                if (ipList != null && ipList.contains(IpUtils.longToIPv4(server.getServerIp()))) {
                    server.setRemark("yes");
                } else {
                    server.setRemark("no");
                }
            }
        }else{
            //若请求不到这个接口的数据，则每个服务器的详情都不能访问
            list.stream().forEach(h->{
                h.setRemark("no");
            });
        }

        return AjaxResult.success(getDataTable(list));
    }

    @GetMapping("/sessionServerList")
    @ApiOperation(value = "获取会话相关的服务器列表", httpMethod = "GET")
    public AjaxResult getSessionServerList(@ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String startDate,
                                           @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate,
                                           @ApiParam("会话ID")@RequestParam(value = "sessionId", required = false) Long sessionId,
                                           @ApiParam("服务器编号")@RequestParam(value = "serverCode", required = false) String serverCode,
                                           @ApiParam("服务器IP")@RequestParam(value = "serverIp", required = false) String serverIp
    ) {

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
    public AjaxResult getByType(@ApiParam("服务器类型")@RequestParam(value = "serverType", required = false) String serverType) {
        ServerTree serverTree = new ServerTree();
        serverTree.setServerType(serverType);
        List<ServerTree> list = serverTreeService.getList(serverTree);
        return AjaxResult.success(list);
    }


    //导入excel
    @PostMapping(value = "/import")
    @ApiOperation(value = "导入服务器列表")
    public Map<String, Object> importExcel(@ApiParam("服务器列表EXCEL")@RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        String AjaxResult = serverTreeService.readExcelFile(file);
        map.put("message", AjaxResult);
        return map;
    }


    @PostMapping
    @ApiOperation(value = "新增一个服务器", httpMethod = "POST")
    public AjaxResult postServerTree(@ApiParam("服务器")@Valid @RequestBody ServerTree dto) {

        serverTreeService.save(dto);
        return AjaxResult.success();
    }

    @PutMapping
    @ApiOperation(value = "更新一个服务器", httpMethod = "PUT")
    public AjaxResult putServerTree(@ApiParam("服务器")@Valid @RequestBody ServerTree dto) {
        serverTreeService.save(dto);
        return AjaxResult.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除一个服务器", httpMethod = "DELETE")
    public AjaxResult deleteServerTree(@ApiParam("勾选的服务器ID")@PathVariable(value = "id") Long id) {
        if (id == null) {
            return AjaxResult.error();
        }
        serverTreeService.deleteById(id);
        return AjaxResult.success();
    }
}
