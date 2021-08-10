package com.yuanqing.project.tiansu.controller.operation;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.common.utils.StringUtils;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSearch;
import com.yuanqing.project.tiansu.mapper.operation.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.swing.text.DateFormatter;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Dong.Chao
 * @Classname OperationBehaviorController
 * @Description 操作行为相关
 * @Date 2021/1/28 15:07
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/api/operation/behavior")
@Api(value = "操作行为相关接口", description = "操作行为相关Api")
public class OperationBehaviorController extends BaseController   {


    @Autowired
    private IOperationBehaviorService IOperationBehaviorService;

    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;

    @Autowired
    private IServerTreeService serverTreeService;

    @Autowired
    private ICameraService cameraService;


    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为列表", httpMethod = "GET")
    public PageResult getAll(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                             @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                             @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String startDate,
                             @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate,
                             @ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                             @ApiParam("目的IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                             @ApiParam("源设备编码")@RequestParam(value = "srcCode", required = false) String srcCode,
                             @ApiParam("目的设备编码")@RequestParam(value = "dstCode", required = false) String dstCode,
                             @ApiParam("会话ID")@RequestParam(value = "sessionId", required = false) Long sessionId,
                             @ApiParam("客户端ID")@RequestParam(value = "clientId", required = false) Long clientId,
                             @ApiParam("摄像头ID")@RequestParam(value = "cameraId", required = false) Long cameraId,
                             @ApiParam("操作类型")@RequestParam(value = "action", required = false) String action,
                             @ApiParam("目的设备ip")@RequestParam(value = "dstDeviceIp", required = false) String dstDeviceIp,
                             @ApiParam("目的设备名")@RequestParam(value = "dstDeviceName", required = false) String dstDeviceName,
                             @ApiParam("内容")@RequestParam(value = "content", required = false) String content,
                             @ApiParam("用户名")@RequestParam(value = "username", required = false) String username,
                             @ApiParam("平台名称")@RequestParam(value = "probeHdInfo", required = false) String probeHdInfo,
                             @ApiParam("联接类型")@RequestParam(value = "connectType", required = false) String connectType,
                             @ApiParam("uuid")@RequestParam(value = "uuid", required = false) String uuid,
                             @ApiParam("排序")@RequestParam(required = false) String orderType,
                             @ApiParam("排序对象")@RequestParam(required = false) String orderValue){

            OperationBehavior operationBehavior = new OperationBehavior();
            operationBehavior.setNum(pageNum);
            operationBehavior.setSize(pageSize);
            operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
            operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
            operationBehavior.setstartDate(startDate);
            operationBehavior.setendDate(endDate);
            operationBehavior.setDstDeviceIp(IpUtils.ipToLong(dstDeviceIp));
            operationBehavior.setAction(action);
            operationBehavior.setUsername(username);
            operationBehavior.setOrderType(orderType);
            operationBehavior.setOrderValue(orderValue);
            operationBehavior.setDstCode(dstCode);
            operationBehavior.setSrcCode(srcCode);
            operationBehavior.setSessionId(sessionId);
            operationBehavior.setClientId(clientId);
            operationBehavior.setCameraId(cameraId);
            operationBehavior.setDstDeviceName(dstDeviceName);
            operationBehavior.setContent(content);
            operationBehavior.setConnectType(connectType);
            operationBehavior.setProbeHdInfo(probeHdInfo);
            operationBehavior.setUuid(uuid);

            try {
                return IOperationBehaviorService.queryOperationList(operationBehavior);
            } catch (Exception e) {
                e.printStackTrace();
               return  PageResult.error("获取操作行为接口报错！");
            }

    }

    @GetMapping("/getOperByUuid")
    @ApiOperation(value = "根据Uuid查询操作行为", httpMethod = "GET")
    public AjaxResult getOperByUuid(@ApiParam("uuid")@RequestParam(value = "uuid", required = false) String uuid){

        OperationBehavior operationBehavior = IOperationBehaviorService.getOperationBehaviorByUuid(uuid);
        if(StringUtils.isEmpty(operationBehavior.getDstDeviceName())){
            String dstCode = operationBehavior.getDstCode();
            String deviceName = cameraService.findByCode(dstCode).getDeviceName();
            if(StringUtils.isNotEmpty(deviceName)){
                operationBehavior.setDstDeviceName(deviceName);
            }
        }
        return AjaxResult.success(operationBehavior);

    }
    @GetMapping("/getOperationBehaviorById")
    @ApiOperation(value = "获取操作行为列表", httpMethod = "GET")
    public AjaxResult getOperationBehaviorById(@ApiParam("勾选的操作行为id")@RequestParam(value = "id", required = false) Long id) {

        OperationBehavior operationBehavior = IOperationBehaviorService.getOperationBehaviorById(id);
        return AjaxResult.success(operationBehavior);
    }

    /**
     * @author: dongchao
     * @create: 2021/2/1-15:51
     * @description: 不直接查库 ，查告警中心接口
     * @param:
     * @return:
     */
    @GetMapping("/findByEventId")
    @ApiOperation(value = "根据事件id获取操作行为列表", httpMethod = "GET")
    public PageResult findByEventId(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                @ApiParam("开始时间")@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                @ApiParam("结束时间")@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                @ApiParam("目的设备编码")@RequestParam(value = "dstCode", required = false) String dstCode,
                                @ApiParam("ID")@RequestParam(value = "id", required = false) Long id,
                                @ApiParam("目的设备IP")@RequestParam(value = "dstDeviceIp", required = false) Long dstDeviceIp) {

        JSONObject filters = new JSONObject();

        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("dstCode", dstCode);
        filters.put("id", id);
        filters.put("dstDeviceIp", dstDeviceIp);
      /*  PageInfo<OperationBehavior> pageInfo = operationBehaviorManager.findByEventId(pageNum, pageSize, filters);
        return ResultUtils.success(pageInfo);*/
      return null;
    }

    /**
     * @author: dongchao
     * @create: 2021/2/1-16:28
     * @description: 只获取了前三千条数据，最长反应时间5s
     * @param:
     * @return:
     */
    @GetMapping("/realTimeList")
    @ApiOperation(value = "获取实时操作行为列表", httpMethod = "GET")
    public PageResult getRealTimeList(@ApiParam("操作行为检索实体类")OperationBehaviorSearch operationBehaviorSearch) throws Exception{
        CompletableFuture<List<OperationBehavior>> operationBehaviorsFuter = CompletableFuture.supplyAsync(()-> operationBehaviorMapper.getRealTimeBehaviorList(operationBehaviorSearch));
        return PageResult.success(operationBehaviorsFuter.get(5, TimeUnit.SECONDS));
    }



    @PutMapping
    @ApiOperation(value = "操作行为更新", httpMethod = "PUT")
    public PageResult putOperationBehavior(@ApiParam("操作行为")@Valid @RequestBody OperationBehavior dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return PageResult.error(500,"更新失败");
        }
        operationBehaviorMapper.updateOperationBehavior(dto);
        return PageResult.success();
    }


