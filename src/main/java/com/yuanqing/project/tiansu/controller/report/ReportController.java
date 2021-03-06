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
import com.yuanqing.project.tiansu.domain.operation.OperationBehaviorSession;
import com.yuanqing.project.tiansu.domain.operation.RawNetFlow;
import com.yuanqing.project.tiansu.domain.operation.RawSignal;
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
 * ??????????????????????????????
 *
 * @author jqchu
 * @version 1.0
 * @since 2017.12.23
 */
@RestController
@RequestMapping(value = "/api/reports")
@CrossOrigin
@Api(value = "??????", description = "????????????")
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
    public AjaxResult sendReport(@ApiParam("????????????") @RequestParam(value = "type", required = false) String type) {
        try {
            reportUtil.generateReport(type);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(404, e.getMessage());
        }
        return AjaxResult.success();
    }


    @GetMapping(value = "/analysis/visit/rate")
    public void getCameraVisitRateReport(@ApiParam("????????????") @RequestParam(value = "cityCode", required = false) String cityCode,
                                         @ApiParam("????????????") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                         @ApiParam("????????????") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                         @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("cityCode", cityCode);  // ????????????
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<VisitRate> list = visitRateService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("?????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "/eventDetail")
    public void getEventReport(@ApiParam("????????????") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
                               @ApiParam("????????????") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
                               @ApiParam("????????????") @RequestParam(value = "eventSource", required = false) String eventSource,
                               @ApiParam("????????????") @RequestParam(value = "strategyName", required = false) String strategyName,
                               @ApiParam("??????") @RequestParam(value = "status", required = false) String status,
                               @ApiParam("????????????") @RequestParam(value = "eventCategory", required = false) String eventCategory,
                               @ApiParam("????????????") @RequestParam(value = "eventLevel", required = false) String eventLevel,
                               @ApiParam("IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                               @ApiParam("???????????????") @RequestParam(value = "cameraName", required = false) String cameraName,
                               @ApiParam("??????") @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "eventSubject", required = false) String eventSubject,
                               @ApiParam("????????????") @RequestParam(value = "ruleName", required = false) String ruleName,
                               @ApiParam("????????????") @RequestParam(value = "action", required = false) String action,
                               @ApiParam("id") @RequestParam(value = "id", required = false) Long id,
                               @ApiParam("????????????") @RequestParam(value = "connectType", required = false) String connectType,
                               @ApiParam("??????") @RequestParam(required = false) String orderType,
                               @ApiParam("????????????") @RequestParam(required = false) String orderValue,
                               @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

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
            event.setClientIp(IpUtils.ipToLong(clientIp));
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = datas.toJavaList(JSONObject.class);
                list.stream().forEach(h -> {
                    h.put("clientIp", IpUtils.longToIp(h.getLong("clientIp")));
                    h.put("status", EventStatusEnum.getLabel(h.getLong("status")));
                    h.put("action", ActionType.getLabel(h.getString("action")));
                });
                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }

    }

    @GetMapping(value = "/sipDevice")
    public void getSipDiveceReport(@ApiParam("??????") @RequestParam(value = "status", required = false) Integer status,
                                   @ApiParam("????????????") @RequestParam(value = "isGb", required = false) Integer isGb,
                                   @ApiParam("????????????") @RequestParam(value = "deviceName", required = false) String deviceName,
                                   @ApiParam("????????????") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @ApiParam("??????ID") @RequestParam(value = "regionId", required = false) Integer regionId,
                                   @ApiParam("IP??????") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @ApiParam("??????") @RequestParam(required = false) String orderType,
                                   @ApiParam("????????????") @RequestParam(required = false) String orderValue,
                                   @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("???????????????".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipClient")
    public void getSipClientReport(@ApiParam("IP??????") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @ApiParam("????????????") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @ApiParam("????????????") @RequestParam(value = "region", required = false) String region,
                                   @ApiParam("??????") @RequestParam(value = "status", required = false) String status,
                                   @ApiParam("???????????????ID") @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                   @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = clientTerminalService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);
                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("????????????".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipUser")
    public void getSipUserReport(@ApiParam("?????????") @RequestParam(value = "username", required = false) String username,
                                 @ApiParam("??????") @RequestParam(value = "status", required = false) String status,
                                 @ApiParam("????????????") @RequestParam(value = "format", required = false) String format,
                                 @ApiParam("??????") @RequestParam(required = false) String orderType,
                                 @ApiParam("????????????") @RequestParam(required = false) String orderValue, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("status", status);
        filters.put("username", username);
        // TODO: ??????????????????
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = clientUserService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("??????????????????".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipServer")
    public void getSipServerReport(@ApiParam("???????????????") @RequestParam(value = "serverCode", required = false) String serverCode,
                                   @ApiParam("????????????") @RequestParam(value = "serverDomain", required = false) String serverDomain,
                                   @ApiParam("?????????IP") @RequestParam(value = "serverIp", required = false) Long serverIp,
                                   @ApiParam("????????????") @RequestParam(value = "deviceType", required = false) String deviceType,
                                   @ApiParam("????????????") @RequestParam(value = "isDelete", defaultValue = "1") Short isDelete,
                                   @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = serverTreeService.getAllToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("?????????????????????".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/operationBehaviorSession")
    public void getoperationBehaviorSessionReport(@ApiParam("????????????") @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
                                                  @ApiParam("????????????") @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
                                                  @ApiParam("IP??????")@RequestParam(value = "ipAddress", required = false) String ipAddress,
                                                  @ApiParam("?????????") @RequestParam(value = "username", required = false) String username,
                                                  @ApiParam("??????")@RequestParam(required = false) String orderType,
                                                  @ApiParam("????????????")@RequestParam(required = false) String orderValue,
                                                  @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        OperationBehaviorSession operationBehaviorSession = new OperationBehaviorSession();
        operationBehaviorSession.setStime(startDate);
        operationBehaviorSession.setEtime(endDate);
        operationBehaviorSession.setSrcIp(IpUtils.ipToLong(ipAddress));
        operationBehaviorSession.setUsername(username);
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            operationBehaviorSession.setOrderType(orderValue + " " + orderType);
        }
        List<JSONObject> list = operationBehaviorSessionService.getAllToReport(operationBehaviorSession);
        if ("xlsx".equals(format)) {
            try {
                operationBehaviorSessionExcel(response, list);
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

                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/operationBehavior")
    public void getOperationBehaviorReport(@ApiParam("????????????") @RequestParam(value = "startDate", required = false) String stime,
                                           @ApiParam("????????????") @RequestParam(value = "endDate", required = false) String etime,
                                           @ApiParam("???IP") @RequestParam(value = "srcIp", required = false) String srcIp,
                                           @ApiParam("??????IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                           @ApiParam("????????????") @RequestParam(value = "dstCode", required = false) String dstCode,
                                           @ApiParam("????????????") @RequestParam(value = "action", required = false) String action,
                                           @ApiParam("??????????????????") @RequestParam(value = "dstDeviceName", required = false) String dstDeviceName,
                                           @ApiParam("?????????") @RequestParam(value = "username", required = false) String username,
                                           @ApiParam("??????") @RequestParam(required = false) String orderType,
                                           @ApiParam("????????????") @RequestParam(required = false) String orderValue,
                                           @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

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

                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                //??????
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }
                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/OperationSignal")
    public void getOperationSignalReport( @ApiParam("???IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                                          @ApiParam("??????IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                                          @ApiParam("??????")@RequestParam(required = false) String orderType,
                                          @ApiParam("????????????")@RequestParam(required = false) String orderValue, @ApiParam("??????????????????") RawSignal rawSignal,
                                          @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        rawSignal.setSrcIp(IpUtils.ipToLong(srcIp));
        rawSignal.setDstIp(IpUtils.ipToLong(dstIp));
        if (StringUtils.isNotBlank(orderValue) && StringUtils.isNotBlank(orderType)) {
            rawSignal.setOrderType(orderValue + " " + orderType);
        }

        if ("xlsx".equals(format)) {
            try {
                rawSignalService.getListToReport(response,rawSignal);
                rawSignalExcel(response, rawSignal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            List<JSONObject> list = rawSignalService.getAllToReport(rawSignal);

            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(RAW_SIGNAL_REPORT_NAME);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("??????????????????".getBytes("gbk"), "iso8859-1") + "." + format);

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "/RawNetFlow")
    public void getRawNetFlowReport(@ApiParam("???ip") @RequestParam(value = "srcIp", required = false) String srcIp,
                                    @ApiParam("??????ip") @RequestParam(value = "dstIp", required = false) String dstIp,
                                    @ApiParam("????????????") @RequestParam(value = "startDate", required = false) String startDate,
                                    @ApiParam("????????????") @RequestParam(value = "endDate", required = false) String endDate,
                                    @ApiParam("??????") @RequestParam(required = false) String orderType,
                                    @ApiParam("????????????") @RequestParam(required = false) String orderValue,
                                    @ApiParam("??????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

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

                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
                //??????
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


//    @GetMapping(value = "/RemoteRecord")
//    public void getRemoteRecordReport(@ApiParam("?????????")@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                      @ApiParam("?????????")@RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                      @ApiParam("?????????")@RequestParam(value = "srcIp", required = false) String srcIp,
//                                      @ApiParam("?????????")@RequestParam(value = "dstIp", required = false) String dstIp,
//                                      @ApiParam("?????????")@RequestParam(value = "remoteType", required = false) String remoteType,
//                                      @ApiParam("?????????")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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
//                //????????????
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //??????
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = remoteRecordManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print??????
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
//                    throw new RuntimeException("????????????");
//                }
//
//                //???????????????
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //???????????????
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
//                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));
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
//            @ApiParam("?????????")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @ApiParam("?????????")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @ApiParam("?????????")@RequestParam(value = "result", required = false) String result,
//            @ApiParam("?????????")@RequestParam(value = "type", required = false) String type,
//            @ApiParam("?????????")@RequestParam(value = "severity", required = false) Integer severity,
//            @ApiParam("?????????")@RequestParam(value = "ip", required = false) String ip,
//            @ApiParam("?????????")@RequestParam(value = "username", required = false) String username,
//            @ApiParam("?????????")@RequestParam(value = "format", required = false) String format,
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
//                //????????????
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //??????
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = selfAuditManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print??????
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
//                    throw new RuntimeException("????????????");
//                }
//
//                //???????????????
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //???????????????
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
//                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));
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
//    public void getFailAccessReport(@ApiParam("?????????")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                    @ApiParam("?????????")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                    @ApiParam("?????????")@RequestParam(value = "srcIp", required = false) String srcIp,
//                                    @ApiParam("?????????")@RequestParam(value = "dstIp", required = false) String dstIp,
//                                    @ApiParam("?????????")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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
//                //????????????
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //??????
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = failAccessManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print??????
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
//                    throw new RuntimeException("????????????");
//                }
//
//                //???????????????
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //???????????????
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
//                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));
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
    public void getAnalysisClientReport(@ApiParam("????????????") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @ApiParam("????????????") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        @ApiParam("?????????IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                                        @ApiParam("????????????") @RequestParam(value = "region[]", required = false) String[] region,
                                        @ApiParam("???????????????") @RequestParam(value = "clientCode", required = false) String clientCode,
                                        @ApiParam("????????????") @RequestParam(value = "action", required = false) String action,
                                        @ApiParam("?????????ID") @RequestParam(value = "cameraId", required = false) Long cameraId,
                                        @ApiParam("?????????ID") @RequestParam(value = "clientId", required = false) String clientId,
                                        @ApiParam("??????") @RequestParam(value = "user", required = false) String user,
                                        @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {


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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = statisticsService.getClientVisitToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/client/visit/cnt")
    public void getAnalysisClientDetailReport(@ApiParam("???IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                                              @ApiParam("??????IP")@RequestParam(value = "dstIp", required = false) String dstIp,
                                              @ApiParam("????????????")@RequestParam(value = "action", required = false) String action,
                                              @ApiParam("??????????????????")@RequestParam(value = "dstCode", required = false) String deviceCode,
                                              @ApiParam("?????????")@RequestParam(value = "username", required = false) String username,
                                              @ApiParam("??????????????????")@RequestParam(value = "dstDeviceName", required = false) String dstDeviceName,
                                              @ApiParam("????????????")@RequestParam(value = "startDate") String startDate,
                                              @ApiParam("????????????")@RequestParam(value = "endDate") String endDate,
                                              @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setDstCode(deviceCode);
        operationBehavior.setUsername(username);
        operationBehavior.setDstDeviceName(dstDeviceName);
        operationBehavior.setAction(action);
        operationBehavior.setDstIp(IpUtils.ipToLong(dstIp));
        operationBehavior.setstartDate(startDate);
        operationBehavior.setendDate(endDate);

        List<JSONObject> list = statisticsService.getClientVisitCntToReport(operationBehavior);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisClientDetailExcelReport(response, list);
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

//                List<JSONObject> list = clientService.getClientVisitDetailToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/client/visit/relatedCamera")
    public void getAnalysisClientRelatedCameraReport(@ApiParam("?????????ID") @RequestParam(value = "clientId", required = false) Long clientId,
                                                     @ApiParam("????????????") @RequestParam(value = "action", required = false) Integer action,
                                                     @ApiParam("???????????????") @RequestParam(value = "cameraCode", required = false) String cameraCode,
                                                     @ApiParam("???????????????") @RequestParam(value = "cameraName", required = false) String cameraName,
                                                     @ApiParam("?????????IP") @RequestParam(value = "cameraIp", required = false) String cameraIp,
                                                     @ApiParam("?????????") @RequestParam(value = "username", required = false) String username,
                                                     @ApiParam("?????????IP") @RequestParam(value = "clientIp", required = false) String clientIp,
                                                     @ApiParam("????????????") @RequestParam(value = "region", required = false) Integer region,
                                                     @ApiParam("????????????") @RequestParam(value = "startDate", required = false) String startDate,
                                                     @ApiParam("????????????") @RequestParam(value = "endDate", required = false) String endDate,
                                                     @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

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

                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);
                }
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("?????????????????????????????????." + format, "utf-8"));
                exporter.exportReport();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/camera/visited")
    public void getAnalysisCameraReport(@ApiParam("????????????")@RequestParam(value = "action",required = false) Integer action,
                                        @ApiParam("?????????IP")@RequestParam(value = "cameraIp",required = false) String cameraIp,
                                        @ApiParam("???????????????")Camera camera,
                                        @ApiParam("??????")@RequestParam(required = false) String orderType,
                                        @ApiParam("????????????")@RequestParam(required = false) String orderValue,
                                        @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();


                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/camera/visit/cnt")
    public void getAnalysisCameraReport( @ApiParam("????????????")@RequestParam(value = "startDate", required = false) String  startDate,
                                         @ApiParam("????????????")@RequestParam(value = "endDate", required = false) String endDate,
                                         @ApiParam("?????????ID")@RequestParam(value = "clientId", required = false) Long clientId,
                                         @ApiParam("?????????ID")@RequestParam(value = "cameraId", required = false) Long cameraId,
                                         @ApiParam("???IP")@RequestParam(value = "srcIp", required = false) String srcIp,
                                         @ApiParam("??????????????????")@RequestParam(value = "deviceCode", required = false) String dstCode,
                                         @ApiParam("?????????")@RequestParam(value = "username", required = false) String username,
                                         @ApiParam("???????????????")@RequestParam(value = "srcCode", required = false) String srcCode,
                                         @ApiParam("????????????")@RequestParam(value = "action", required = false) String action,
                                        @ApiParam("??????")@RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        OperationBehavior operationBehavior = new OperationBehavior();
        operationBehavior.setClientId(clientId);
        operationBehavior.setCameraId(cameraId);
        operationBehavior.setDstCode(srcCode);
        operationBehavior.setAction(action);
        operationBehavior.setSrcIp(IpUtils.ipToLong(srcIp));
        operationBehavior.setUsername(username);
        operationBehavior.setstartDate(startDate);
        operationBehavior.setDstCode(dstCode);
        operationBehavior.setendDate(endDate);

        List<JSONObject> all = statisticsService.getCameraVisitedCntToReport(operationBehavior);

        if ("xlsx".equals(format)) {
            try {
                this.getAnalysisCameraDetailExcelReport(response, all);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ApplicationContext ctx = SpringContextUtil.getApplicationContext();

            org.springframework.core.io.Resource resource = null;

            resource = ctx.getResource(ANALYSIS_CAMERA_DETAIL);

            InputStream inputStream;
            try {
                inputStream = resource.getInputStream();


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(all);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("??????????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/camera/visited/relatedClient")
    public void getAnalysisCameraRelatedClientReport(@ApiParam("????????????") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                                     @ApiParam("????????????") @RequestParam(value = "action", required = false) Integer action,
                                                     @ApiParam("????????????") @RequestParam(value = "startDate", required = false) String startDate,
                                                     @ApiParam("????????????") @RequestParam(value = "endDate", required = false) String endDate,
                                                     @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(statisticsList);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("????????????????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping(value = "analysis/visit/rate/cameraCnt")
    public void getRateCameraCntReport(@ApiParam("???????????????")Camera camera,
                                       @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        List<JSONObject> list = visitRateService.getRateCameraCntToReport(camera);

        if ("xlsx".equals(format)) {
            try {
                this.getRateCameraCntExcelReport(response, list);
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("????????????????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/visitedCnt")
    public void getRateVisitCntReport(@ApiParam("????????????") @RequestParam(value = "region", required = false) Integer region,
                                      @ApiParam("????????????") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                      @ApiParam("????????????") @RequestParam(value = "deviceName", required = false) String deviceName,
                                      @ApiParam("IP??????") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                      @ApiParam("??????") @RequestParam(value = "status", required = false) String status,
                                      @ApiParam("????????????") @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                      @ApiParam("????????????") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                      @ApiParam("????????????") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                      @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateVisitedCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("???????????????????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/visitCnt")
    public void getAnalysisCameraReport(@ApiParam("????????????") @RequestParam(value = "region", required = false) Integer region,
                                        @ApiParam("???IP") @RequestParam(value = "srcIp", required = false) String srcIp,
                                        @ApiParam("??????IP") @RequestParam(value = "dstIp", required = false) String dstIp,
                                        @ApiParam("?????????ID") @RequestParam(value = "clientId", required = false) String clientId,
                                        @ApiParam("?????????ID") @RequestParam(value = "cameraId", required = false) String cameraId,
                                        @ApiParam("????????????") @RequestParam(value = "action", required = false) String action,
                                        @ApiParam("????????????") @RequestParam(value = "dstCode", required = false) String dstCode,
                                        @ApiParam("?????????") @RequestParam(value = "username", required = false) String username,
                                        @ApiParam("????????????") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                        @ApiParam("????????????") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                        @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateVisitCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("???????????????????????????." + format, "utf-8"));

                exporter.exportReport();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "analysis/visit/rate/clientCnt")
    public void getRateClientCntReport(@ApiParam("????????????") @RequestParam(value = "region", required = false) Integer region,
                                       @ApiParam("????????????") @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @ApiParam("??????") @RequestParam(value = "status", required = false) Integer status,
                                       @ApiParam("IP??????") @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @ApiParam("???????????????ID") @RequestParam(value = "sipServerId", required = false) String sipServerId,
                                       @ApiParam("????????????") @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                       @ApiParam("????????????") @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                       @ApiParam("????????????") @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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


                //????????????
                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

                //??????
                Map<String, Object> params = new HashMap<>();

                List<JSONObject> list = visitRateService.getRateClientCntToReport(filters);

                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);

                //print??????
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
                    throw new RuntimeException("????????????");
                }

                //???????????????
                ExporterInput exporterInput = new SimpleExporterInput(print);
                exporter.setExporterInput(exporterInput);

                //???????????????
                if ("html".equals(format)) {
                    SimpleHtmlExporterOutput htmlExportOutput = new SimpleHtmlExporterOutput(response.getOutputStream());

                    exporter.setExporterOutput(htmlExportOutput);
                } else {
                    OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
                            response.getOutputStream());
                    exporter.setExporterOutput(exporterOutput);

                }

                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String("???????????????????????????".getBytes("gbk"), "iso8859-1") + "." + format);

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
//            @ApiParam("?????????")@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @ApiParam("?????????")@RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @ApiParam("?????????")@RequestParam(value = "ipaddr", required = false) String ipaddr,
//            @ApiParam("?????????")@RequestParam(value = "userName", required = false) String userName,
//            @ApiParam("?????????")@RequestParam(value = "status", required = false) Integer status,
//            @ApiParam("?????????")@RequestParam(value = "format", required = false) String format,
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
//                //????????????
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //??????
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = loginLogManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print??????
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
//                    throw new RuntimeException("????????????");
//                }
//
//                //???????????????
//                ExporterInput exporterInput = new SimpleExporterInput(print);
//                exporter.setExporterInput(exporterInput);
//
//                //???????????????
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
//                        "attachment;filename=" + URLEncoder.encode("??????????????????." + format, "utf-8"));
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
        reportTemplete("???????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"IP??????", "MAC??????","??????","??????????????????"}));
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
        reportTemplete("???????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "?????????IP","???????????????","???????????????","??????","????????????","????????????","??????","??????"}));
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
        reportTemplete("???????????????????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "???????????????","?????????IP","????????????","????????????","??????","??????????????????"}));
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

    public void getRateCameraCntExcelReport(HttpServletResponse response, List<JSONObject> list) throws Exception {
        reportTemplete("????????????????????????????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "???????????????","?????????IP","????????????","????????????","??????","??????????????????"}));
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
        reportTemplete("????????????????????????????????????.xlsx", response, statisticsList, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "????????????","????????????"}));
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

    private void getAnalysisCameraDetailExcelReport(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("??????????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "?????????","??????", "????????????", "??????"}));
            dataList.add(headList);

            for (JSONObject j : objectList) {
                List<Object> row = new ArrayList();
                row.add(j.get("srcIp"));
                row.add(j.get("username"));
                row.add(j.get("action"));
                row.add(j.get("actionDetail"));
                row.add(j.get("stamp"));
                dataList.add(row);
            }
            return dataList;
        });
    }

    private void getAnalysisCameraExcelReport(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "?????????IP","???????????????", "????????????", "????????????","????????????","???????????????"}));
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
        reportTemplete("?????????????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "?????????IP","???????????????", "????????????", "???????????????"}));
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

    private void getAnalysisClientDetailExcelReport(HttpServletResponse response, List<JSONObject> all) throws Exception {
        reportTemplete("????????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "???????????????","?????????IP", "???????????????", "??????","????????????","??????"}));
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
        reportTemplete("??????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "?????????", "????????????", "????????????", "???????????????"}));
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

    private void rawSignalExcel(HttpServletResponse response, RawSignal rawSignal) throws Exception {
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
//        response.setCharacterEncoding("utf8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("??????????????????.xlsx", "utf-8"));
//
//        //???????????????sheet??????
//        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
//
//        List<List<JSONObject>> lists = SplitList.splitList(list, MAXROWS); // ???????????????
//        for (int i = 1; i <= lists.size(); i++) {
//            List<JSONObject> objectList = lists.get(i - 1);
//            List<List<Object>> dataList = new ArrayList<>();
//            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"?????????IP", "????????????IP", "??????????????????", "??????", "??????"}));
//            dataList.add(headList);
//
//            for (JSONObject j : objectList) {
//                List<Object> row = new ArrayList();
//                row.add(j.get("srcIp"));
//                row.add(j.get("dstIp"));
//                row.add(j.get("toCode"));
//                row.add(j.get("content"));
//                row.add(j.get("stamp"));
//                dataList.add(row);
//            }
//
//            WriteSheet writeSheet = EasyExcel.writerSheet(i - 1, "sheet" + i).build();
//            excelWriter.write(dataList, writeSheet);
//        }
//
//        excelWriter.finish();
//        response.flushBuffer();

    }

    private void rawNetFlowExcel(HttpServletResponse response, List<JSONObject> list) throws Exception {
        reportTemplete("??????????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???IP", "?????????", "??????IP", "????????????", "?????????","?????????","??????"}));
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
//        data.setName("????????????");
//        List<JSONObject> all = remoteRecordManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("?????????IP");
//        titles.add("?????????");
//        titles.add("???MAC");
//        titles.add("????????????IP");
//        titles.add("????????????");
//        titles.add("??????MAC");
//        titles.add("??????");
//        titles.add("??????????????????");
//        titles.add("???????????????");
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
//        ExportExcelUtils.exportExcel(response, "??????????????????.xlsx", data);
//    }

    public void operationBehaviorSessionExcel(HttpServletResponse response, List<JSONObject> list) throws Exception {
        reportTemplete("??????????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"?????????IP", "????????????", "????????????", "??????????????????"}));
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
        reportTemplete("??????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "?????????IP", "???????????????", "???????????????", "??????", "????????????", "????????????", "??????", "????????????"}));
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

        reportTemplete("?????????????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"???????????????", "?????????IP", "???????????????", "???????????????"}));
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
        reportTemplete("???????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"????????????", "IP??????", "???????????????", "????????????", "??????", "??????????????????"}));
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

        reportTemplete("????????????.xlsx", response, all, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"IP??????", "MAC??????", "?????????", "??????", "??????????????????"}));
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
        data.setName("??????");

        List<String> titles = new ArrayList();
        titles.add("?????????");
        titles.add("?????????");
        titles.add("??????");
        titles.add("??????????????????");
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

        ExportExcelUtils.exportExcel(response, "??????????????????.xlsx", data);
    }

    public void eventExcel(HttpServletResponse response, JSONArray datas) throws Exception {
        List<JSONObject> list = datas.toJavaList(JSONObject.class);

        reportTemplete("??????????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"??????IP", "???????????????", "????????????", "????????????", "????????????", "????????????","??????","??????"}));
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
        reportTemplete("?????????????????????.xlsx", response, list, (dataList, objectList) -> {
            List<Object> headList = new ArrayList<>(Arrays.asList(new Object[]{"????????????", "????????????", "??????????????????", "?????????", "????????????", "?????????"}));
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
//        data.setName("????????????");
//        List<JSONObject> all = selfAuditManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("?????????");
//        titles.add("??????IP");
//        titles.add("??????");
//        titles.add("????????????");
//        titles.add("????????????");
//        titles.add("??????");
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
//        ExportExcelUtils.exportExcel(response, "??????????????????.xlsx", data);
//    }
//
//    public void loginLogExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("????????????");
//        List<JSONObject> all = loginLogManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("????????????");
//        titles.add("?????????");
//        titles.add("????????????");
//        titles.add("????????????");
//        titles.add("????????????");
//        titles.add("????????????");
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
//        ExportExcelUtils.exportExcel(response, "??????????????????.xlsx", data);
//    }
//
//    public void failAccessExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("????????????");
//        List<JSONObject> all = failAccessManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("???????????????");
//        titles.add("???IP");
//        titles.add("??????????????????");
//        titles.add("??????IP");
//        titles.add("????????????");
//        titles.add("??????");
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
//        ExportExcelUtils.exportExcel(response, "??????????????????.xlsx", data);
//    }


    private void reportTemplete(String fileName, HttpServletResponse response, List<JSONObject> list, BiFunction<List<List<Object>>, List<JSONObject>, List<List<Object>>> biFunction) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));

        //???????????????sheet??????
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        List<List<JSONObject>> lists = SplitList.splitList(list, MAXROWS); // ???????????????
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
