package com.yuanqing.project.tiansu.domain.assets;

import com.yuanqing.common.utils.ip.IpUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReadExcel {

    // 总行数
    private int totalRows = 0;
    // 总条数
    private int totalCells = 0;
    // 错误信息接收器
    private String errorMsg;

    // 构造方法
    public ReadExcel() {
    }

    // 获取总行数
    public int getTotalRows() {
        return totalRows;
    }

    // 获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    // 获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }

    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> getExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();// 获取文件名
//        List<Map<String, Object>> userList = new LinkedList<Map<String, Object>>();
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            return createExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is      输入流
     * @param isExcel2003   excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> createExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            return readExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<Map<String, Object>> readExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Map<String, Object>> cameraList = new ArrayList<Map<String, Object>>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            Map<String, Object> map = new HashMap<String, Object>();
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String deviceCode = String.valueOf(cell.getStringCellValue());
                            map.put("deviceCode", deviceCode);// 设备编号
                        } else {
                            map.put("deviceCode", "");// 设备编号
                        }
                    } else if (c == 1) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            String deviceDomain = String.valueOf(cell.getStringCellValue());
                            map.put("deviceDomain",deviceDomain);// 设备域
                        } else {
                            map.put("deviceDomain","");// 设备域
                        }
                    } else if (c == 2) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            String deviceName = String.valueOf(cell.getStringCellValue());
                            map.put("deviceName", deviceName);// 设备名称
                        } else {
                            map.put("deviceName", "");// 设备名称
                        }
                    }else if (c == 3) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            String macAddress = String.valueOf(cell.getStringCellValue());
                            map.put("macAddress",macAddress);// MAC地址
                        } else {
                            map.put("macAddress","");// MAC地址
                        }
                    } else if (c == 4) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            Double longitude = Double.valueOf(String.valueOf(cell.getStringCellValue()));
                            map.put("longitude", longitude);// 经度
                        } else {
                            map.put("longitude", "");// 经度
                        }
                    }else if (c == 5) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            Double latitude = Double.valueOf(String.valueOf(cell.getStringCellValue()));
                            map.put("latitude",latitude);// 纬度
                        } else {
                            map.put("latitude","");// 纬度
                        }
                    } else if (c == 6) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String region = String.valueOf(cell.getStringCellValue());
                            map.put("region", region);// 行政区域
                        } else {
                            map.put("region", "");// 行政区域
                        }
                    }else if (c == 7) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String ip = String.valueOf(cell.getStringCellValue());
                            Long ipAddress= IpUtils.getIp2long(ip);
                            map.put("ipAddress",ipAddress);// IP地址
                        } else {
                            map.put("ipAddress","");// IP地址
                        }
                    } else if (c == 8) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String age = String.valueOf(cell.getStringCellValue());
                            if(age.equals("已确认")||age.equals("0")){
                                map.put("status", "已确认");// 状态
                            }else if(age.equals("新发现")||age.equals("1")){
                                map.put("status", "新发现");// 状态
                            }else if(age.equals("变更")||age.equals("2")){
                                map.put("status", "变更");// 状态
                            }else if(age.equals("未授权")||age.equals("3")){
                                map.put("status", "未授权");
                            }
                        } else {
                            map.put("status", "新发现");// 状态
                        }
                    }else if (c == 9) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String deviceType = String.valueOf(cell.getStringCellValue());
                            map.put("deviceType",deviceType);// 设备类型
                        } else {
                            map.put("deviceType","");// 设备类型
                        }
                    } else if (c == 10) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String ip = String.valueOf(cell.getStringCellValue());
                            Long domainIp=IpUtils.ipToLong(ip);
                            map.put("domainIp", domainIp);// 域IP
                        } else {
                            map.put("domainIp", "");// 域IP
                        }
                    }else if (c == 11) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String age = String.valueOf(cell.getStringCellValue());
                            Long domainPort=Long.valueOf(age);
                            map.put("domainPort", domainPort);// 域端口
                        } else {
                            map.put("domainPort", "");// 域端口
                        }
                    } else if (c == 12) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String age = String.valueOf(cell.getStringCellValue());
                            if(age.equals("非国标")||age.equals("0")){
                                map.put("isGb", 0);// 是否国标
                            }else if(age.equals("国标")||age.equals("1")){
                                map.put("isGb", 1);// 是否国标
                            }
                        } else {
                            map.put("isGb", "");// 是否国标
                        }
                    }
                }
            }
            // 添加到list
            cameraList.add(map);
        }
        return cameraList;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    // @描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    /**
     * 读EXCEL文件，获取信息集合
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> getExtCameraExcelInfo(MultipartFile mFile) {
        String fileName = mFile.getOriginalFilename();// 获取文件名
//        List<Map<String, Object>> userList = new LinkedList<Map<String, Object>>();
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            return createExtCameraExcel(mFile.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据excel里面的内容读取客户信息
     *
     * @param is      输入流
     * @param isExcel2003   excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> createExtCameraExcel(InputStream is, boolean isExcel2003) {
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            return readExtCameraExcelValue(wb);// 读取Excel里面客户的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private List<Map<String, Object>> readExtCameraExcelValue(Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Map<String, Object>> cameraList = new ArrayList<Map<String, Object>>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            Map<String, Object> map = new HashMap<String, Object>();
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String gbId = String.valueOf(cell.getStringCellValue());
                            map.put("gbId", gbId);// 国标编码
                        } else {
                            map.put("gbId", "");// 国标编码
                        }
                    } else if (c == 1) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String deviceId = String.valueOf(cell.getStringCellValue());
                            map.put("deviceId", deviceId);// 内部编码
                        } else {
                            map.put("deviceId", "");// 内部编码
                        }
                    } else if (c == 2) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            String deviceName = String.valueOf(cell.getStringCellValue());
                            map.put("deviceName", deviceName);// 设备名称
                        } else {
                            map.put("deviceName", "");// 设备名称
                        }
                    }else if (c == 3) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {

                            String macAddress = String.valueOf(cell.getStringCellValue());
                            map.put("macAddress",macAddress);// MAC地址
                        } else {
                            map.put("macAddress","");// MAC地址
                        }
                    } else if (c == 4) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            try {
                                Double longitude = Double.valueOf(String.valueOf(cell.getStringCellValue()));
                                map.put("longitude", longitude);// 经度
                            }catch (Exception e){
                                map.put("longitude", "");// 经度
                            }
                        } else {
                            map.put("longitude", "");// 经度
                        }
                    }else if (c == 5) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            try {
                                Double latitude = Double.valueOf(String.valueOf(cell.getStringCellValue()));
                                map.put("latitude",latitude);// 纬度
                            }catch (Exception e){
                                map.put("latitude","");// 纬度
                            }
                        } else {
                            map.put("latitude","");// 纬度
                        }
                    } else if (c == 6) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String region = String.valueOf(cell.getStringCellValue());
                            map.put("region", region);// 行政区域
                        } else {
                            map.put("region", "");// 行政区域
                        }
                    }else if (c == 7) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String ip = String.valueOf(cell.getStringCellValue());
//                            Long ipAddress=IPv4Util.getIp2long(ip);
                            map.put("ipAddress",ip);// IP地址
                        } else {
                            map.put("ipAddress","");// IP地址
                        }
                    } else if (c == 8) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String manufacturer = String.valueOf(cell.getStringCellValue());
                            map.put("manufacturer",manufacturer);// 设备厂商
                        } else {
                            map.put("manufacturer","");// 设备厂商
                        }
                    }
                }
            }
            // 添加到list
            cameraList.add(map);
        }
        return cameraList;
    }
}