    @GetMapping("/cameraChart")
    @ApiOperation(value = "获取摄像头统计图", httpMethod = "GET")
    public AjaxResult getCameraChart(@ApiParam("开始时间")@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @ApiParam("结束时间")@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                     @ApiParam("摄像头的操作类型")@RequestParam(value = "cameraActionType", required = false) String action,
                                     @ApiParam("排序")@RequestParam(value = "sort",required = false) String sort){
        //校验判断
        if (sort == null) {
            return AjaxResult.error(500, "未知排列顺序");
        }
        if (startDate == null){
            return AjaxResult.error(500,"未知开始时间");
        }
        if(endDate == null){
            return AjaxResult.error(500,"未知结束时间");
        }
       return  IOperationBehaviorService.getCharts(startDate,endDate,action,sort,"CAMERA");
    }


    /**
     * @author: dongchao
     * @create: 2021/2/5-13:55
     * @description:
     * @param:
     * @return:
     */
    @GetMapping("/clientChart")
    @ApiOperation(value = "获取客户端统计图", httpMethod = "GET")
    public AjaxResult getCilentChart(@ApiParam("开始时间")@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 @ApiParam("结束时间")@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                 @ApiParam("操作类型")@RequestParam(value = "clientActionType", required = false) String action,
                                 @ApiParam("排序")@RequestParam(value = "sort",required = false) String sort) {
        //校验判断
        if (sort == null) {
            return AjaxResult.error(500, "未知排列顺序");
        }
        if (startDate == null){
            return AjaxResult.error(500,"未知开始时间");
        }
        if(endDate == null){
            return AjaxResult.error(500,"未知结束时间");
        }

        return  IOperationBehaviorService.getCharts(startDate,endDate,action,sort,"CLIENT");
    }


