package com.yuanqing.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.yuanqing.project.tiansu.domain.macs.MacsConfig;
import com.yuanqing.project.tiansu.service.analysis.IVisitRateService;
import com.yuanqing.project.tiansu.service.macs.IMacsConfigService;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ReportUtil {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);

    @Resource
    private ExcelMethods excelMethods;

    @Resource
    private IVisitRateService visitRateService;

    @Resource
    private IMacsConfigService macsConfigService;

    @Async
    public void generateReport(String type) throws Exception {

        if (type.equals("DayReport")) {
            this.generateAndSendDayReport();
        } else if (type.equals("WeekReport")) {
            this.generateAndSendWeekReport();
        } else if (type.equals("MonthReport")) {
            this.generateAndSendMonthReport();
        }


    }

    private void generateAndSendDayReport() throws Exception {
        //        JSONObject dayReport = StrategyMap.get("DayReport");
        MacsConfig macsConfig = macsConfigService.selectMacsConfigByTypeAndName("DayReport", "data");
        if(macsConfig == null) {
            return;
        }
        String[] arr = macsConfig.getValue().split(",");
        String type = macsConfig.getType();

        JSONObject filters = new JSONObject();

        String stime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String etime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        filters.put("stime", stime);
        filters.put("etime", etime);
        filters.put("type", "DayReport");

        for (String s : arr) {
            if (s.equals("0")) {
//                File file = this.sendToType(excelMethods.clientReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("1")) {
                File file = this.sendToType(excelMethods.cameraReport(filters),type);
                this.sendToFTP("DayReport", file);
            } else if (s.equals("2")) {
                File file = this.sendToType(excelMethods.serverReport(filters),type);
                this.sendToFTP("DayReport", file);
            } else if (s.equals("3")) {
//                File file = this.sendToType(excelMethods.eventReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("4")) {
//                File file = this.sendToType(excelMethods.sessionReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("5")) {
//                File file = this.sendToType(excelMethods.operReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("6")) {
//                File file = this.sendToType(excelMethods.signalReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("7")) {
//                File file = this.sendToType(excelMethods.flowReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("8")) {
//                File file = this.sendToType(excelMethods.remoteReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("9")) {
                //getRateList需要地区码，和长度
                String cityCode = "";
                cityCode = macsConfigService.getRegion(cityCode);

                if (cityCode.length() == 6){
                    filters.put("length", cityCode.length());
                } else {
                    filters.put("length", cityCode.length() + 2);
                }
                filters.put("cityCode", cityCode);
                filters.put("stime", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                filters.put("etime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                File file = this.sendToType(excelMethods.rateReport(filters),type);
                this.sendToFTP("DayReport", file);
            } else if (s.equals("10")) {
//                File file = this.sendToType(excelMethods.failReport(filters),type);
//                this.sendToFTP("DayReport", file);
            } else if (s.equals("11")) {
//                File file = this.sendToType(excelMethods.selfAuditReport(filters),type);
//                this.sendToFTP("DayReport", file);
            }
        }


    }

    private void generateAndSendWeekReport() throws Exception {

//        JSONObject dayReport = StrategyMap.get("WeekReport");
//        String[] arr = dayReport.get("data").toString().split(",");
//        String type = dayReport.get("type").toString();
        MacsConfig macsConfig = macsConfigService.selectMacsConfigByTypeAndName("WeekReport", "data");
        if(macsConfig == null) {
            return;
        }
        String[] arr = macsConfig.getValue().split(",");
        String type = macsConfig.getType();

        JSONObject filters = new JSONObject();

        String stime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.MONDAY), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String etime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        filters.put("stime", stime);
        filters.put("etime", etime);
        filters.put("type", "WeekReport");

        for (String s : arr) {
            if (s.equals("0")) {
//                File file = this.sendToType(excelMethods.clientReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("1")) {
                File file =this.sendToType(excelMethods.cameraReport(filters),type);
                this.sendToFTP("WeekReport", file);
            } else if (s.equals("2")) {
                File file =this.sendToType(excelMethods.serverReport(filters),type);
                this.sendToFTP("WeekReport", file);
            } else if (s.equals("3")) {
//                File file = this.sendToType(excelMethods.eventReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("4")) {
//                File file = this.sendToType(excelMethods.sessionReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("5")) {
//                File file = this.sendToType(excelMethods.operReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("6")) {
//                File file = this.sendToType(excelMethods.signalReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("7")) {
//                File file = this.sendToType(excelMethods.flowReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("8")) {
//                File file = this.sendToType(excelMethods.remoteReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("9")) {
                //getRateList需要地区码，和长度
                String cityCode = "";
                cityCode = macsConfigService.getRegion(cityCode);

                if (cityCode.length() == 6){
                    filters.put("length", cityCode.length());
                } else {
                    filters.put("length", cityCode.length() + 2);
                }
                filters.put("cityCode", cityCode);
                filters.put("stime", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                filters.put("etime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                File file = this.sendToType(excelMethods.rateReport(filters),type);
                this.sendToFTP("WeekReport", file);
            } else if (s.equals("10")) {
//                File file = this.sendToType(excelMethods.failReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            } else if (s.equals("11")) {
//                File file = this.sendToType(excelMethods.selfAuditReport(filters),type);
//                this.sendToFTP("WeekReport", file);
            }
        }


    }

    private void generateAndSendMonthReport() throws Exception {

//        JSONObject dayReport = StrategyMap.get("MonthReport");
//        String[] arr = dayReport.get("data").toString().split(",");
//        String type = dayReport.get("type").toString();
        MacsConfig macsConfig = macsConfigService.selectMacsConfigByTypeAndName("MonthReport", "data");
        if(macsConfig == null) {
            return;
        }
        String[] arr = macsConfig.getValue().split(",");
        String type = macsConfig.getType();

        JSONObject filters = new JSONObject();

        String stime = LocalDateTime.of(LocalDate.now().minusDays(LocalDateTime.now().getDayOfMonth() - 1), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        String etime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        filters.put("stime", stime);
        filters.put("etime", etime);
        filters.put("type", "MonthReport");

        for (String s : arr) {
            if (s.equals("0")) {
//                File file = this.sendToType(excelMethods.clientReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("1")) {
                File file = this.sendToType(excelMethods.cameraReport(filters),type);
                this.sendToFTP("MonthReport", file);
            } else if (s.equals("2")) {
                File file = this.sendToType(excelMethods.serverReport(filters),type);
                this.sendToFTP("MonthReport", file);
            } else if (s.equals("3")) {
//                File file = this.sendToType(excelMethods.eventReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("4")) {
//                File file = this.sendToType(excelMethods.sessionReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("5")) {
//                File file = this.sendToType(excelMethods.operReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("6")) {
//                File file = this.sendToType(excelMethods.signalReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("7")) {
//                File file = this.sendToType(excelMethods.flowReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("8")) {
//                File file = this.sendToType(excelMethods.remoteReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("9")) {
                //getRateList需要地区码，和长度
                String cityCode = "";
                cityCode = macsConfigService.getRegion(cityCode);

                if (cityCode.length() == 6){
                    filters.put("length", cityCode.length());
                } else {
                    filters.put("length", cityCode.length() + 2);
                }
                filters.put("cityCode", cityCode);
                filters.put("stime", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                filters.put("etime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                File file = this.sendToType(excelMethods.rateReport(filters),type);
                this.sendToFTP("MonthReport", file);
            } else if (s.equals("10")) {
//                File file = this.sendToType(excelMethods.failReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            } else if (s.equals("11")) {
//                File file = this.sendToType(excelMethods.selfAuditReport(filters),type);
//                this.sendToFTP("MonthReport", file);
            }
        }


    }

    @Async
    public void sendToFTP(String type, File file) throws Exception {
//        String ip = StrategyMap.get(type).getString("ip");
//        int port = StrategyMap.get(type).getIntValue("port");
//        String username = StrategyMap.get(type).getString("username");
//        String password = StrategyMap.get(type).getString("password");
        MacsConfig ipConfig = macsConfigService.selectMacsConfigByTypeAndName(type, "ip");
        if(ipConfig == null) {
            return;
        }
        String ip = ipConfig.getValue();

        MacsConfig portConfig = macsConfigService.selectMacsConfigByTypeAndName(type, "port");
        if(portConfig == null) {
            return;
        }
        int port = Integer.valueOf(portConfig.getValue());

        MacsConfig usernameConfig = macsConfigService.selectMacsConfigByTypeAndName(type, "username");
        if(usernameConfig == null) {
            return;
        }
        String username = usernameConfig.getValue();

        MacsConfig passwordConfig = macsConfigService.selectMacsConfigByTypeAndName(type, "password");
        if(passwordConfig == null) {
            return;
        }
        String password = passwordConfig.getValue();

        FTPClient ftpClient = FtpUtil.connect(ip, port, username, password);

        FtpUtil.upload(file, ftpClient);

        LOGGER.info("发送{}文件成功", file.getName());
    }

    private File sendToType(File file,String type) throws Exception {
        switch (type){
            case "excel" : return file;
            case "pdf": return ExcelToPDF.excel2PDF(file);
            case "html":return ExcelToHtmlUtils.readExcelToHtml(file,true);
        }
        return file;
    }
}
