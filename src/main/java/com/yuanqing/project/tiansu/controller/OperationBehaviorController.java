package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.DateUtils;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.framework.web.domain.PageResult;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSearch;
import com.yuanqing.project.tiansu.job.IndexStatisticsTask;
import com.yuanqing.project.tiansu.mapper.assets.OperationBehaviorMapper;
import com.yuanqing.project.tiansu.service.video.IOperationBehaviorService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class OperationBehaviorController extends BaseController   {


    @Autowired
    private IOperationBehaviorService IOperationBehaviorService;

    @Autowired
    private IndexStatisticsTask indexStatisticsTask;


    @Resource
    private OperationBehaviorMapper operationBehaviorMapper;


    @GetMapping("/list")
    @ApiOperation(value = "获取操作行为列表", httpMethod = "GET")
    public PageResult getAll(@RequestParam(value = "pageNum", defaultValue = "1") int num,
                             @RequestParam(value = "pageSize", defaultValue = "20") int size,OperationBehavior operationBehavior) throws ExecutionException, InterruptedException {
            operationBehavior.setNum(num -1);
            operationBehavior.setSize(size);
            try {
                return IOperationBehaviorService.queryOperationList(operationBehavior);
            } catch (Exception e) {
               return  PageResult.error("获取操作行为接口报错！");
            }

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
    public PageResult findByEventId(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                @RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                                @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                                @RequestParam(value = "dstCode", required = false) String dstCode,
                                @RequestParam(value = "id", required = false) Long id,
                                @RequestParam(value = "dstDeviceIp", required = false) Long dstDeviceIp) {

        JSONObject filters = new JSONObject();

        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
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
    public PageResult getRealTimeList(OperationBehaviorSearch operationBehaviorSearch) throws Exception{
        CompletableFuture<List<OperationBehavior>> operationBehaviorsFuter = CompletableFuture.supplyAsync(()-> operationBehaviorMapper.getRealTimeBehaviorList(operationBehaviorSearch));
        return PageResult.success(operationBehaviorsFuter.get(5, TimeUnit.SECONDS));
    }



    @PutMapping
    @ApiOperation(value = "操作行为更新", httpMethod = "PUT")
    public PageResult putOperationBehavior(@Valid @RequestBody OperationBehavior dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return PageResult.error(500,"更新失败");
        }
        operationBehaviorMapper.updateOperationBehavior(dto);
        return PageResult.success();
    }


    @GetMapping("/cameraChart")
    @ApiOperation(value = "获取摄像头统计图", httpMethod = "GET")
    public AjaxResult getCameraChart(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                     @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                     @RequestParam(value = "cameraActionType", required = false) String action,
                                     @RequestParam(value = "sort",required = false) String sort){
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
    public AjaxResult getCilentChart(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                 @RequestParam(value = "clientActionType", required = false) String action,
                                 @RequestParam(value = "sort",required = false) String sort) {
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
    public AjaxResult getUserChart(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd" ) LocalDate startDate,
                               @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                               @RequestParam(value = "userActionType", required = false) String action,
                               @RequestParam(value = "sort") String sort) {
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
    public AjaxResult getCameraAnalysisDetail(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                          @RequestParam(value = "cameraId") Long cameraId,
                                          @RequestParam(value = "action", required = false) String action,
                                          @RequestParam(value = "stime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime stime,
                                          @RequestParam(value = "etime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime etime) throws ExecutionException, InterruptedException {
        JSONObject filters = new JSONObject();
        filters.put("num", pageNum - 1);
        filters.put("size", pageSize);
        filters.put("cameraId", cameraId);
        filters.put("action", action);
        filters.put("sTime", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + stime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        filters.put("eTime",  LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + etime.format(DateTimeFormatter.ISO_LOCAL_TIME));
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




}