    @GetMapping("/userChart")
    @ApiOperation(value = "获取用户统计图", httpMethod = "GET")
    public AjaxResult getUserChart(@ApiParam("开始时间")@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd" ) LocalDate startDate,
                               @ApiParam("结束时间")@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                               @ApiParam("操作类型")@RequestParam(value = "userActionType", required = false) String action,
                               @ApiParam("排序")@RequestParam(value = "sort") String sort) {
        //校验判断
        if (sort == null) {
            return AjaxResult.error(500, "未知排列顺序");
        }
        if (startDate == null){
            return AjaxResult.error(500,"未知开始时间");
        }
        if(endDate == null){
            return AjaxResult.error(500,"未知结束时间");
        }

        return  IOperationBehaviorService.getCharts(startDate,endDate,action,sort,"USER");
    }


    @GetMapping("/camera/relatedClient")
    @ApiOperation(value = "相关客户端", httpMethod = "GET")
    public AjaxResult getCameraAnalysisDetail(@ApiParam("页码数")@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @ApiParam("行数")@RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                          @ApiParam("摄像头ID")@RequestParam(value = "cameraId") Long cameraId,
                                          @ApiParam("操作类型")@RequestParam(value = "action", required = false) String action,
                                          @ApiParam("开始时间")@RequestParam(value = "startDate") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startDate,
                                          @ApiParam("结束时间")@RequestParam(value = "endDate") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endDate) throws ExecutionException, InterruptedException {
        JSONObject filters = new JSONObject();
        filters.put("num", pageNum - 1);
        filters.put("size", pageSize);
        filters.put("cameraId", cameraId);
        filters.put("action", action);
        filters.put("startDate", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + startDate.format(DateTimeFormatter.ISO_LOCAL_TIME));
        filters.put("endDate",  LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + endDate.format(DateTimeFormatter.ISO_LOCAL_TIME));
        //数据
        CompletableFuture<List<HashMap>> sessionClients = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.cameraAnalysisDetail(filters));
        //总量
        CompletableFuture<Integer> count = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.cameraAnalysisCount(filters));
        return AjaxResult.success(sessionClients.get(),pageSize,pageNum,count.get());
    }



    @GetMapping("/getCountByTime")
    @ApiOperation(value = "获取某天操作行为和原始信令总数", httpMethod = "GET")
    public AjaxResult getCountByTime() throws ExecutionException, InterruptedException {
        OperationBehavior operationBehaviorSearch = (OperationBehavior) DateUtils.getDayTime(OperationBehavior.class);
        CompletableFuture<Integer> countFuture = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.quertyOperationBehaviorCount(operationBehaviorSearch));
        CompletableFuture<Integer> countsFuture = CompletableFuture.supplyAsync(() -> operationBehaviorMapper.queryRawCount(operationBehaviorSearch));
        HashMap hashMap = new HashMap();
        hashMap.put("count",countFuture.get());
        hashMap.put("counts",countsFuture.get());
        return AjaxResult.success(hashMap);
    }

    @GetMapping("/getProbeName")
    @ApiOperation(value = "获取探针和机器码" ,httpMethod = "GET")
    public AjaxResult getProbeName(){
        List<JSONObject> probeNameList = serverTreeService.getProbeName();
        return AjaxResult.success(probeNameList);
    }
}


