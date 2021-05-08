package com.yuanqing.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.yuanqing.common.enums.DeviceType;
import com.yuanqing.common.utils.ip.IpUtils;
import com.yuanqing.project.tiansu.domain.assets.Camera;
import com.yuanqing.project.tiansu.domain.assets.Client;
import com.yuanqing.project.tiansu.domain.assets.ServerTree;
import com.yuanqing.project.tiansu.domain.report.VisitRate;
import com.yuanqing.project.tiansu.mapper.analysis.VisitRateMapper;
import com.yuanqing.project.tiansu.service.assets.ICameraService;
import com.yuanqing.project.tiansu.service.assets.IClientService;
import com.yuanqing.project.tiansu.service.assets.IServerTreeService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExcelMethods {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExcelMethods.class);

    @Resource
    private IClientService clientService;

    @Resource
    private ICameraService cameraService;

    @Resource
    private IServerTreeService serverTreeService;

//    @Resource
//    private EventDetailManager eventDetailManager;
//
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

    @Resource
    private VisitRateMapper visitRateMapper;

//    @Resource
//    private FailAccessManager failAccessManager;
//
//    @Resource
//    private SelfAuditManager selfAuditManager;

    public File clientReport(JSONObject filters) throws Exception {
        Client clientCond = new Client();
        // TODO: clientCond条件
        List<Client> list = clientService.getList(clientCond);

        if (list == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String time = LocalDateTime.now().format(formatter);
        String fileName = "客户端报表_" + time + ".xls";


        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("sheet1");

        HSSFCellStyle titleStyle = getTitleStyle(book);
        HSSFCellStyle cellStyle = getCellStyle(book);

        String[] tableHeader = {"设备编号", "IP地址", "MAC地址", "登陆账号", "状态", "最后更新时间"};

        HSSFRow row = null;
        HSSFCell cell = null;
        int rowIndex = 1;
        int i = 0;
        row = sheet.createRow(0);
        for (String string : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(string);
            cell.setCellStyle(titleStyle);
            i++;
        }

        for (Client entity : list) {
            row = sheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(entity.getDeviceCode());
            cell.setCellStyle(cellStyle);


            cell = row.createCell(1);
            cell.setCellValue(IpUtils.longToIp(entity.getIpAddress()));
            cell.setCellStyle(cellStyle);


            cell = row.createCell(2);
            cell.setCellValue(entity.getMacAddress());
            cell.setCellStyle(cellStyle);


            cell = row.createCell(3);
            cell.setCellValue(entity.getUsername());
            cell.setCellStyle(cellStyle);


            cell = row.createCell(4);
            cell.setCellValue(entity.getStatus());
            cell.setCellStyle(cellStyle);


            cell = row.createCell(5);
            cell.setCellValue(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, entity.getUpdateTime()));
            cell.setCellStyle(cellStyle);


            rowIndex++;
        }

        for (int j = 0; j < tableHeader.length; j++) {
            if (j == 0) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(j);
                cell.setCellValue("生成日期：");
                cell.setCellStyle(cellStyle);
            } else if (j == 1) {
                cell = row.createCell(j);
                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                cell.setCellStyle(cellStyle);
            } else {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle);
            }
            sheet.autoSizeColumn(j);
        }


        return generateFile(filters.getString("type"), time, fileName, book);
    }

    public File cameraReport(JSONObject filters) throws Exception {
        Camera cameraCond = new Camera();
        // TODO: 拼cameraCond
        List<Camera> list = cameraService.getList(cameraCond);

        if (list == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String time = LocalDateTime.now().format(formatter);
        String fileName = "摄像头报表_" + time + ".xls";


        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("sheet1");

        HSSFCellStyle titleStyle = getTitleStyle(book);
        HSSFCellStyle cellStyle = getCellStyle(book);

        String[] tableHeader = {"设备编号", "摄像头名称", "IP地址", "所在地区", "状态", "最后更新时间"};

        HSSFRow row = null;
        HSSFCell cell = null;
        int rowIndex = 1;
        int i = 0;
        row = sheet.createRow(0);
        for (String string : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(string);
            cell.setCellStyle(titleStyle);
            i++;
        }

        for (Camera entity : list) {
            row = sheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(entity.getDeviceCode());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(entity.getDeviceName());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            if (entity.getIpAddress() == null || entity.getIpAddress() == 0L) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(IpUtils.longToIp(entity.getIpAddress()));
            }
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            if (entity.getRegion() != null) {
                cell.setCellValue(entity.getRegion());
            } else {
                cell.setCellValue("");
            }
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(entity.getStatus());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, entity.getUpdateTime()));
            cell.setCellStyle(cellStyle);

            rowIndex++;
        }

        for (int j = 0; j < tableHeader.length; j++) {
            if (j == 0) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(j);
                cell.setCellValue("生成日期：");
                cell.setCellStyle(cellStyle);
            } else if (j == 1) {
                cell = row.createCell(j);
                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                cell.setCellStyle(cellStyle);
            } else {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle);
            }
            sheet.autoSizeColumn(j);
        }

        return generateFile(filters.getString("type"), time, fileName, book);
    }

    public File serverReport(JSONObject filters) throws Exception {
        ServerTree serverTreeCond = new ServerTree();
        // TODO: 拼serverTreeCond
        List<ServerTree> list = serverTreeService.getList(serverTreeCond);

        if (list == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String time = LocalDateTime.now().format(formatter);
        String fileName = "服务器资源报表_" + time + ".xls";


        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("sheet1");

        HSSFCellStyle titleStyle = getTitleStyle(book);
        HSSFCellStyle cellStyle = getCellStyle(book);

        String[] tableHeader = {"服务器编号", "服务器名称", "服务器IP", "服务器域名", "服务器类型"};

        HSSFRow row = null;
        HSSFCell cell = null;
        int rowIndex = 1;
        int i = 0;
        row = sheet.createRow(0);
        for (String string : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(string);
            cell.setCellStyle(titleStyle);
            i++;
        }

        for (ServerTree entity : list) {
            row = sheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(entity.getServerCode());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(entity.getServerName());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(IpUtils.longToIp(entity.getServerIp()));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(entity.getServerDomain());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(DeviceType.getLabelByValue(entity.getServerType()));
            cell.setCellStyle(cellStyle);

            rowIndex++;
        }

        row = sheet.createRow(rowIndex);
        for (int j = 0; j < tableHeader.length; j++) {
            if (j == 0) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(j);
                cell.setCellValue("生成日期：");
                cell.setCellStyle(cellStyle);
            } else if (j == 1) {
                cell = row.createCell(j);
                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                cell.setCellStyle(cellStyle);
            } else {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle);
            }
            sheet.autoSizeColumn(j);
        }

        return generateFile(filters.getString("type"), time, fileName, book);
    }

