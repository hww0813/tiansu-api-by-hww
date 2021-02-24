package com.yuanqing.project.tiansu.controller;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.utils.ExportExcelUtils;
import com.yuanqing.common.utils.ReportUtil;
import com.yuanqing.common.utils.poi.ExcelUtil;
import com.yuanqing.common.utils.spring.SpringContextUtil;
import com.yuanqing.framework.web.controller.BaseController;
import com.yuanqing.framework.web.domain.AjaxResult;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.report.ExcelData;
import com.yuanqing.project.tiansu.domain.video.VisitRate;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.assets.*;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import io.swagger.annotations.Api;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @Resource
//    private ClientVisitManager clientVisitManager;
//
//    @Resource
//    private CameraVisitedManager cameraVisitedManager;
//
//    @Resource
//    private CameraDirectVisitedManager cameraDirectVisitedManager;

    @Resource
    private IServerTreeService serverTreeService;

//    @Resource
//    private OperationBehaviorSessionManager operationBehaviorSessionManager;
//
//    @Resource
//    private OperationBehaviorManager operationBehaviorManager;
//
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
    public AjaxResult sendReport(@RequestParam(value = "type", required = false) String type) {
        try {
            reportUtil.generateReport(type);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(404, e.getMessage());
        }
        return AjaxResult.success();
    }


    @GetMapping(value = "/analysis/visit/rate")
    public void getCameraVisitRateReport(@RequestParam(value = "cityCode", required = false) String cityCode,
                                         @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                         @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                         @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("cityCode", cityCode);
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

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


//    @GetMapping(value = "/eventDetail")
//    public void getEventReport(@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                               @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                               @RequestParam(value = "id", required = false) Long id,
//                               @RequestParam(value = "eventCategory", required = false) String eventCategory,
//                               @RequestParam(value = "eventLevel", required = false) String eventLevel,
//                               @RequestParam(value = "status", required = false) String status,
//                               @RequestParam(value = "operId", required = false) String operId,
//                               @RequestParam(value = "eventSource", required = false) String eventSource,
//                               @RequestParam(value = "cameraCode", required = false) String cameraCode,
//                               @RequestParam(value = "clientCode", required = false) String clientCode,
//                               @RequestParam(value = "clientIp", required = false) String clientIp,
//                               @RequestParam(value = "clientMac", required = false) String clientMac,
//                               @RequestParam(value = "action", required = false) String action,
//                               @RequestParam(value = "content", required = false) String content,
//                               @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("status", status);
//        filters.put("id", id);
//        filters.put("eventCategory", eventCategory);
//        filters.put("eventLevel", eventLevel);
//        filters.put("operId", operId);
//        filters.put("eventSource", eventSource);
//        filters.put("cameraCode", cameraCode);
//        filters.put("clientCode", clientCode);
//        filters.put("clientIp", clientIp);
//        filters.put("clientMac", clientMac);
//        filters.put("action", action);
//        filters.put("content", content);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.eventExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(EVENT_REPORT_NAME);
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
//                List<JSONObject> list = eventDetailManager.getAllToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("告警事件报表." + format, "utf-8"));
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

    @GetMapping(value = "/sipDevice")
    public void getSipDiveceReport(@RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "deviceName", required = false) String deviceName,
                                   @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @RequestParam(value = "region", required = false) String region,
                                   @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                   @RequestParam(value = "format", required = false) String format) {
        JSONObject filters = new JSONObject();
        filters.put("status", status);
        filters.put("deviceName", deviceName);
        filters.put("deviceCode", deviceCode);
        filters.put("ipAddress", ipAddress);
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
                CameraExcel(filters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/SipClient")
    public void getSipClientReport(@RequestParam(value = "ipAddress", required = false) String ipAddress,
                                   @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                   @RequestParam(value = "region", required = false) String region,
                                   @RequestParam(value = "status", required = false) String status,
                                   @RequestParam(value = "sipServerId", required = false) Long sipServerId,
                                   @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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
    public void getSipUserReport(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("status", status);
        filters.put("username", username);

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
    public void getSipServerReport(@RequestParam(value = "serverCode", required = false) String serverCode,
                                   @RequestParam(value = "serverDomain", required = false) String serverDomain,
                                   @RequestParam(value = "serverIp", required = false) Long serverIp,
                                   @RequestParam(value = "deviceType", required = false) String deviceType,
                                   @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("serverCode", serverCode);
        filters.put("serverDomain", serverDomain);
        filters.put("serverIp", serverIp);
        filters.put("serverTypeValue", deviceType);
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

//    @GetMapping(value = "/operationBehaviorSession")
//    public void getoperationBehaviorSessionReport(@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                                  @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                                  @RequestParam(value = "sessionId", required = false) Long sessionId,
//                                                  @RequestParam(value = "srcCode", required = false) String srcCode,
//                                                  @RequestParam(value = "dstCode", required = false) String dstCode,
//                                                  @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        if (stime != null) {
//            filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        if (etime != null) {
//            filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        filters.put("sessionId", sessionId);
//        filters.put("srcCode", srcCode);
//        filters.put("dstCode", dstCode);
//        if ("xlsx".equals(format)) {
//            try {
//                operationBehaviorSessionExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(OPERATION_BEHAVIOR_SESSION_REPORT_NAME);
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
//                List<JSONObject> list = operationBehaviorSessionManager.getAllToReport(filters);
//
//                JRDataSource jrDataSource = new JRBeanCollectionDataSource(list);
//
//                //print文件
//                JasperPrint print = null;
//                try {
//                    print = JasperFillManager.fillReport(jasperReport, params, jrDataSource);
//                } catch (JRException e) {
//                    e.printStackTrace();
//                }
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
//                        "attachment;filename=" + URLEncoder.encode("行为会话报表." + format, "utf-8"));
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

//    @GetMapping(value = "/operationBehavior")
//    public void getOperationBehaviorReport(@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                           @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                           @RequestParam(value = "srcIp", required = false) String srcIp,
//                                           @RequestParam(value = "dstIp", required = false) String dstIp,
//                                           @RequestParam(value = "srcCode", required = false) String srcCode,
//                                           @RequestParam(value = "dstCode", required = false) String dstCode,
//                                           @RequestParam(value = "sessionId", required = false) Long sessionId,
//                                           @RequestParam(value = "clientId", required = false) Long clientId,
//                                           @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                           @RequestParam(value = "action", required = false) String action,
//                                           @RequestParam(value = "dstDeviceIp", required = false) String dstDeviceIp,
//                                           @RequestParam(value = "content", required = false) String content,
//                                           @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        filters.put("srcIp", srcIp);
//        filters.put("dstIp", dstIp);
//        filters.put("srcCode", srcCode);
//        filters.put("dstCode", dstCode);
//        filters.put("sessionId", sessionId);
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("action", action);
//        filters.put("dstDeviceIp", dstDeviceIp);
//        if (StringUtils.isNotBlank(content)) {
//            filters.put("content", "%" + content + "%");
//        }
//        if ("xlsx".equals(format)) {
//            try {
//                operationBehaviorExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(OPERATION_BEHAVIOR_REPORT_NAME);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = operationBehaviorManager.getAllToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("操作行为报表." + format, "utf-8"));
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

//    @GetMapping(value = "/OperationSignal")
//    public void getOperationSignalReport(@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                         @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                         @RequestParam(value = "srcIp", required = false) String srcIp,
//                                         @RequestParam(value = "dstIp", required = false) String dstIp,
//                                         @RequestParam(value = "connectType", required = false) String connectType,
//                                         @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        if (stime != null) {
//            filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        if (etime != null) {
//            filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        filters.put("connectType", connectType);
//        filters.put("srcIp", srcIp);
//        filters.put("dstIp", dstIp);
//
//        if ("xlsx".equals(format)) {
//            try {
//                rawSignalExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(RAW_SIGNAL_REPORT_NAME);
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
//                List<JSONObject> list = rawSignalManager.getAllToReport(filters);
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
//                        "attachment;filename=" + new String("原始信令报表".getBytes("gbk"), "iso8859-1") + "." + format);
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


//    @GetMapping(value = "/RawNetFlow")
//    public void getRawNetFlowReport(@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                    @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                    @RequestParam(value = "srcIp", required = false) String srcIp,
//                                    @RequestParam(value = "dstIp", required = false) String dstIp,
//                                    @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        if (stime != null) {
//            filters.put("stime", stime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        if (etime != null) {
//            filters.put("etime", etime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        }
//        filters.put("srcIp", srcIp);
//        filters.put("dstIp", dstIp);
//        if ("xlsx".equals(format)) {
//            try {
//                rawNetFlowExcel(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(RAW_NET_FLOW_REPORT_NAME);
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
//                List<JSONObject> list = rawNetFlowManager.getAllToReport(filters);
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
//                        "attachment;filename=" + new String("流量报表".getBytes("gbk"), "iso8859-1") + "." + format);
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


//    @GetMapping(value = "/RemoteRecord")
//    public void getRemoteRecordReport(@RequestParam(value = "stime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                      @RequestParam(value = "etime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                      @RequestParam(value = "srcIp", required = false) String srcIp,
//                                      @RequestParam(value = "dstIp", required = false) String dstIp,
//                                      @RequestParam(value = "remoteType", required = false) String remoteType,
//                                      @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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
//            @RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @RequestParam(value = "result", required = false) String result,
//            @RequestParam(value = "type", required = false) String type,
//            @RequestParam(value = "severity", required = false) Integer severity,
//            @RequestParam(value = "ip", required = false) String ip,
//            @RequestParam(value = "username", required = false) String username,
//            @RequestParam(value = "format", required = false) String format,
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
//    public void getFailAccessReport(@RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//                                    @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//                                    @RequestParam(value = "srcIp", required = false) String srcIp,
//                                    @RequestParam(value = "dstIp", required = false) String dstIp,
//                                    @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
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

//    @GetMapping(value = "analysis/client/visit")
//    public void getAnalysisClientReport(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                        @RequestParam(value = "clientIp", required = false) String clientIp,
//                                        @RequestParam(value = "region[]", required = false) String[] region,
//                                        @RequestParam(value = "clientCode", required = false) String clientCode,
//                                        @RequestParam(value = "action", required = false) String action,
//                                        @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                        @RequestParam(value = "clientId", required = false) String clientId,
//                                        @RequestParam(value = "user", required = false) String user,
//                                        @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//
//        JSONObject filters = new JSONObject();
//        if (startDate != null) filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        if (startDate != null) filters.put("endDate", endDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("clientIp", clientIp);
//        filters.put("clientCode", clientCode);
//        filters.put("action", action);
//        filters.put("cameraId", cameraId);
//        filters.put("clientId", clientId);
//        filters.put("user", user);
//        filters = RegionUtil.setRegion(filters, region);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisClientExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CLIENT);
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
//                List<JSONObject> list = clientVisitManager.getClientVisitToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("终端访问报表." + format, "utf-8"));
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

//    @GetMapping(value = "analysis/client/visit/cnt")
//    public void getAnalysisClientDetailReport(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                              @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                              @RequestParam(value = "clientId", required = false) Long clientId,
//                                              @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                              @RequestParam(value = "dstIp", required = false) String dstIp,
//                                              @RequestParam(value = "dstCode", required = false) String dstCode,
//                                              @RequestParam(value = "action", required = false) String action,
//                                              @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("dstIp", dstIp);
//        filters.put("dstCode", dstCode);
//        filters.put("action", action);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisClientDetailExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CLIENT_DETAIL);
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
//                List<JSONObject> list = clientService.getClientVisitDetailToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("终端访问次数报表." + format, "utf-8"));
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

//    @GetMapping(value = "analysis/client/visit/relatedCamera")
//    public void getAnalysisClientRelatedCameraReport(@RequestParam(value = "clientId") Long clientId,
//                                                     @RequestParam(value = "action", required = false) String action,
//                                                     @RequestParam(value = "cameraCode", required = false) String cameraCode,
//                                                     @RequestParam(value = "cameraName", required = false) String cameraName,
//                                                     @RequestParam(value = "cameraIp", required = false) String cameraIp,
//                                                     @RequestParam(value = "region[]", required = false) String[] region,
//                                                     @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                                     @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                                     @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        filters.put("clientId", clientId);
//        filters.put("action", action);
//        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("cameraCode", cameraCode);
//        filters.put("cameraName", cameraName);
//        filters.put("cameraIp", cameraIp);
//        filters = RegionUtil.setRegion(filters,region);
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisClientRelatedCameraExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CLIENT_RELATED_CAMERA);
//
//            InputStream inputStream;
//            try {
//                inputStream = resource.getInputStream();
//
//                //编译报表
//                JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
//
//                //参数
//                Map<String, Object> params = new HashMap<>();
//
//                List<JSONObject> list = clientVisitManager.getClientVisitRelateCameraToReport(filters);
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
//                }
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=" + URLEncoder.encode("终端访问相关摄像头报表." + format, "utf-8"));
//                exporter.exportReport();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JRException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @GetMapping(value = "analysis/camera/visited")
//    public void getAnalysisCameraReport(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                        @RequestParam(value = "cameraIp", required = false) String cameraIp,
//                                        @RequestParam(value = "region[]", required = false) String[] region,
//                                        @RequestParam(value = "cameraCode", required = false) String cameraCode,
//                                        @RequestParam(value = "cameraName", required = false) String cameraName,
//                                        @RequestParam(value = "action", required = false) String action,
//                                        @RequestParam(value = "cameraId", required = false) String cameraId,
//                                        @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        if (startDate != null) filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        if (startDate != null) filters.put("endDate", endDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("cameraIp", cameraIp);
//        filters.put("cameraCode", cameraCode);
//        filters.put("cameraName", cameraName);
//        filters.put("action", action);
//        filters.put("cameraId", cameraId);
//        filters = RegionUtil.setRegion(filters, region);
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisCameraExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CAMERA);
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
//                List<JSONObject> list = cameraVisitedManager.getCameraVisitedToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("摄像头被访问报表." + format, "utf-8"));
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

//    @GetMapping(value = "analysis/camera/visit/cnt")
//    public void getAnalysisCameraReport(@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                        @RequestParam(value = "clientId", required = false) Long clientId,
//                                        @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                        @RequestParam(value = "srcIp", required = false) String srcIp,
//                                        @RequestParam(value = "srcCode", required = false) String srcCode,
//                                        @RequestParam(value = "action", required = false) String action,
//                                        @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//
//        JSONObject filters = new JSONObject();
//        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("srcIp", srcIp);
//        filters.put("srcCode", srcCode);
//        filters.put("action", action);
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

//    @GetMapping(value = "analysis/camera/visited/relatedClient")
//    public void getAnalysisCameraRelatedClientReport(@RequestParam(value = "clientId", required = false) Long clientId,
//                                                     @RequestParam(value = "cameraId", required = false) Long cameraId,
//                                                     @RequestParam(value = "action", required = false) String action,
//                                                     @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//                                                     @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//                                                     @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
//        JSONObject filters = new JSONObject();
//        filters.put("clientId", clientId);
//        filters.put("cameraId", cameraId);
//        filters.put("action", action);
//        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//
//        if ("xlsx".equals(format)) {
//            try {
//                this.getAnalysisCameraRelatedClientExcelReport(response, filters);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ApplicationContext ctx = SpringContextUtil.getApplicationContext();
//
//            org.springframework.core.io.Resource resource = null;
//
//            resource = ctx.getResource(ANALYSIS_CAMERA_RELATED_CLIENT);
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
//                List<JSONObject> list = cameraVisitedManager.getCameraVisitedRelateClientToReport(filters);
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
//                        "attachment;filename=" + URLEncoder.encode("摄像头被访问相关终端报表." + format, "utf-8"));
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


    @GetMapping(value = "analysis/visit/rate/cameraCnt")
    public void getRateCameraCntReport(@RequestParam(value = "cityCode", required = false) String cityCode,
                                       @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @RequestParam(value = "deviceName", required = false) String deviceName,
                                       @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @RequestParam(value = "status", required = false) String status,
                                       @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                       @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {

        JSONObject filters = new JSONObject();
        filters.put("deviceCode", deviceCode);
        filters.put("deviceName", deviceName);
        filters.put("ipAddress", ipAddress);
        filters.put("status", status);
        filters.put("manufacturer", manufacturer);
        if (cityCode == null) {
            cityCode = macsConfigService.getRegion(cityCode);
        }
        filters.put("cityCode", cityCode);
        filters.put("length", cityCode.length());

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
    public void getRateVisitCntReport(@RequestParam(value = "cityCode", required = false) String cityCode,
                                      @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                      @RequestParam(value = "deviceName", required = false) String deviceName,
                                      @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                      @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                      @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                      @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("manufacturer", manufacturer);
        filters.put("deviceCode", deviceCode);
        filters.put("deviceName", deviceName);
        filters.put("ipAddress", ipAddress);
        filters.put("status", status);
        filters.put("cityCode", cityCode);
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

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
    public void getAnalysisCameraReport(@RequestParam(value = "cityCode", required = false) String cityCode,
                                        @RequestParam(value = "srcIp", required = false) String srcIp,
                                        @RequestParam(value = "dstIp", required = false) String dstIp,
                                        @RequestParam(value = "clientId", required = false) String clientId,
                                        @RequestParam(value = "cameraId", required = false) String cameraId,
                                        @RequestParam(value = "action", required = false) String action,
                                        @RequestParam(value = "dstCode", required = false) String dstCode,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("cityCode", cityCode);
        filters.put("srcIp", srcIp);
        filters.put("dstIp", dstIp);
        filters.put("clientId", clientId);
        filters.put("cameraId", cameraId);
        filters.put("action", action);
        filters.put("dstCode", dstCode);
        filters.put("username", username);
        filters.put("length", cityCode.length());
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

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
    public void getRateClientCntReport(@RequestParam(value = "cityCode", required = false) String cityCode,
                                       @RequestParam(value = "deviceCode", required = false) String deviceCode,
                                       @RequestParam(value = "status", required = false) String status,
                                       @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                       @RequestParam(value = "sipServerId", required = false) String sipServerId,
                                       @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                       @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                       @RequestParam(value = "format", required = false) String format, HttpServletResponse response) {
        JSONObject filters = new JSONObject();
        filters.put("cityCode", cityCode);
        filters.put("deviceCode", deviceCode);
        filters.put("status", status);
        filters.put("ipAddress", ipAddress);
        filters.put("sipServerId", sipServerId);
        filters.put("length", cityCode.length());
        filters.put("startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        filters.put("endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
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
//            @RequestParam(value = "stime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime stime,
//            @RequestParam(value = "etime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime etime,
//            @RequestParam(value = "ipaddr", required = false) String ipaddr,
//            @RequestParam(value = "userName", required = false) String userName,
//            @RequestParam(value = "status", required = false) Integer status,
//            @RequestParam(value = "format", required = false) String format,
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
        ExcelData data = new ExcelData();
        data.setName("访问率相关终端个数");
        List<JSONObject> all = visitRateService.getRateClientCntToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("设备编号");
        titles.add("IP地址");
        titles.add("MAC地址");
        titles.add("登陆账号");
        titles.add("状态");
        titles.add("最后更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("deviceCode"));
            row.add(j.get("ipAddress"));
            row.add(j.get("macAddress"));
            row.add(j.get("username"));
            row.add(j.get("status"));
            row.add(j.get("updateTime"));
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "访问率相关终端报表.xlsx", data);
    }

    private void getRateVisitCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("访问率访问次数列表");
        List<JSONObject> all = visitRateService.getRateVisitCntToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("终端IP");
        titles.add("摄像头IP");
        titles.add("摄像头编号");
        titles.add("摄像头名称");
        titles.add("动作");
        titles.add("动作详情");
        titles.add("上行流量");
        titles.add("下行流量");
        titles.add("登录账号");
        titles.add("时间");
        titles.add("结果");
        data.setTitles(titles);

        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("srcIp"));
            row.add(j.get("dstIp"));
            row.add(j.get("dstCode"));
            row.add(j.get("dstDeviceName"));
            row.add(j.get("action"));
            row.add(j.get("actionDetail"));
            row.add(j.get("upFlow"));
            row.add(j.get("downFlow"));
            row.add(j.get("username"));
            row.add(j.get("stamp"));
            row.add(j.get("result"));
            rows.add(row);
        }
        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "访问率访问次数报表.xlsx", data);
    }

    public void getRateVisitedCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("访问率被访问摄像头个数");
        List<JSONObject> all = visitRateService.getRateVisitedCntToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("设备编号");
        titles.add("摄像头名称");
        titles.add("IP地址");
        titles.add("设备厂商");
        titles.add("所在地区");
        titles.add("状态");
        titles.add("最后更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("deviceCode"));
            row.add(j.get("deviceName"));
            row.add(j.get("ipAddress"));
            row.add(j.get("manufacturer"));
            row.add(j.get("region"));
            row.add(j.get("status"));
            row.add(j.get("updateTime"));
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "访问率被访问摄像头个数报表.xlsx", data);
    }

    public void getRateCameraCntExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("访问率相关摄像头总数");
        List<JSONObject> all = visitRateService.getRateCameraCntToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("设备编号");
        titles.add("摄像头名称");
        titles.add("IP地址");
        titles.add("设备厂商");
        titles.add("所在地区");
        titles.add("状态");
        titles.add("最后更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("deviceCode"));
            row.add(j.get("deviceName"));
            row.add(j.get("ipAddress"));
            row.add(j.get("manufacturer"));
            row.add(j.get("region"));
            row.add(j.get("status"));
            row.add(j.get("updateTime"));
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "访问率相关摄像头总数报表.xlsx", data);
    }

//    private void getAnalysisCameraRelatedClientExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("摄像头被访问相关终端");
//        List<JSONObject> all = cameraVisitedManager.getCameraVisitedRelateClientToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("终端编号");
//        titles.add("终端名称");
//        titles.add("登录用户");
//        titles.add("终端IP");
//        titles.add("访问次数");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("CLIENT_CODE"));
//            row.add(j.get("CLIENT_NAME"));
//            row.add(j.get("USERNAME"));
//            row.add(j.get("CLIENT_IP"));
//            row.add(j.get("VISIT_CNT"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "摄像头被访问相关终端报表.xlsx", data);
//    }
//
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
//
//    private void getAnalysisCameraExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("摄像头被访问");
//        List<JSONObject> all = cameraVisitedManager.getCameraVisitedToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("摄像头编号");
//        titles.add("摄像头IP");
//        titles.add("摄像头名称");
//        titles.add("所在地区");
//        titles.add("操作类型");
//        titles.add("被访问次数");
//        titles.add("相关终端数");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("CAMERA_CODE"));
//            row.add(j.get("CAMERA_IP"));
//            row.add(j.get("CAMERA_NAME"));
//            row.add(j.get("CAMERA_REGION"));
//            row.add(j.get("ACTION"));
//            row.add(j.get("VISITED_CNT"));
//            row.add(j.get("CLIENT_CNT"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "摄像头被访问报表.xlsx", data);
//    }
//
//    private void getAnalysisClientRelatedCameraExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("终端访问相关摄像头");
//        List<JSONObject> all = clientVisitManager.getClientVisitRelateCameraToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("设备编号");
//        titles.add("名称");
//        titles.add("IP地址");
//        titles.add("所在地区");
//        titles.add("被访问次数");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("CAMERA_CODE"));
//            row.add(j.get("CAMERA_NAME"));
//            row.add(j.get("CAMERA_IP"));
//            row.add(j.get("CAMERA_REGION"));
//            row.add(j.get("VISITED_CNT"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "终端访问相关摄像头报表.xlsx", data);
//    }
//
//    private void getAnalysisClientDetailExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("终端访问次数");
//        List<JSONObject> all = clientVisitManager.getClientVisitCntToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("目的设备编号");
//        titles.add("目的设备IP");
//        titles.add("目的设备端口");
//        titles.add("目的设备MAC");
//        titles.add("目的设备名称");
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
//            row.add(j.get("dstCode"));
//            row.add(j.get("dstIp").toString());
//            row.add(j.get("dstPort"));
//            row.add(j.get("dstMac"));
//            row.add(j.get("dstDeviceName"));
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
//        ExportExcelUtils.exportExcel(response, "终端访问次数报表.xlsx", data);
//    }
//
//    private void getAnalysisClientExcelReport(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("终端访问");
//        List<JSONObject> all = clientVisitManager.getClientVisitToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("终端编号");
//        titles.add("IP地址");
//        titles.add("登录用户");
//        titles.add("操作类型");
//        titles.add("访问次数");
//        titles.add("相关摄像头数");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("CLIENT_CODE"));
//            row.add(j.get("CLIENT_IP"));
//            row.add(j.get("USERNAME"));
//            row.add(j.get("ACTION"));
//            row.add(j.get("VISIT_CNT").toString());
//            row.add(j.get("CAMERA_CNT").toString());
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "终端访问报表.xlsx", data);
//    }
//
//    private void rawSignalExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("原始信令");
//        List<JSONObject> all = rawSignalManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备IP");
//        titles.add("源设备编码");
//        titles.add("目的设备IP");
//        titles.add("目的设备编码");
//        titles.add("时间");
//        titles.add("信令内容");
//        data.setTitles(titles);
//
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("srcIp"));
//            row.add(j.get("fromCode"));
//            row.add(j.get("dstIp"));
//            row.add(j.get("toCode"));
//            row.add(j.get("stamp"));
//            row.add(j.get("content"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "原始信令报表.xlsx", data);
//    }
//
//    private void rawNetFlowExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("原始流量");
//        List<JSONObject> all = rawNetFlowManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备IP");
//        titles.add("源端口");
//        titles.add("源MAC");
//        titles.add("目的设备IP");
//        titles.add("目的端口");
//        titles.add("目的MAC");
//        titles.add("时间");
//        titles.add("协议类型");
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
//            row.add(j.get("packetType"));
//            row.add(j.get("packetSize"));
//            rows.add(row);
//        }
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "原始流量报表.xlsx", data);
//    }
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
//
//    public void operationBehaviorSessionExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("行为会话");
//        List<JSONObject> all = operationBehaviorSessionManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("源设备编号");
//        titles.add("源设备IP");
//        titles.add("登录帐号");
//        titles.add("上行流量");
//        titles.add("下行流量");
//        titles.add("开始时间");
//        titles.add("时长");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("srcCode"));
//            row.add(j.get("srcIp"));
//            row.add(j.get("username"));
//            row.add(j.get("upFlow"));
//            row.add(j.get("downFlow"));
//            row.add(j.get("startTime"));
//            row.add(j.get("totalTime"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "行为会话报表.xlsx", data);
//    }
//
//    public void operationBehaviorExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("操作行为");
//        List<JSONObject> all = operationBehaviorManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("终端IP");
//        titles.add("摄像头IP");
//        titles.add("摄像头编号");
//        titles.add("摄像头名称");
//        titles.add("动作");
//        titles.add("动作详细");
//        titles.add("上行流量");
//        titles.add("下行流量");
//        titles.add("登录帐号");
//        titles.add("时间");
//        titles.add("结果");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("srcIp"));
//            row.add(j.get("dstIp"));
//            row.add(j.get("dstCode"));
//            row.add(j.get("dstDeviceName"));
//            row.add(j.get("action"));
//            row.add(j.get("actionDetail"));
//            row.add(j.get("upFlow"));
//            row.add(j.get("downFlow"));
//            row.add(j.get("username"));
//            row.add(j.get("stamp"));
//            row.add(j.get("result"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "操作行为报表.xlsx", data);
//    }

    public void ServerTreeExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("服务器资源树");
        List<JSONObject> all = serverTreeService.getAllToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("服务器编号");
        titles.add("服务器名称");
        titles.add("服务器IP");
        titles.add("服务器域名");
        titles.add("服务器类型");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("serverCode"));
            row.add(j.get("serverName"));
            row.add(j.get("serverIp"));
            row.add(j.get("serverDomain"));
            row.add(j.get("serverType"));
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "服务器资源树报表.xlsx", data);
    }

    public AjaxResult CameraExcel(JSONObject filters) throws Exception {
        Camera cameraCond = new Camera();
        // TODO: 拼cameraCond
        List<Camera> list = cameraService.getList(cameraCond);
        ExcelUtil<Camera> util = new ExcelUtil<>(Camera.class);
        return util.exportExcel(list, "camera");
    }

    public void ClientExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("终端");
        List<JSONObject> all = clientTerminalService.getAllToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("设备编号");
        titles.add("IP地址");
        titles.add("MAC地址");
        titles.add("账户数");
        titles.add("状态");
        titles.add("最后更新时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (JSONObject j : all) {
            List<Object> row = new ArrayList();
            row.add(j.get("deviceCode"));
            row.add(j.get("ipAddress"));
            row.add(j.get("macAddress"));
            row.add(j.get("usercnt"));
            row.add(j.get("status"));
            row.add(j.get("updateTime"));
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "终端报表.xlsx", data);
    }

    public void ClientUserExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("用户");
        List<JSONObject> all = clientUserService.getAllToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("用户名");
        titles.add("终端数");
        titles.add("状态");
        titles.add("最后更新时间");
        data.setTitles(titles);
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

        ExportExcelUtils.exportExcel(response, "终端用户报表.xlsx", data);
    }

