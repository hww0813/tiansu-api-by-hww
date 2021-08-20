package com.yuanqing.project.tiansu.controller.report;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.ActionType;
import com.yuanqing.common.enums.EventStatusEnum;
import com.yuanqing.common.utils.*;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.common.utils.spring.SpringContextUtil;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.controller.analysis.CameraVisitedController;
import com.yuanqing.project.tiansu.domain.analysis.CameraVisit;
import com.yuanqing.project.tiansu.domain.analysis.Statistics;
import com.yuanqing.project.tiansu.domain.analysis.dto.CameraVisitDto;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.event.Event;
import com.yuanqing.project.tiansu.domain.operation.OperationBehavior;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.domain.report.ExcelData;
import com.yuanqing.project.tiansu.domain.report.VisitRate;
import com.yuanqing.project.tiansu.service.analysis.IStatisticsService;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.assets.*;
import com.yuanqing.project.tiansu.service.feign.AlarmFeignClient;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorService;
import com.yuanqing.project.tiansu.service.operation.IOperationBehaviorSessionService;
import com.yuanqing.project.tiansu.service.operation.IRawNetFlowService;
import com.yuanqing.project.tiansu.service.operation.IRawSignalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 摄像头被访问统计报表
 *
 * @author jqchu
 * @version 1.0
 * @since 2017.12.23
 */
@RestController
@RequestMapping(value = "/api/reports")
@CrossOrigin
@Api(value = "报表", description = "报表导出")
public class ReportController extends BaseController {

    @Value("${tiansu.default.max-rows}")
    private int MAXROWS = 10000;

    public static Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    private static final String RATE = "classpath:/static/reports/Rate.jrxml";

    private static final String EVENT_REPORT_NAME = "classpath:/static/reports/EventDetail.jrxml";

    private static final String SIP_DEVICE_REPORT_NAME = "classpath:/static/reports/SipDevice.jrxml";

    private static final String SIP_CLIENT_REPORT_NAME = "classpath:/static/reports/SipClient.jrxml";

    private static final String SIP_USER_REPORT_NAME = "classpath:/static/reports/SipUser.jrxml";

    private static final String SIP_SERVER_REPORT_NAME = "classpath:/static/reports/SipServer.jrxml";

    private static final String OPERATION_BEHAVIOR_SESSION_REPORT_NAME = "classpath:/static/reports/OperationBehaviorSession.jrxml";

    private static final String OPERATION_BEHAVIOR_REPORT_NAME = "classpath:/static/reports/OperationBehavior.jrxml";

    private static final String RAW_SIGNAL_REPORT_NAME = "classpath:/static/reports/RawSignal.jrxml";

    private static final String RAW_NET_FLOW_REPORT_NAME = "classpath:/static/reports/RawNetFlow.jrxml";

    private static final String REMOTE_RECORD_REPORT_NAME = "classpath:/static/reports/RemoteRecord.jrxml";

    private static final String SELF_AUDIT_REPORT_NAME = "classpath:/static/reports/SelfAudit.jrxml";

    private static final String LGOIN_LOG_REPORT_NAME = "classpath:/static/reports/LoginLog.jrxml";

    private static final String FAIL_ACCESS_REPORT_NAME = "classpath:/static/reports/FailAccess.jrxml";

    private static final String ANALYSIS_CLIENT = "classpath:/static/reports/AnalysisClient.jrxml";

    private static final String ANALYSIS_CLIENT_DETAIL = "classpath:/static/reports/AnalysisClientDetail.jrxml";

    private static final String ANALYSIS_CLIENT_RELATED_CAMERA = "classpath:/static/reports/AnalysisClientRelatedCamera.jrxml";

    private static final String ANALYSIS_CAMERA = "classpath:/static/reports/AnalysisCamera.jrxml";

    private static final String ANALYSIS_CAMERA_DETAIL = "classpath:/static/reports/AnalysisCameraDetail.jrxml";

    private static final String ANALYSIS_CAMERA_RELATED_CLIENT = "classpath:/static/reports/AnalysisCameraRelatedClient.jrxml";

    private static final String RATE_CAMERA_CNT = "classpath:/static/reports/RateCameraCnt.jrxml";

    private static final String RATE_VISITED_CNT = "classpath:/static/reports/RateVisitedCnt.jrxml";

    private static final String RATE_VISIT_CNT = "classpath:/static/reports/RateVisitCnt.jrxml";

    private static final String RATE_CLIENT_CNT = "classpath:/static/reports/RateClientCnt.jrxml";

//    @Value("${tiansu.alarmhost}")
//    private String alarmHost;

    @Resource
    private IMacsConfigService macsConfigService;

    @Resource
    private ReportUtil reportUtil;

    @Resource
    private IVisitRateService visitRateService;

//    @Resource
//    private EventDetailManager eventDetailManager;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IClientService clientService;

    @Resource
    private IClientTerminalService clientTerminalService;

    @Resource
    private IClientUserService clientUserService;

    @Resource
    private IRawSignalService rawSignalService;

    @Resource
    private IStatisticsService statisticsService;

//
//    @Resource
//    private CameraDirectVisitedManager cameraDirectVisitedManager;

    @Resource
    private IServerTreeService serverTreeService;

    @Resource
    private IOperationBehaviorSessionService operationBehaviorSessionService;

    @Resource
    private IOperationBehaviorService operationBehaviorService;

    @Resource
    private AlarmFeignClient alarmFeignClient;

    @Autowired
    private IRawNetFlowService busiRawNetFlowService;

//    @Resource
//    private RawSignalManager rawSignalManager;
//
//    @Resource
//    private RawNetFlowManager rawNetFlowManager;
//
//    @Resource
//    private RemoteRecordManager remoteRecordManager;
//
//    @Resource
//    private SelfAuditManager selfAuditManager;
//
//    @Resource
//    private FailAccessManager failAccessManager;
//
//    @Resource
//    private LoginLogManager loginLogManager;


    @GetMapping(value = "/sendReport")
    public AjaxResult sendReport(@ApiParam("时间要素") @RequestParam(value = "type", required = false) String type) {
        try {
            reportUtil.generateReport(type);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(404, e.getMessage());
        }
        return AjaxResult.success();
    }


