package com.yuanqing.project.tiansu.domain.report;

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

public class ReadServerExcel {
    // 总行数
    private int totalRows = 0;
    // 总条数
    private int totalCells = 0;
    // 错误信息接收器
    private String errorMsg;

    // 构造方法
    public ReadServerExcel() {
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
        // 获取文件名
        String fileName = mFile.getOriginalFilename();
//        List<Map<String, Object>> userList = new LinkedList<Map<String, Object>>();
        try {
            // 验证文件名是否合格
            if (!validateExcel(fileName)) {
                return null;
            }
            // 根据文件名判断文件是2003版本还是2007版本
            boolean isExcel2003 = true;
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
            // 当excel是2003时,创建excel2003
            if (isExcel2003) {
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            // 读取Excel里面客户的信息
            return readExcelValue(wb);
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
        List<Map<String, Object>> clientUserList = new ArrayList<Map<String, Object>>();
        // 循环Excel行数
        for (int r = 1; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            /**
             * @param:servercode 服务器编号
             * @param:serverName 服务器名称
             * @param:serverIp 服务器IP
             * @param:serverType 服务器类型
             * @param:serverDomain 服务器域
             */
            Map<String, Object> map = new HashMap<String, Object>();
            for (int c = 0; c < this.totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String servercode = String.valueOf(cell.getStringCellValue());
                            map.put("servercode", servercode);
                        } else {
                            map.put("servercode", "");
                        }
                    }else if (c == 1) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String serverName = String.valueOf(cell.getStringCellValue());
                            map.put("serverName", serverName);
                        } else {
                            map.put("serverName", "");
                        }
                    }else if (c == 2) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String serverIp = String.valueOf(cell.getStringCellValue());
                            map.put("serverIp", serverIp);
                        } else {
                            map.put("serverIp", "");
                        }
                    } else if (c == 3) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String serverType = String.valueOf(cell.getStringCellValue());
                            map.put("serverType", serverType);
                        } else {
                            map.put("serverType", "");
                        }
                    } else if (c == 4) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (!cell.getStringCellValue().equals("")&&cell.getStringCellValue()!=null) {
                            String serverDomain = String.valueOf(cell.getStringCellValue());
                            map.put("serverDomain", serverDomain);
                        } else {
                            map.put("serverDomain", "");
                        }
                    }

                }
            }
            // 添加到list
            clientUserList.add(map);
        }
        return clientUserList;
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



}