//    public void eventExcel(HttpServletResponse response, JSONObject filters) throws Exception {
//        ExcelData data = new ExcelData();
//        data.setName("告警事件");
//        List<JSONObject> all = eventDetailManager.getAllToReport(filters);
//        List<String> titles = new ArrayList();
//        titles.add("终端IP");
//        titles.add("摄像头名称");
//        titles.add("事件类型");
//        titles.add("事件来源");
//        titles.add("报警等级");
//        titles.add("操作行为");
//        titles.add("状态");
//        titles.add("时间");
//        data.setTitles(titles);
//        List<List<Object>> rows = new ArrayList();
//        for (JSONObject j : all) {
//            List<Object> row = new ArrayList();
//            row.add(j.get("clientIp"));
//            row.add(j.get("cameraName"));
//            row.add(j.get("eventCategory"));
//            row.add(j.get("eventSource"));
//            row.add(j.get("eventLevel"));
//            row.add(j.get("action"));
//            row.add(j.get("status"));
//            row.add(j.get("startTime"));
//            rows.add(row);
//        }
//
//        data.setRows(rows);
//
//        ExportExcelUtils.exportExcel(response, "告警事件报表.xlsx", data);
//    }

    public void CameraVisitRateExcel(HttpServletResponse response, JSONObject filters) throws Exception {
        ExcelData data = new ExcelData();
        data.setName("摄像头访问率");
        List<VisitRate> all = visitRateService.getAllToReport(filters);
        List<String> titles = new ArrayList();
        titles.add("地区名称");
        titles.add("摄像头数");
        titles.add("被访问摄像头");
        titles.add("访问率");
        titles.add("访问次数");
        titles.add("终端数");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (VisitRate visitRate : all) {
            List<Object> row = new ArrayList();
            row.add(visitRate.getCityName());
            row.add(visitRate.getCameraCnt());
            row.add(visitRate.getVisitedCnt());
            row.add(visitRate.getRate());
            row.add(visitRate.getVisitCnt());
            row.add(visitRate.getClientCnt());
            rows.add(row);
        }

        data.setRows(rows);

        ExportExcelUtils.exportExcel(response, "访问率统计报表.xlsx", data);
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
}