    @GetMapping(value = "/analysis/visit/rate")
    public void getCameraVisitRateReport(@ApiParam("城市代码") @RequestParam(value = "cityCode", required = false) String cityCode,
                                         @ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                         @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                         @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("cityCode", cityCode);  // 无用参数
        if (startDate != null) {
            filters.put("startDate", startDate.toLocalDate());
        }
        if (endDate != null) {
            filters.put("endDate", endDate.toLocalDate());
        }

        if ("xlsx".equals(format)) {
            try {
                this.CameraVisitRateExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RATE);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<VisitRate> list = visitRateService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("访问率统计报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "/eventDetail")
    public void getEventReport(@ApiParam("开始时间") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                               @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                               @ApiParam("事件来源") @RequestParam(value = "eventSource", required = false) String eventSource,
                               @ApiParam("策略名称") @RequestParam(value = "strategyName", required = false) String strategyName,
                               @ApiParam("状态") @RequestParam(value = "status", required = false) String status,
                               @ApiParam("告警类别") @RequestParam(value = "eventCategory", required = false) String eventCategory,
                               @ApiParam("事件等级") @RequestParam(value = "eventLevel", required = false) String eventLevel,
                               @ApiParam("IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                               @ApiParam("摄像头名称") @RequestParam(value = "cameraName", required = false) String cameraName,
                               @ApiParam("内容") @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "eventSubject", required = false) String eventSubject,
                               @ApiParam("规则名称") @RequestParam(value = "ruleName", required = false) String ruleName,
                               @ApiParam("操作类型") @RequestParam(value = "action", required = false) String action,
                               @ApiParam("id") @RequestParam(value = "id", required = false) Long id,
                               @ApiParam("联接类型") @RequestParam(value = "connectType", required = false) String connectType,
                               @ApiParam("排序") @RequestParam(required = false) String orderType,
                               @ApiParam("排序对象") @RequestParam(required = false) String orderValue,
                               @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        Event event = new Event();
        event.setStartTime(stime);
        event.setEndTime(etime);
        event.setEventSource(eventSource);
        event.setEventCategory(eventCategory);
        event.setEventLevel(eventLevel);
        event.setCameraName(cameraName);
        event.setId(id);


        if(StringUtils.isNotEmpty(status)){
            event.setStatus(Long.parseLong(status));
        }
        if(StringUtils.isNotEmpty(clientIp)){
            event.setClientIp(Long.parseLong(clientIp));
        }
        if(StringUtils.isNotEmpty(action)){
            event.setAction(Long.parseLong(action));
        }

        if (StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderValue)) {
            event.setOrderType(orderValue + " " + orderType);
        }

        String result = alarmFeignClient.listT(event, null, null);
        JSONObject resultObj = JSONObject.parseObject(result);
        JSONArray datas = resultObj.getJSONArray("rows");

        if ("xlsx".equals(format)) {
            try {
                this.eventExcel(response, datas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(EVENT_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = datas.toJavaList(JSONObject.class);
                list.stream().forEach(h -> {
                    h.put("clientIp", IpUtils.longToIp(h.getLong("clientIp")));
                    h.put("status", EventStatusEnum.getLabel(h.getLong("status")));
                    h.put("action", ActionType.getLabel(h.getString("action")));
                });
                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("告警事件报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }

    }

    @GetMapping(value = "/sipDevice")
    public void getSipDiveceReport(@ApiParam("状态") @RequestParam(value = "status", required = false) Integer status,
                                   @ApiParam("是否国标") @RequestParam(value = "isGb", required = false) Integer isGb,
                                   @ApiParam("设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
                                   @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @ApiParam("区域ID") @RequestParam(value = "regionId", required = false) Integer regionId,
                                   @ApiParam("IP地址") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @ApiParam("排序") @RequestParam(required = false) String orderType,
                                   @ApiParam("排序对象") @RequestParam(required = false) String orderValue,
                                   @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        Camera camera = new Camera();
        camera.setStatus(status);
        camera.setIsGb(isGb);
        camera.setDeviceName(deviceName);
        camera.setRegion(regionId);
        camera.setDeviceCode(deviceCode);
        camera.setIpAddress(IpUtils.ipToLong(ipAddress));

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = orderValue + " " + orderType;
        }

        List<Camera> getList = cameraService.getListWithOrder(camera, orderStr);

        macsConfigService.setLowerRegionByCamera(getList);

        List<JSONObject> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        getList.stream().forEach(f -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceCode", f.getDeviceCode());
            jsonObject.put("ipAddress", IpUtils.longToIp(f.getIpAddress()));
            jsonObject.put("deviceName", f.getDeviceName());
            jsonObject.put("regionName", f.getRegionName());
            jsonObject.put("status", EnumTransfer.eventStatus(f.getStatus()));
            jsonObject.put("updateTime", sdf.format(f.getUpdateTime()));
            list.add(jsonObject);
        });

        if ("xlsx".equals(format)) {
            try {
                CameraExcel(response, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(SIP_DEVICE_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("摄像头报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipClient")
    public void getSipClientReport(@ApiParam("IP地址") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @ApiParam("区域代码") @RequestParam(value = "region", required = false) String region,
                                   @ApiParam("状态") @RequestParam(value = "status", required = false) String status,
                                   @ApiParam("信令服务器ID") @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                   @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("status", status);
        filters.put("ipAddress", ipAddress);
        filters.put("deviceCode", deviceCode);
        filters.put("sipServerId", sipServerId);
        if (region != null && !region.equals("")) {
            String[] regionList = region.split(",");
            if (regionList.length == 1) {
                String provinceRegion1 = region.substring(2, 6);
                if (!provinceRegion1.equals("0000")) {
                    String provinceRegion = region.substring(0, 2);
                    filters.put("provinceRegion", provinceRegion);
                }
            }
            if (regionList.length == 2) {
                String cityRegion = regionList[1].substring(0, 4);
                filters.put("cityRegion", cityRegion);
            }
            if (regionList.length == 3) {
                String countryRegion = regionList[2].substring(0, 6);
                filters.put("countryRegion", countryRegion);
            }
        }
        if ("xlsx".equals(format)) {
            try {
                ClientExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(SIP_CLIENT_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = clientTerminalService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);
                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("终端报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipUser")
    public void getSipUserReport(@ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
                                 @ApiParam("状态") @RequestParam(value = "status", required = false) String status,
                                 @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format,
                                 @ApiParam("排序") @RequestParam(required = false) String orderType,
                                 @ApiParam("排序对象") @RequestParam(required = false) String orderValue, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("status", status);
        filters.put("username", username);
        // TODO: 好像没接过来
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            filters.put("orderStr", StringUtils.humpToUnderline(orderValue) + " " + orderType);
        }

        if ("xlsx".equals(format)) {
            try {
                ClientUserExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(SIP_USER_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = clientUserService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("终端用户报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipServer")
    public void getSipServerReport(@ApiParam("服务器编码") @RequestParam(value = "serverCode", required = false) String serverCode,
                                   @ApiParam("服务器域") @RequestParam(value = "serverDomain", required = false) String serverDomain,
                                   @ApiParam("服务器IP") @RequestParam(value = "serverIp", required = false) Long serverIp,
                                   @ApiParam("设备类型") @RequestParam(value = "deviceType", required = false) String deviceType,
                                   @ApiParam("是否删除") @RequestParam(value = "isDelete", defaultValue = "1") Short isDelete,
                                   @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("serverCode", serverCode);
        filters.put("serverDomain", serverDomain);
        filters.put("serverIp", serverIp);
        filters.put("serverTypeValue", deviceType);
        filters.put("isDelete", isDelete);
        if ("xlsx".equals(format)) {
            try {
                ServerTreeExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(SIP_SERVER_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = serverTreeService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = null;
                try {
                    print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
                } catch (JRException e) {
                    e.printStackTrace();
                }

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("服务器资源报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/operationBehaviorSession")
    public void getoperationBehaviorSessionReport(@ApiParam("开始时间") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                  @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                                                  @ApiParam("会话ID") @RequestParam(value = "sessionId", required = false) Long sessionId,
                                                  @ApiParam("源编码") @RequestParam(value = "srcCode", required = false) String srcCode,
                                                  @ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
                                                  @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        if (startDate != null) {
            filters.put("stime", startDate);
        }
        if (endDate != null) {
            filters.put("etime", endDate);
        }
        filters.put("sessionId", sessionId);
        filters.put("srcCode", srcCode);
        filters.put("username", username);
        if ("xlsx".equals(format)) {
            try {
                operationBehaviorSessionExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(OPERATION_BEHAVIOR_SESSION_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();

                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = operationBehaviorSessionService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = null;
                try {
                    print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
                } catch (JRException e) {
                    e.printStackTrace();
                }

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("行为会话报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/operationBehavior")
    public void getOperationBehaviorReport(@ApiParam("开始时间") @RequestParam(value = "startDate", required = false) String stime,
                                           @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) String etime,
                                           @ApiParam("源IP") @RequestParam(value = "srcIp", required = false) String srcIp,
                                           @ApiParam("目的IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                           @ApiParam("目的编码") @RequestParam(value = "dstCode", required = false) String dstCode,
                                           @ApiParam("操作类型") @RequestParam(value = "action", required = false) String action,
                                           @ApiParam("目的设备名称") @RequestParam(value = "dstDeviceName", required = false) String dstDeviceName,
                                           @ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
                                           @ApiParam("排序") @RequestParam(required = false) String orderType,
                                           @ApiParam("排除对象") @RequestParam(required = false) String orderValue,
                                           @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setstartDate(stime);
        operationBehavior.setendDate(etime);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
        operationBehavior.setDstCode(dstCode);
        operationBehavior.setAction(action);
        operationBehavior.setDstDeviceName(dstDeviceName);
        operationBehavior.setUsername(username);
        if (StringUtils.isNotBlank(orderType) && StringUtils.isNotBlank(orderValue)) {
            operationBehavior.setOrderType(orderType + " " + orderValue);
        }

        List<JSONObject> list = operationBehaviorService.getAllToReport(operationBehavior);

        if ("xlsx".equals(format)) {
            try {
                operationBehaviorExcel(response, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(OPERATION_BEHAVIOR_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();

                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                //参数
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }
                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("操作行为报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/OperationSignal")
    public void getOperationSignalReport(@ApiParam("开始时间") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                         @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                                         @ApiParam("源IP") @RequestParam(value = "srcIp", required = false) String srcIp,
                                         @ApiParam("目的IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                         @ApiParam("联接类型") @RequestParam(value = "connectType", required = false) String connectType,
                                         @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject filters = new JSONObject();
        if (startDate != null) {
            filters.put("startDate", sdf.format(startDate));
        }
        if (endDate != null) {
            filters.put("endDate", sdf.format(endDate));
        }
        filters.put("connectType", connectType);
        filters.put("srcIp", srcIp);
        filters.put("dstIp", dstIp);

        if ("xlsx".equals(format)) {
            try {
                rawSignalExcel(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RAW_SIGNAL_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = rawSignalService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("原始信令报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "/RawNetFlow")
    public void getRawNetFlowReport(@ApiParam("源ip") @RequestParam(value = "srcIp", required = false) String srcIp,
                                    @ApiParam("目的ip") @RequestParam(value = "dstIp", required = false) String dstIp,
                                    @ApiParam("开始时间") @RequestParam(value = "startDate", required = false) String startDate,
                                    @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) String endDate,
                                    @ApiParam("排序") @RequestParam(required = false) String orderType,
                                    @ApiParam("排序对象") @RequestParam(required = false) String orderValue,
                                    @ApiParam("格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        RawNetFlow rawNetFlow = new RawNetFlow();
        rawNetFlow.setSrcIp(IpUtils.ipToLong(srcIp));
        rawNetFlow.setDstIp(IpUtils.ipToLong(dstIp));
        rawNetFlow.setstartDate(startDate);
        rawNetFlow.setendDate(endDate);
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            rawNetFlow.setOrderType(orderValue + " " + orderType);
        }
        List<RawNetFlow> rawNetFlows = busiRawNetFlowService.selectBusiRawNetFlowList(rawNetFlow);

        List<JSONObject> list = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        rawNetFlows.stream().forEach(j -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("srcIp", IpUtils.longToIp(j.getSrcIp()));
            jsonObject.put("srcPort", j.getSrcPort());
            jsonObject.put("dstIp", IpUtils.longToIp(j.getDstIp()));
            jsonObject.put("dstPort", j.getDstPort());
            jsonObject.put("packetSize", j.getPacketSize());
            jsonObject.put("packetCount", j.getPacketCount());
            jsonObject.put("stamp", df.format(j.getStamp()));
            list.add(jsonObject);
        });

        if ("xlsx".equals(format)) {
            try {
                rawNetFlowExcel(response, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RAW_NET_FLOW_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();

                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                //参数
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = null;
                try {
                    print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
                } catch (JRException e) {
                    e.printStackTrace();
                }

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("xlsx".equals(format)) {
                    exporter = new JRXlsxExporter();
                    response.setContentType("application/vnd.ms-excel");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("流量列表报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


//    @GetMapping(value = "/RemoteRecord")
//    public void getRemoteRecordReport(@ApiParam("页码数")@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                      @ApiParam("页码数")@RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                      @ApiParam("页码数")@RequestParam(value = "srcIp", required = false) String srcIp,
//                                      @ApiParam("页码数")@RequestParam(value = "dstIp", required = false) String dstIp,
//                                      @ApiParam("页码数")@RequestParam(value = "remoteType", required = false) String remoteType,
//                                      @ApiParam("页码数")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        if (stime != null) {
//            filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        if (etime != null) {
//            filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        filters.put("srcIp", srcIp);
//        filters.put("dstIp", dstIp);
//        filters.put("remoteType", remoteType);
//        if ("xlsx".equals(format)) {
//            try {
//                remoteRecordExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(REMOTE_RECORD_REPORT_NAME);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = remoteRecordManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//
//                JRAbstractExporter exporter = null;
//
//                if ("pdf".equals(format)) {
//                    exporter = new JRPdfExporter();
//                    response.setContentType("application/pdf");
//                } else if ("xlsx".equals(format)) {
//                    exporter = new JRXlsxExporter();
//                    response.setContentType("application/vnd.ms-excel");
//                } else if ("docx".equals(format)) {
//                    exporter = new JRDocxExporter();
//                    response.setContentType("application/msword");
//                } else if ("html".equals(format)) {
//                    exporter = new HtmlExporter();
//                    response.setContentType("application/html");
//                } else {
//                    throw new RuntimeException("参数错误");
//                }
//
//                //设置输入项
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //设置输出项
//                if ("html".equals(format)) {
//                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//
//                    exporter.setExporterOutput(htmlExportOutput);
//                } else {
//                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                            response.getOutputStream());
//                    exporter.setExporterOutput(exporterOutput);
//
//                }
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("远程访问报表." + format, "utf-8"));
//
//                exporter.exportReport();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    @GetMapping(value = "/selfaudit")
//    public void getSelfAuditReport(
//            @ApiParam("页码数")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @ApiParam("页码数")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @ApiParam("页码数")@RequestParam(value = "result", required = false) String result,
//            @ApiParam("页码数")@RequestParam(value = "type", required = false) String type,
//            @ApiParam("页码数")@RequestParam(value = "severity", required = false) Integer severity,
//            @ApiParam("页码数")@RequestParam(value = "ip", required = false) String ip,
//            @ApiParam("页码数")@RequestParam(value = "username", required = false) String username,
//            @ApiParam("页码数")@RequestParam(value = "format", required = false) String format,
//            HttpServletResponse response, HttpServletRequest request) {
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("result", result);
//        filters.put("type", type);
//        filters.put("ip", ip);
//        filters.put("severity", severity);
//        filters.put("username", username);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.selfAuditExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(SELF_AUDIT_REPORT_NAME);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = selfAuditManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//
//                JRAbstractExporter exporter = null;
//
//                if ("pdf".equals(format)) {
//                    exporter = new JRPdfExporter();
//                    response.setContentType("application/pdf");
//                } else if ("xlsx".equals(format)) {
//                    exporter = new JRXlsxExporter();
//                    response.setContentType("application/vnd.ms-excel");
//                } else if ("docx".equals(format)) {
//                    exporter = new JRDocxExporter();
//                    response.setContentType("application/msword");
//                } else if ("html".equals(format)) {
//                    exporter = new HtmlExporter();
//                    response.setContentType("application/html");
//                } else {
//                    throw new RuntimeException("参数错误");
//                }
//
//                //设置输入项
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //设置输出项
//                if ("html".equals(format)) {
//                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//
//                    exporter.setExporterOutput(htmlExportOutput);
//                } else {
//                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                            response.getOutputStream());
//                    exporter.setExporterOutput(exporterOutput);
//
//                }
//
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("自审信息报表." + format, "utf-8"));
//
//                exporter.exportReport();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

//    @GetMapping(value = "/FailAccess")
//    public void getFailAccessReport(@ApiParam("页码数")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                    @ApiParam("页码数")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                    @ApiParam("页码数")@RequestParam(value = "srcIp", required = false) String srcIp,
//                                    @ApiParam("页码数")@RequestParam(value = "dstIp", required = false) String dstIp,
//                                    @ApiParam("页码数")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("srcIp", srcIp);
//        filters.put("dstIp", dstIp);
//
//        if ("xlsx".equals(format)) {
//            try {
//                failAccessExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(FAIL_ACCESS_REPORT_NAME);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = failAccessManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//
//                JRAbstractExporter exporter = null;
//
//                if ("pdf".equals(format)) {
//                    exporter = new JRPdfExporter();
//                    response.setContentType("application/pdf");
//                } else if ("xlsx".equals(format)) {
//                    exporter = new JRXlsxExporter();
//                    response.setContentType("application/vnd.ms-excel");
//                } else if ("docx".equals(format)) {
//                    exporter = new JRDocxExporter();
//                    response.setContentType("application/msword");
//                } else if ("html".equals(format)) {
//                    exporter = new HtmlExporter();
//                    response.setContentType("application/html");
//                } else {
//                    throw new RuntimeException("参数错误");
//                }
//
//                //设置输入项
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //设置输出项
//                if ("html".equals(format)) {
//                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//
//                    exporter.setExporterOutput(htmlExportOutput);
//                } else {
//                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                            response.getOutputStream());
//                    exporter.setExporterOutput(exporterOutput);
//
//                }
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("访问失败报表." + format, "utf-8"));
//
//                exporter.exportReport();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

    @GetMapping(value = "analysis/client/visit")
    public void getAnalysisClientReport(@ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        @ApiParam("客户端IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                                        @ApiParam("区域代码") @RequestParam(value = "region[]", required = false) String[] region,
                                        @ApiParam("客户端编码") @RequestParam(value = "clientCode", required = false) String clientCode,
                                        @ApiParam("操作类型") @RequestParam(value = "action", required = false) String action,
                                        @ApiParam("摄像头ID") @RequestParam(value = "cameraId", required = false) Long cameraId,
                                        @ApiParam("客户端ID") @RequestParam(value = "clientId", required = false) String clientId,
                                        @ApiParam("用户") @RequestParam(value = "user", required = false) String user,
                                        @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {


        JSONObject filters = new JSONObject();
        if (startDate != null) {
            filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (startDate != null) {
            filters.put("endDate", endDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        filters.put("clientIp", clientIp);
        filters.put("clientCode", clientCode);
        filters.put("action", action);
        filters.put("cameraId", cameraId);
        filters.put("clientId", clientId);
        filters.put("user", user);
        filters = RegionUtil.setRegion(filters, region);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisClientExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CLIENT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = statisticsService.getClientVisitToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("终端访问报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/client/visit/cnt")
    public void getAnalysisClientDetailReport(@ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                              @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                              @ApiParam("客户端ID") @RequestParam(value = "clientId", required = false) Long clientId,
                                              @ApiParam("摄像头ID") @RequestParam(value = "cameraId", required = false) Long cameraId,
                                              @ApiParam("目的IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                              @ApiParam("目的编码") @RequestParam(value = "dstCode", required = false) String dstCode,
                                              @ApiParam("操作类型") @RequestParam(value = "action", required = false) String action,
                                              @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("clientId", clientId);
        filters.put("cameraId", cameraId);
        filters.put("dstIp", dstIp);
        filters.put("dstCode", dstCode);
        filters.put("action", action);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisClientDetailExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CLIENT_DETAIL);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = clientService.getClientVisitDetailToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("终端访问次数报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/client/visit/relatedCamera")
    public void getAnalysisClientRelatedCameraReport(@ApiParam("客户端ID") @RequestParam(value = "clientId", required = false) Long clientId,
                                                     @ApiParam("操作类型") @RequestParam(value = "action", required = false) Integer action,
                                                     @ApiParam("摄像头编码") @RequestParam(value = "cameraCode", required = false) String cameraCode,
                                                     @ApiParam("摄像头名称") @RequestParam(value = "cameraName", required = false) String cameraName,
                                                     @ApiParam("摄像头IP") @RequestParam(value = "cameraIp", required = false) String cameraIp,
                                                     @ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
                                                     @ApiParam("客户端IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                                                     @ApiParam("区域代码") @RequestParam(value = "region", required = false) Integer region,
                                                     @ApiParam("开始时间") @RequestParam(value = "startDate", required = false) String startDate,
                                                     @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) String endDate,
                                                     @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("clientId", clientId);
        filters.put("action", action);
        filters.put("cameraCode", cameraCode);
        filters.put("cameraName", cameraName);
        filters.put("cameraIp", cameraIp);
        filters.put("username", username);
        filters.put("clientIp", clientIp);
        filters.put("region", region);
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);

        List<JSONObject> list = statisticsService.getClientVisitRelateCameraToReport(filters);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisClientRelatedCameraExcelReport(response, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CLIENT_RELATED_CAMERA);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();

                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);
                }
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("终端访问相关摄像头报表." + format, "utf-8"));
                exporter.exportReport();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/camera/visited")
    public void getAnalysisCameraReport(@ApiParam("动作类型")@RequestParam(value = "action",required = false) Integer action,
                                        @ApiParam("摄像头IP")@RequestParam(value = "cameraIp",required = false) String cameraIp,
                                        @ApiParam("摄像头实体")Camera camera,
                                        @ApiParam("排序")@RequestParam(required = false) String orderType,
                                        @ApiParam("排序对象")@RequestParam(required = false) String orderValue,
                                        @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        CameraVisit cameraVisit = new CameraVisit();
        cameraVisit.setAction(action);

        cameraVisit.setstartDate(camera.getstartDate());
        cameraVisit.setendDate(camera.getendDate());

        camera.setIpAddress(IpUtils.ipToLong(cameraIp));

        String orderStr = null;
        if (!StringUtils.isEmpty(orderType) && !StringUtils.isEmpty(orderValue)) {
            orderStr = orderValue + " " + orderType;
        }


        List<JSONObject> list = statisticsService.getCameraVisitedToReport(cameraVisit,camera,orderStr);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisCameraExcelReport(response, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CAMERA);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("摄像头被访问报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

//    @GetMapping(value = "analysis/camera/visit/cnt")
//    public void getAnalysisCameraReport( @ApiParam("开始时间")@RequestParam(value = "startDate", required = false) String  startDate,
//                                         @ApiParam("结束时间")@RequestParam(value = "endDate", required = false) String endDate,
//                                         @ApiParam("客户端ID")@RequestParam(value = "clientId", required = false) Long clientId,
//                                         @ApiParam("摄像头ID")@RequestParam(value = "cameraId", required = false) Long cameraId,
//                                         @ApiParam("源IP")@RequestParam(value = "srcIp", required = false) String srcIp,
//                                         @ApiParam("目的设备编码")@RequestParam(value = "deviceCode", required = false) String dstCode,
//                                         @ApiParam("用户名")@RequestParam(value = "username", required = false) String username,
//                                         @ApiParam("源设备编码")@RequestParam(value = "srcCode", required = false) String srcCode,
//                                         @ApiParam("动作类型")@RequestParam(value = "action", required = false) String action,
//                                        @ApiParam("格式")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        OperationBehavior operationBehavior = new OperationBehavior();
//        operationBehavior.setClientId(clientId);
//        operationBehavior.setCameraId(cameraId);
//        operationBehavior.setDstCode(srcCode);
//        operationBehavior.setAction(action);
//        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
//        operationBehavior.setUsername(username);
//        operationBehavior.setstartDate(startDate);
//        operationBehavior.setDstCode(dstCode);
//        operationBehavior.setendDate(endDate);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisCameraDetailExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CAMERA_DETAIL);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = cameraVisitedManager.getCameraVisitedCntToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//
//                JRAbstractExporter exporter = null;
//
//                if ("pdf".equals(format)) {
//                    exporter = new JRPdfExporter();
//                    response.setContentType("application/pdf");
//                } else if ("docx".equals(format)) {
//                    exporter = new JRDocxExporter();
//                    response.setContentType("application/msword");
//                } else if ("html".equals(format)) {
//                    exporter = new HtmlExporter();
//                    response.setContentType("application/html");
//                } else {
//                    throw new RuntimeException("参数错误");
//                }
//
//                //设置输入项
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //设置输出项
//                if ("html".equals(format)) {
//                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//
//                    exporter.setExporterOutput(htmlExportOutput);
//                } else {
//                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                            response.getOutputStream());
//                    exporter.setExporterOutput(exporterOutput);
//
//                }
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("摄像头被访问次数报表." + format, "utf-8"));
//
//                exporter.exportReport();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @GetMapping(value = "analysis/camera/visited/relatedClient")
    public void getAnalysisCameraRelatedClientReport(@ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                                     @ApiParam("操作类型") @RequestParam(value = "action", required = false) Integer action,
                                                     @ApiParam("开始时间") @RequestParam(value = "startDate", required = false) String startDate,
                                                     @ApiParam("结束时间") @RequestParam(value = "endDate", required = false) String endDate,
                                                     @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        Statistics statistics = new Statistics();

        statistics.setstartDate(startDate);
        statistics.setendDate(endDate);
        statistics.setAction(action);
        statistics.setDstCode(deviceCode);

        List<JSONObject> statisticsList = statisticsService.getClientList(statistics);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisCameraRelatedClientExcelReport(response, statisticsList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CAMERA_RELATED_CLIENT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(statisticsList);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("摄像头被访问相关终端报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "analysis/visit/rate/cameraCnt")
    public void getRateCameraCntReport(@ApiParam("区域代码") @RequestParam(value = "region", required = false) Integer region,
                                       @ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                       @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                       @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @ApiParam("设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
                                       @ApiParam("IP地址") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @ApiParam("状态") @RequestParam(value = "status", required = false) String status,
                                       @ApiParam("制造厂商") @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                       @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("deviceCode", deviceCode);
        filters.put("deviceName", deviceName);
        filters.put("ipAddress", ipAddress);
        filters.put("status", status);
        filters.put("manufacturer", manufacturer);
        filters.put("region", region);
        // 不用时间，就是查区域全部的摄像头
//        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        if ("xlsx".equals(format)) {
            try {
                this.getRateCameraCntExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RATE_CAMERA_CNT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateCameraCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("访问率相关摄像头总数报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/visitedCnt")
    public void getRateVisitCntReport(@ApiParam("区域代码") @RequestParam(value = "region", required = false) Integer region,
                                      @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                      @ApiParam("设备名称") @RequestParam(value = "deviceName", required = false) String deviceName,
                                      @ApiParam("IP地址") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                      @ApiParam("状态") @RequestParam(value = "status", required = false) String status,
                                      @ApiParam("制造厂商") @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                      @ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                      @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                      @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("manufacturer", manufacturer);
        filters.put("deviceCode", deviceCode);
        filters.put("deviceName", deviceName);
        filters.put("ipAddress", ipAddress);
        filters.put("status", status);
        filters.put("region", region);
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        if ("xlsx".equals(format)) {
            try {
                this.getRateVisitedCntExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RATE_VISITED_CNT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateVisitedCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("访问率被访问摄像头个数报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/visitCnt")
    public void getAnalysisCameraReport(@ApiParam("区域代码") @RequestParam(value = "region", required = false) Integer region,
                                        @ApiParam("源IP") @RequestParam(value = "srcIp", required = false) String srcIp,
                                        @ApiParam("目的IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                        @ApiParam("客户端ID") @RequestParam(value = "clientId", required = false) String clientId,
                                        @ApiParam("摄像头ID") @RequestParam(value = "cameraId", required = false) String cameraId,
                                        @ApiParam("操作类型") @RequestParam(value = "action", required = false) String action,
                                        @ApiParam("目的编码") @RequestParam(value = "dstCode", required = false) String dstCode,
                                        @ApiParam("用户名") @RequestParam(value = "username", required = false) String username,
                                        @ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                        @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                        @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("region", region);
        filters.put("srcIp", srcIp);
        filters.put("dstIp", dstIp);
        filters.put("clientId", clientId);
        filters.put("cameraId", cameraId);
        filters.put("action", action);
        filters.put("dstCode", dstCode);
        filters.put("username", username);
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        if ("xlsx".equals(format)) {
            try {
                this.getRateVisitCntExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RATE_VISIT_CNT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateVisitCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("访问率访问次数报表." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/clientCnt")
    public void getRateClientCntReport(@ApiParam("区域代码") @RequestParam(value = "region", required = false) Integer region,
                                       @ApiParam("设备编码") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @ApiParam("状态") @RequestParam(value = "status", required = false) Integer status,
                                       @ApiParam("IP地址") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @ApiParam("信令服务器ID") @RequestParam(value = "sipServerId", required = false) String sipServerId,
                                       @ApiParam("开始时间") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                       @ApiParam("结束时间") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                       @ApiParam("导出格式") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("region", region);
        filters.put("deviceCode", deviceCode);
        filters.put("status", status);
        filters.put("ipAddress", ipAddress);
        filters.put("sipServerId", sipServerId);
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if ("xlsx".equals(format)) {
            try {
                this.getRateClientCntExcelReport(response, filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RATE_CLIENT_CNT);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //编译报表
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //参数
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateClientCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print文件
                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);

                JRAbstractExporter exporter = null;

                if ("pdf".equals(format)) {
                    exporter = new JRPdfExporter();
                    response.setContentType("application/pdf");
                } else if ("docx".equals(format)) {
                    exporter = new JRDocxExporter();
                    response.setContentType("application/msword");
                } else if ("html".equals(format)) {
                    exporter = new HtmlExporter();
                    response.setContentType("application/html");
                } else {
                    throw new RuntimeException("参数错误");
                }

                //设置输入项
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //设置输出项
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("访问率相关终端报表".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

//    @GetMapping(value = "/logingLog")
//    public void getLoginLogReport(
//            @ApiParam("页码数")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @ApiParam("页码数")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @ApiParam("页码数")@RequestParam(value = "ipaddr", required = false) String ipaddr,
//            @ApiParam("页码数")@RequestParam(value = "userName", required = false) String userName,
//            @ApiParam("页码数")@RequestParam(value = "status", required = false) Integer status,
//            @ApiParam("页码数")@RequestParam(value = "format", required = false) String format,
//            HttpServletResponse response, HttpServletRequest request) {
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("ipaddr", ipaddr);
//        filters.put("userName", userName);
//        filters.put("status", status);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.loginLogExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(LGOIN_LOG_REPORT_NAME);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = loginLogManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//
//                JRAbstractExporter exporter = null;
//
//                if ("pdf".equals(format)) {
//                    exporter = new JRPdfExporter();
//                    response.setContentType("application/pdf");
//                } else if ("xlsx".equals(format)) {
//                    exporter = new JRXlsxExporter();
//                    response.setContentType("application/vnd.ms-excel");
//                } else if ("docx".equals(format)) {
//                    exporter = new JRDocxExporter();
//                    response.setContentType("application/msword");
//                } else if ("html".equals(format)) {
//                    exporter = new HtmlExporter();
//                    response.setContentType("application/html");
//                } else {
//                    throw new RuntimeException("参数错误");
//                }
//
//                //设置输入项
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //设置输出项
//                if ("html".equals(format)) {
//                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//
//                    exporter.setExporterOutput(htmlExportOutput);
//                } else {
//                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
//                            response.getOutputStream());
//                    exporter.setExporterOutput(exporterOutput);
//
//                }
//
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("登录日志报表." + format, "utf-8"));
//
//                exporter.exportReport();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

    public void getRateClientCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = visitRateService.getRateClientCntToReport(filters);
        reportTemplete("访问率相关终端报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"IP地址", "MAC地址","状态","最后更新时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("ipAddress"));
                row.add(j.get("macAddress"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void getRateVisitCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = visitRateService.getRateVisitCntToReport(filters);
        reportTemplete("访问率访问次数报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"终端IP", "摄像头IP","摄像头编号","摄像头名称","动作","动作详情","登录账号","时间","结果"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("dstIp"));
                row.add(j.get("dstCode"));
                row.add(j.get("dstDeviceName"));
                row.add(j.get("action"));
                row.add(j.get("actionDetail"));
                row.add(j.get("username"));
                row.add(j.get("stamp"));
                row.add(j.get("result"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    public void getRateVisitedCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = visitRateService.getRateVisitedCntToReport(filters);
        reportTemplete("访问率被访问摄像头个数报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"摄像头编号", "摄像头名称","摄像头IP","设备厂商","所在地区","状态","最后更新时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("deviceCode"));
                row.add(j.get("deviceName"));
                row.add(j.get("ipAddress"));
                row.add(j.get("manufacturer"));
                row.add(j.get("region"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    public void getRateCameraCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = visitRateService.getRateCameraCntToReport(filters);
        reportTemplete("访问率相关摄像头总数报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"摄像头编号", "摄像头名称","摄像头IP","设备厂商","所在地区","状态","最后更新时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("deviceCode"));
                row.add(j.get("deviceName"));
                row.add(j.get("ipAddress"));
                row.add(j.get("manufacturer"));
                row.add(j.get("region"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void getAnalysisCameraRelatedClientExcelReport(HttpServletResponse response, List<JSONObject> statisticsList) throws Exception {
        reportTemplete("摄像头被访问相关终端报表.xlsx", response, statisticsList, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"终端IP", "登录用户","访问次数"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("username"));
                row.add(j.get("count"));
                dataList.add(row);
            }
            return dataList;
        });
    }

//    private void getAnalysisCameraDetailExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("摄像头被访问次数");
//        List<JSONObject> all = cameraVisitedManager.getCameraVisitedCntToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备编号");
//        titles.add("源设备IP");
//        titles.add("源设备端口");
//        titles.add("源设备MAC");
//        titles.add("用户名");
//        titles.add("上行流量");
//        titles.add("下行流量");
//        titles.add("动作");
//        titles.add("动作详情");
//        titles.add("结果");
//        titles.add("时间");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("srcCode"));
//            row.add(j.get("srcIp").toString());
//            row.add(j.get("srcPort"));
//            row.add(j.get("srcMac"));
//            row.add(j.get("username"));
//            row.add(j.get("upFlow"));
//            row.add(j.get("downFlow"));
//            row.add(j.get("action"));
//            row.add(j.get("actionDetail"));
//            row.add(j.get("result"));
//            row.add(j.get("stamp"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "摄像头被访问次数报表.xlsx", data);
//    }

    private void getAnalysisCameraExcelReport(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("摄像头被访问报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"摄像头编号", "摄像头IP","摄像头名称", "所在地区", "操作类型","操作次数","相关终端数"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("CAMERA_CODE"));
                row.add(j.get("CAMERA_IP"));
                row.add(j.get("CAMERA_NAME"));
                row.add(j.get("CAMERA_REGION"));
                row.add(j.get("ACTION"));
                row.add(j.get("VISITED_CNT"));
                row.add(j.get("CLIENT_CNT"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void getAnalysisClientRelatedCameraExcelReport(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("终端访问相关摄像头报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"摄像头编号", "摄像头IP","摄像头名称", "所在地区", "被访问次数"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("CAMERA_CODE"));
                row.add(j.get("CAMERA_IP"));
                row.add(j.get("CAMERA_NAME"));
                row.add(j.get("CAMERA_REGION"));
                row.add(j.get("VISITED_CNT"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void getAnalysisClientDetailExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = statisticsService.getClientVisitCntToReport(filters);
        reportTemplete("终端访问次数报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"摄像头编号", "摄像头名称","摄像头IP", "摄像头端口", "动作","动作详情","时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("dstCode"));
                row.add(j.get("dstDeviceName"));
                row.add(j.get("dstIp"));
                row.add(j.get("dstPort"));
                row.add(j.get("action"));
                row.add(j.get("actionDetail"));
                row.add(j.get("stamp"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    private void getAnalysisClientExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = statisticsService.getClientVisitToReport(filters);
        reportTemplete("终端访问报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"终端IP", "用户名", "操作类型", "访问次数", "相关摄像头"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("CLIENT_IP"));
                row.add(j.get("USERNAME"));
                row.add(j.get("ACTION"));
                row.add(j.get("VISIT_CNT"));
                row.add(j.get("CAMERA_CNT"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void rawSignalExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = rawSignalService.getAllToReport(filters);

        reportTemplete("原始信令报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"源设备IP", "目的设备IP", "目的设备编码", "内容", "时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("dstIp"));
                row.add(j.get("toCode"));
                row.add(j.get("content"));
                row.add(j.get("stamp"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    private void rawNetFlowExcel(HttpServletResponse response, List<JSONObject> list) throws Exception {
        reportTemplete("流量列表报表.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"源IP", "源端口", "目的IP", "目的端口", "包大小","包数量","时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("srcPort"));
                row.add(j.get("dstIp"));
                row.add(j.get("dstPort"));
                row.add(j.get("packetSize"));
                row.add(j.get("packetCount"));
                row.add(j.get("stamp"));
                dataList.add(row);
            }
            return dataList;
        });

    }
//
//    private void remoteRecordExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("远程访问");
//        List<JSONObject> all = remoteRecordManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备IP");
//        titles.add("源端口");
//        titles.add("源MAC");
//        titles.add("目的设备IP");
//        titles.add("目的端口");
//        titles.add("目的MAC");
//        titles.add("时间");
//        titles.add("远程访问类型");
//        titles.add("通信包大小");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("srcIp"));
//            row.add(j.get("srcPort"));
//            row.add(j.get("srcMac"));
//            row.add(j.get("dstIp"));
//            row.add(j.get("dstPort"));
//            row.add(j.get("dstMac"));
//            row.add(j.get("stamp"));
//            row.add(j.get("remoteType"));
//            row.add(j.get("packetSize"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "远程访问报表.xlsx", data);
//    }

    public void operationBehaviorSessionExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = operationBehaviorSessionService.getAllToReport(filters);

        reportTemplete("行为会话报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"源设备IP", "登录帐号", "开始时间", "最近活跃时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("username"));
                row.add(j.get("startTime"));
                row.add(j.get("activeTime"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    public void operationBehaviorExcel(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("操作行为报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"终端IP", "摄像头IP", "摄像头编号", "摄像头名称", "动作", "动作详细", "登录帐号", "时间", "平台名称"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("dstIp"));
                row.add(j.get("dstCode"));
                row.add(j.get("dstDeviceName"));
                row.add(j.get("action"));
                row.add(j.get("actionDetail"));
                row.add(j.get("username"));
                row.add(j.get("stamp"));
                row.add(j.get("platformName"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    public void ServerTreeExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = serverTreeService.getAllToReport(filters);

        reportTemplete("服务器资源报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"服务器名称", "服务器IP", "服务器域名", "服务器类型"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("serverName"));
                row.add(j.get("serverIp"));
                row.add(j.get("serverDomain"));
                row.add(j.get("serverType"));
                dataList.add(row);
            }
            return dataList;
        });

    }


    public void CameraExcel(HttpServletResponse response, List<JSONObject> list) throws Exception {
        reportTemplete("摄像头报表.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"设备编号", "IP地址", "摄像头名称", "所在地区", "状态", "首次更新时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("deviceCode"));
                row.add(j.get("ipAddress"));
                row.add(j.get("deviceName"));
                row.add(j.get("regionName"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    public void ClientExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        List<JSONObject> all = clientTerminalService.getAllToReport(filters);

        reportTemplete("终端报表.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"IP地址", "MAC地址", "用户数", "状态", "首次更新时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("ipAddress"));
                row.add(j.get("macAddress"));
                row.add(j.get("usercnt"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    public void ClientUserExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("用户");

        List<String> titles = new ArrayList();
        titles.add("用户名");
        titles.add("终端数");
        titles.add("状态");
        titles.add("首次更新时间");
        data.setTitles(titles);

        List<JSONObject> all = clientUserService.getAllToReport(filters);
        if (!CollectionUtils.isEmpty(all)) {
            List<List<Object>> rows = new ArrayList();
            for (JSONObject j : all) {
                List<Object> row = new ArrayList();
                row.add(j.get("username"));
                row.add(j.get("ipCnt"));
                row.add(j.get("status"));
                row.add(j.get("updateTime"));
                rows.add(row);
            }
            data.setRows(rows);
        }

        ExportExcelUtils.exportExcel(response, "终端用户报表.xlsx", data);
    }

    public void eventExcel(HttpServletResponse response, JSONArray datas) throws Exception {
        List<JSONObject> list = datas.toJavaList(JSONObject.class);

        reportTemplete("告警事件报表.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"终端IP", "摄像头名称", "事件类型", "事件来源", "报警等级", "操作行为","状态","时间"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(IpUtils.longToIPv4(j.getLong("clientIp")));
                row.add(j.get("cameraName"));
                row.add(j.get("eventCategory"));
                row.add(j.get("eventSource"));
                row.add(j.get("eventLevel"));
                row.add(ActionType.getLabel(j.getString("action")));
                row.add(EventStatusEnum.getLabel(j.getLong("status")));
                row.add(j.get("startTime"));
                dataList.add(row);
            }
            return dataList;
        });

    }

    public void CameraVisitRateExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        List<VisitRate> all = visitRateService.getAllToReport(filters);
        List<JSONObject> list = all.stream().map(f -> JSONObject.parseObject(JSONObject.toJSONString(f))).collect(Collectors.toList());
        reportTemplete("访问率统计报表.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"地区名称", "摄像头数", "被访问摄像头", "访问率", "访问次数", "终端数"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("cityName"));
                row.add(j.get("cameraCnt"));
                row.add(j.get("visitedCnt"));
                row.add(j.get("rate"));
                row.add(j.get("visitCnt"));
                row.add(j.get("clientCnt"));
                dataList.add(row);
            }
            return dataList;
        });

    }

//    public void selfAuditExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("自审信息");
//        List<JSONObject> all = selfAuditManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("用户名");
//        titles.add("终端IP");
//        titles.add("时间");
//        titles.add("操作类型");
//        titles.add("告警级别");
//        titles.add("结果");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("username"));
//            row.add(j.get("ipAddress"));
//            row.add(j.get("stime"));
//            row.add(j.get("type"));
//            row.add(j.get("severity"));
//            row.add(j.get("result"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "自审信息报表.xlsx", data);
//    }
//
//    public void loginLogExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("登录日志");
//        List<JSONObject> all = loginLogManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("访问编码");
//        titles.add("用户名");
//        titles.add("登录地址");
//        titles.add("登录状态");
//        titles.add("操作信息");
//        titles.add("登录时间");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("infoId"));
//            row.add(j.get("userName"));
//            row.add(j.get("ipaddr"));
//            row.add(j.get("status"));
//            row.add(j.get("msg"));
//            row.add(j.get("loginTime"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "登录日志报表.xlsx", data);
//    }
//
//    public void failAccessExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("访问失败");
//        List<JSONObject> all = failAccessManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备编码");
//        titles.add("源IP");
//        titles.add("目的设备编码");
//        titles.add("目的IP");
//        titles.add("错误类型");
//        titles.add("时间");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("toCode"));
//            row.add(j.get("srcIp"));
//            row.add(j.get("fromCode"));
//            row.add(j.get("dstIp"));
//            row.add(j.get("failAccessCode"));
//            row.add(j.get("stamp"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "访问失败报表.xlsx", data);
//    }


    private void reportTemplete(String fileName, HttpServletResponse response, List<JSONObject> list, BiFunction<List<List<Object>>, List<JSONObject>, List<List<Object>>> biFunction) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

        //将数据写入sheet页中
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        List<List<JSONObject>> lists = SplitList.splitList(list, MAXROWS); // 分割的集合
        for (int i = 1; i <= lists.size(); i++) {
            List<JSONObject> objectList = lists.get(i - 1);
            List<List<Object>> dataList = new ArrayList<>();
            dataList = biFunction.apply(dataList, objectList);
            WriteSheet writeSheet = EasyExcel.writerSheet(i - 1, "sheet" + i).build();
            excelWriter.write(dataList, writeSheet);
        }

        excelWriter.finish();
        response.flushBuffer();
    }
}