//    public File eventReport(JSONObject filters) throws Exception {
//        List<EventDetail> list = eventDetailManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "告警事件报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"客户端IP", "摄像头名称", "事件类型", "事件来源", "报警等级", "操作行为", "状态", "时间"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (EventDetail entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(IPv4Util.longToIp(entity.getClientIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(entity.getCameraName());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getEventCategory());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(typeFormatter(entity.getEventSource()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(eventLevelFormatter(entity.getEventLevel()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            if (entity.getAction() != null) {
//                cell.setCellValue(entity.getAction().getLabel());
//            } else {
//                cell.setCellValue("");
//            }
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getStatus().getLabel());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue(entity.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File sessionReport(JSONObject filters) throws Exception {
//        List<OperationBehaviorSession> list = operationBehaviorSessionManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "行为会话报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"源设备编号", "源设备IP", "登录帐号", "上行流量", "下行流量", "开始时间", "时长"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (OperationBehaviorSession entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(entity.getSrcCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getUsername());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(flowFormatter(entity.getUpFlow()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(flowFormatter(entity.getDownFlow()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue(entity.getTotalTime());
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File operReport(JSONObject filters) throws Exception {
//        List<OperationBehavior> list = operationBehaviorManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "操作行为报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"客户端IP", "摄像头IP", "摄像头编号", "摄像头名称", "动作", "动作详细", "上行流量", "下行流量", "登录帐号", "时间", "结果"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (OperationBehavior entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(IPv4Util.longToIp(entity.getDstIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getDstCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(entity.getDstDeviceName());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getAction().getLabel());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getActionDetail().getLabel());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue(flowFormatter(entity.getUpFlow()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(7);
//            cell.setCellValue(flowFormatter(entity.getDownFlow()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(8);
//            cell.setCellValue(entity.getUsername());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(9);
//            cell.setCellValue(entity.getStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(10);
//            cell.setCellValue(resultFormatter(entity.getResult()));
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File signalReport(JSONObject filters) throws Exception {
//        List<RawSignal> list = rawSignalManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "原始信令报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"源设备IP", "源设备编码", "目的设备IP", "目的设备编码", "时间", "信令内容"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (RawSignal entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(entity.getFromCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(IPv4Util.longToIp(entity.getDstIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(entity.getToCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getContent());
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File flowReport(JSONObject filters) throws Exception {
//        List<RawNetFlow> list = rawNetFlowManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "原始流量报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"源设备IP", "源端口", "源MAC", "目的设备IP", "目的端口", "目的MAC", "时间", "协议类型", "通信包大小"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (RawNetFlow entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(entity.getSrcPort());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getSrcMac());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(IPv4Util.longToIp(entity.getDstIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getDstPort());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getDstMac());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue(entity.getStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(7);
//            cell.setCellValue(entity.getPacketType());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(8);
//            cell.setCellValue(entity.getPacketSize());
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File remoteReport(JSONObject filters) throws Exception {
//        List<RemoteRecord> list = remoteRecordManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "远程访问报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"源设备IP", "源端口", "源MAC", "目的设备IP", "目的端口", "目的MAC", "时间", "远程访问类型", "通信包大小"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (RemoteRecord entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(entity.getSrcPort());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getSrcMac());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(IPv4Util.longToIp(entity.getDstIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getDstPort());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getDstMac());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(6);
//            cell.setCellValue(entity.getStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(7);
//            cell.setCellValue(entity.getRemoteType());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(8);
//            cell.setCellValue(entity.getPacketSize());
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

    public File rateReport(JSONObject filters) throws Exception {
        String startDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String endDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        filters.put("startDate", startDate);
        filters.put("endDate", endDate);
        List<VisitRate> list = visitRateMapper.getRateList(filters);

        if (list == null) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String time = LocalDateTime.now().format(formatter);
        String fileName = "摄像头访问率报表_" + time + ".xls";


        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("sheet1");

        HSSFCellStyle titleStyle = getTitleStyle(book);
        HSSFCellStyle cellStyle = getCellStyle(book);

        String[] tableHeader = {"地区名称", "摄像头数", "被访问摄像头", "访问率", "访问次数", "访问客户端数"};

        HSSFRow row = null;
        HSSFCell cell = null;
        int rowIndex = 1;
        int i = 0;
        row = sheet.createRow(0);
        for (String string : tableHeader) {
            cell = row.createCell(i);
            cell.setCellValue(string);
            cell.setCellStyle(titleStyle);
            i++;
        }

        for (VisitRate entity : list) {
            row = sheet.createRow(rowIndex);

            cell = row.createCell(0);
            cell.setCellValue(entity.getCityName().toString());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue(entity.getCameraCnt().toString());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue(entity.getVisitedCnt().toString());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue(entity.getRate());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue(entity.getVisitCnt().toString());
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue(entity.getClientCnt().toString());
            cell.setCellStyle(cellStyle);

            rowIndex++;
        }

        row = sheet.createRow(rowIndex);
        for (int j = 0; j < tableHeader.length; j++) {
            if (j == 0) {
                row = sheet.createRow(rowIndex);
                cell = row.createCell(j);
                cell.setCellValue("生成日期：");
                cell.setCellStyle(cellStyle);
            } else if (j == 1) {
                cell = row.createCell(j);
                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                cell.setCellStyle(cellStyle);
            } else {
                cell = row.createCell(j);
                cell.setCellValue("");
                cell.setCellStyle(cellStyle);
            }
            sheet.autoSizeColumn(j);
        }

        return generateFile(filters.getString("type"), time, fileName, book);
    }

//    public File failReport(JSONObject filters) throws Exception {
//        List<FailAccess> list = failAccessManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "访问失败报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"源设备编码", "源IP", "目的设备编码", "目的IP", "错误类型", "时间"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        row = sheet.createRow(0);
//        int i = 0;
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (FailAccess entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(entity.getFromCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(IPv4Util.longToIp(entity.getSrcIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getToCode());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(IPv4Util.longToIp(entity.getDstIp()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getFailAccessCode().getLabel());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(5);
//            cell.setCellValue(entity.getStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

//    public File selfAuditReport(JSONObject filters) throws Exception {
//        List<SelfAudit> list = selfAuditManager.getAll(filters);
//
//        if (list == null) {
//            return null;
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        String time = LocalDateTime.now().format(formatter);
//        String fileName = "自审信息报表_" + time + ".xls";
//
//
//        HSSFWorkbook book = new HSSFWorkbook();
//        HSSFSheet sheet = book.createSheet("sheet1");
//
//        HSSFCellStyle titleStyle = getTitleStyle(book);
//        HSSFCellStyle cellStyle = getCellStyle(book);
//
//        String[] tableHeader = {"用户名", "客户端IP", "时间", "操作类型", "告警级别", "结果"};
//
//        HSSFRow row = null;
//        HSSFCell cell = null;
//        int rowIndex = 1;
//        int i = 0;
//        row = sheet.createRow(0);
//        for (String string : tableHeader) {
//            cell = row.createCell(i);
//            cell.setCellValue(string);
//            cell.setCellStyle(titleStyle);
//            i++;
//        }
//
//        for (SelfAudit entity : list) {
//            row = sheet.createRow(rowIndex);
//
//            cell = row.createCell(0);
//            cell.setCellValue(entity.getUsername());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(1);
//            cell.setCellValue(entity.getIpAddress());
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(2);
//            cell.setCellValue(entity.getStime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(3);
//            cell.setCellValue(typeFormatter(entity.getType()));
//            cell.setCellStyle(cellStyle);
//
//            cell = row.createCell(4);
//            cell.setCellValue(entity.getSeverity().getLabel());
//            cell.setCellStyle(cellStyle);
//
////            cell = row.createCell(5);
////            cell.setCellValue(resultFormatter(entity.getResult().toString()));
////            cell.setCellStyle(cellStyle);
//
//            rowIndex++;
//        }
//
//        row = sheet.createRow(rowIndex);
//        for (int j = 0; j < tableHeader.length; j++) {
//            if (j == 0) {
//                row = sheet.createRow(rowIndex);
//                cell = row.createCell(j);
//                cell.setCellValue("生成日期：");
//                cell.setCellStyle(cellStyle);
//            } else if (j == 1) {
//                cell = row.createCell(j);
//                cell.setCellValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//                cell.setCellStyle(cellStyle);
//            } else {
//                cell = row.createCell(j);
//                cell.setCellValue("");
//                cell.setCellStyle(cellStyle);
//            }
//            sheet.autoSizeColumn(j);
//        }
//
//        return generateFile(filters.getString("type"), time, fileName, book);
//    }

    private String typeFormatter(String type) {
        if (type.equals("LOGIN")) return "登录";
        if (type.equals("LOGOUT")) return "退出登录";
        if (type.equals("GET")) return "获取资源";
        if (type.equals("PUT")) return "修改数据";
        if (type.equals("POST")) return "插入数据";
        if (type.equals("DELETE")) return "删除数据";
        return type;
    }

    private String resultFormatter(String result) {
        if (result == null) return "未知";
        if (result.equals("true")) return "成功";
        if (result.equals("false")) return "失败";
        if (result.equals("1")) return "成功";
        if (result.equals("0")) return "失败";
        return result;
    }

    private String eventLevelFormatter(String eventLevel) {
        if (eventLevel.equals("0")) return "一般";
        if (eventLevel.equals("1")) return "重要";
        if (eventLevel.equals("2")) return "严重";
        return eventLevel;
    }

    private static String flowFormatter(Long flow) {
        if (flow < Math.pow(1024, 1)) return flow + "B";
        if (flow < Math.pow(1024, 2)) return String.format("%.2f", (flow / Math.pow(1024, 1))) + "K";
        if (flow < Math.pow(1024, 3)) return String.format("%.2f", (flow / Math.pow(1024, 2))) + "M";
        if (flow < Math.pow(1024, 4)) return String.format("%.2f", (flow / Math.pow(1024, 3))) + "G";
        if (flow < Math.pow(1024, 5)) return String.format("%.2f", (flow / Math.pow(1024, 4))) + "T";
        return flow + "B";
    }

    private File generateFile(String reportType, String time, String fileName, HSSFWorkbook book) throws Exception {
        File file = null;
        if (SystemUtil.currSystem()) {
            //linux路径
            file = new File("/usr/local/tiansu/report" + reportType + "/" + time + "/" + fileName);
        } else if(SystemUtil.macSystem()) {
            //mac路径
            file = new File("/users/xucan/yuanqing/report" + reportType + "/" + time + "/" + fileName);
        }else{
            //Windows路径
            file = new File("D:\\report\\" + reportType + "\\" + time + "\\" + fileName);
        }


        file.getParentFile().mkdirs();
        file.createNewFile();
        OutputStream stream = null;
        stream = new FileOutputStream(file);
        book.write(stream);
        stream.close();
        LOGGER.info("生成{}文件成功", fileName);
        return file;
    }


    private HSSFCellStyle getTitleStyle(HSSFWorkbook book) {
        Font titleFont = book.createFont();
        titleFont.setFontName("宋体");
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setColor(IndexedColors.BLACK.index);

        HSSFCellStyle titleStyle = book.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        return titleStyle;
    }

    private HSSFCellStyle getCellStyle(HSSFWorkbook book) {
        Font cellFont = book.createFont();
        cellFont.setFontName("宋体");
        cellFont.setColor(IndexedColors.BLACK.index);

        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFont(cellFont);

        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        return cellStyle;
    }

}
