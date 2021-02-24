package com.yuanqing.common.utils;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;

/**
 * Created by xucan on 2019-11-20 16:40
 */
public class ExcelToPDF {

    private CellRangeAddress range;

    public static File excel2PDF(File sourceFile) throws IOException, DocumentException {
        InputStream in = new FileInputStream(sourceFile);//读取excel文件到流中

        String path = sourceFile.getParentFile().getAbsolutePath();
        String sfName = sourceFile.getName();
        String fileName = sfName.substring(0,sfName.lastIndexOf("."))+".pdf";

        File file = new File( path + "/" + fileName);


        try {
             return Excel2003(in,file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public static File Excel2007(InputStream in,File file) throws Exception {

        XSSFWorkbook workBook = new XSSFWorkbook(in);
        XSSFSheet sheet = workBook.getSheetAt(0);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);//设置pdf纸张大小
        PdfWriter writer = PdfWriter.getInstance(document, stream);
        document.setMargins(0, 0, 15, 15);//设置页边距
        document.open();
        float[] widths = getColWidth(sheet);//获取excel每列宽度占比

        PdfPTable table = new PdfPTable(widths);//初始化pdf中每列的宽度
        table.setWidthPercentage(88);
        int colCount = widths.length;
        String font = "";
        if (SystemUtil.currSystem()) {
            //linux路径
            font = "/usr/share/fonts/win/msyh.ttf";
        } else if(SystemUtil.macSystem()){
            //mac路径
            font = "/Users/xucan/Downloads/msyh/msyh.ttf";
        }else{
            //windows
            font = "C:\\Windows\\Fonts\\simsun.ttc,0";
        }

        BaseFont baseFont = BaseFont.createFont(font, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);//设置基本字体
//开始遍历excel内容并绘制pdf
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            XSSFRow row = sheet.getRow(r);
            if (row != null) {
                for (int c = row.getFirstCellNum(); (c < row.getLastCellNum() || c < colCount) && c > -1; c++) {
                    if (c >= row.getPhysicalNumberOfCells()) {
                        PdfPCell pCell = new PdfPCell(new Phrase(""));
                        pCell.setBorder(0);
                        table.addCell(pCell);
                        continue;
                    }
                    XSSFCell excelCell = row.getCell(c);
                    String value = "";

                    if (excelCell != null) {
                        value = excelCell.toString().trim();
                        if (value != null && value.length() != 0) {
                            String dataFormat = excelCell.getCellStyle().getDataFormatString();//获取excel单元格数据显示样式
                            if (dataFormat != "General" && dataFormat != "@") {
                                try {
                                    String numStyle = getNumStyle(dataFormat);
                                    value = numFormat(numStyle, excelCell.getNumericCellValue());
                                } catch (Exception e) {

                                }
                            }
                        }
                    }

                    XSSFFont excelFont = excelCell.getCellStyle().getFont();
                    Font pdFont = new Font(baseFont, excelFont.getFontHeightInPoints(),
                            excelFont.getBoldweight() == 700 ? Font.BOLD : Font.NORMAL, BaseColor.BLACK);//设置单元格字体

                    PdfPCell pCell = new PdfPCell(new Phrase(value, pdFont));

//                    List<PicturesInfo> info = getAllPictureInfos(r, r, c, c, true);//判断单元格中是否有图片，不支持图片跨单元格
//                    if (info.size() > 0) {
//                        PdfPCell pdfCell = new PdfPCell(Image.getInstance(info.get(0).getPictureData()));
//                    }
                    boolean hasBorder = hasBorder(excelCell);
                    if (!hasBorder) {
                        pCell.setBorder(0);
                    }
                    pCell.setHorizontalAlignment(getHorAglin(excelCell.getCellStyle().getAlignment()));
                    pCell.setVerticalAlignment(getVerAglin(excelCell.getCellStyle().getVerticalAlignment()));

                    pCell.setMinimumHeight(row.getHeightInPoints());
                    if (isMergedRegion(sheet, r, c)) {
                        int[] span = getMergedSpan(sheet, r, c);
                        if (span[0] == 1 && span[1] == 1) {//忽略合并过的单元格
                            continue;
                        }
                        pCell.setRowspan(span[0]);
                        pCell.setColspan(span[1]);
                        c = c + span[1] - 1;//合并过的列直接跳过
                    }

                    table.addCell(pCell);

                }
            } else {
                PdfPCell pCell = new PdfPCell(new Phrase(""));
                pCell.setBorder(0);
                pCell.setMinimumHeight(13);
                table.addCell(pCell);
            }
        }
        document.add(table);
        document.close();

        byte[] pdfByte = stream.toByteArray();
        stream.close();

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(pdfByte);
        outputStream.flush();
        outputStream.close();

        return file;

    }

    public static File Excel2003(InputStream in,File file) throws Exception {

        HSSFWorkbook workBook = new HSSFWorkbook(in);
        HSSFSheet sheet = workBook.getSheetAt(0);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4);//设置pdf纸张大小
        PdfWriter writer = PdfWriter.getInstance(document, stream);
        document.setMargins(0, 0, 15, 15);//设置页边距
        document.open();
        float[] widths = getColWidthHSS(sheet);//获取excel每列宽度占比

        PdfPTable table = new PdfPTable(widths);//初始化pdf中每列的宽度
        table.setWidthPercentage(88);
        int colCount = widths.length;
        String font = "";
        if (SystemUtil.currSystem()) {
            //linux路径
            font = "/usr/share/fonts/win/msyh.ttf";
        } else if(SystemUtil.macSystem()){
            //mac路径
            font = "/Users/xucan/Downloads/msyh/msyh.ttf";
        }else{
            //windows
            font = "C:\\Windows\\Fonts\\simsun.ttc,0";
        }

        BaseFont baseFont = BaseFont.createFont(font, BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);//设置基本字体
//开始遍历excel内容并绘制pdf
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            HSSFRow row = sheet.getRow(r);
            if (row != null) {
                for (int c = row.getFirstCellNum(); (c < row.getLastCellNum() || c < colCount) && c > -1; c++) {
                    if (c >= row.getPhysicalNumberOfCells()) {
                        PdfPCell pCell = new PdfPCell(new Phrase(""));
                        pCell.setBorder(0);
                        table.addCell(pCell);
                        continue;
                    }
                    HSSFCell excelCell = row.getCell(c);
                    String value = "";

                    if (excelCell != null) {
                        value = excelCell.toString().trim();
                        if (value != null && value.length() != 0) {
                            String dataFormat = excelCell.getCellStyle().getDataFormatString();//获取excel单元格数据显示样式
                            if (dataFormat != "General" && dataFormat != "@") {
                                try {
                                    String numStyle = getNumStyle(dataFormat);
                                    value = numFormat(numStyle, excelCell.getNumericCellValue());
                                } catch (Exception e) {

                                }
                            }
                        }
                    }

                    HSSFFont excelFont = excelCell.getCellStyle().getFont(workBook);
                    Font pdFont = new Font(baseFont, excelFont.getFontHeightInPoints(),
                            excelFont.getBoldweight() == 700 ? Font.BOLD : Font.NORMAL, BaseColor.BLACK);//设置单元格字体

                    PdfPCell pCell = new PdfPCell(new Phrase(value, pdFont));

//                    List<PicturesInfo> info = getAllPictureInfos(r, r, c, c, true);//判断单元格中是否有图片，不支持图片跨单元格
//                    if (info.size() > 0) {
//                        PdfPCell pdfCell = new PdfPCell(Image.getInstance(info.get(0).getPictureData()));
//                    }
                    boolean hasBorder = hasBorderHSS(excelCell);
                    if (!hasBorder) {
                        pCell.setBorder(0);
                    }
                    pCell.setHorizontalAlignment(getHorAglin(excelCell.getCellStyle().getAlignment()));
                    pCell.setVerticalAlignment(getVerAglin(excelCell.getCellStyle().getVerticalAlignment()));

                    pCell.setMinimumHeight(row.getHeightInPoints());
                    if (isMergedRegionHSS(sheet, r, c)) {
                        int[] span = getMergedSpanHSS(sheet, r, c);
                        if (span[0] == 1 && span[1] == 1) {//忽略合并过的单元格
                            continue;
                        }
                        pCell.setRowspan(span[0]);
                        pCell.setColspan(span[1]);
                        c = c + span[1] - 1;//合并过的列直接跳过
                    }

                    table.addCell(pCell);

                }
            } else {
                PdfPCell pCell = new PdfPCell(new Phrase(""));
                pCell.setBorder(0);
                pCell.setMinimumHeight(13);
                table.addCell(pCell);
            }
        }
        document.add(table);
        document.close();

        byte[] pdfByte = stream.toByteArray();
        stream.close();

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(pdfByte);
        outputStream.flush();
        outputStream.close();

        return file;

    }

    /**
     * 判断excel单元格是否有边框
     *
     * @param excelCell
     * @return
     */
    private static boolean hasBorder(XSSFCell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop();
        short bottom = excelCell.getCellStyle().getBorderBottom();
        short left = excelCell.getCellStyle().getBorderLeft();
        short right = excelCell.getCellStyle().getBorderRight();
        return top + bottom + left + right > 2;
    }

    private static boolean hasBorderHSS(HSSFCell excelCell) {
        short top = excelCell.getCellStyle().getBorderTop();
        short bottom = excelCell.getCellStyle().getBorderBottom();
        short left = excelCell.getCellStyle().getBorderLeft();
        short right = excelCell.getCellStyle().getBorderRight();
        return top + bottom + left + right > 2;
    }

    /**
     * 获取excel单元格数据显示格式
     *
     * @param dataFormat
     * @return
     * @throws Exception
     */
    private static String getNumStyle(String dataFormat) throws Exception {
        if (dataFormat == null || dataFormat.length() == 0) {
            throw new Exception("");
        }
        if (dataFormat.indexOf("%") > -1) {
            return dataFormat;
        } else {
            return dataFormat.substring(0, dataFormat.length() - 2);
        }

    }

    /**
     * 判断单元格是否是合并单元格
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static boolean isMergedRegion(XSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isMergedRegionHSS(HSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 计算合并单元格合并的跨行跨列数
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private static int[] getMergedSpan(XSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        int[] span = {1, 1};
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (firstColumn == column && firstRow == row) {
                span[0] = lastRow - firstRow + 1;
                span[1] = lastColumn - firstColumn + 1;
                break;
            }
        }
        return span;
    }

    private static int[] getMergedSpanHSS(HSSFSheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        int[] span = {1, 1};
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (firstColumn == column && firstRow == row) {
                span[0] = lastRow - firstRow + 1;
                span[1] = lastColumn - firstColumn + 1;
                break;
            }
        }
        return span;
    }


    /**
     * 获取excel中每列宽度的占比
     *
     * @param sheet
     * @return
     */
    private static float[] getColWidth(XSSFSheet sheet) {
        int rowNum = getMaxColRowNum(sheet);
        XSSFRow row = sheet.getRow(rowNum);
        int cellCount = row.getPhysicalNumberOfCells();
        int[] colWidths = new int[cellCount];
        int sum = 0;

        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            XSSFCell cell = row.getCell(i);
            if (cell != null) {
                colWidths[i] = sheet.getColumnWidth(i);
                sum += sheet.getColumnWidth(i);
            }
        }

        float[] colWidthPer = new float[cellCount];
        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            colWidthPer[i] = (float) colWidths[i] / sum * 100;
        }
        return colWidthPer;
    }

    private static float[] getColWidthHSS(HSSFSheet sheet) {
        int rowNum = getMaxColRowNumHSS(sheet);
        HSSFRow row = sheet.getRow(rowNum);
        int cellCount = row.getPhysicalNumberOfCells();
        int[] colWidths = new int[cellCount];
        int sum = 0;

        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            HSSFCell cell = row.getCell(i);
            if (cell != null) {
                colWidths[i] = sheet.getColumnWidth(i);
                sum += sheet.getColumnWidth(i);
            }
        }

        float[] colWidthPer = new float[cellCount];
        for (int i = row.getFirstCellNum(); i < cellCount; i++) {
            colWidthPer[i] = (float) colWidths[i] / sum * 100;
        }
        return colWidthPer;
    }

    /**
     * 获取excel中列数最多的行号
     *
     * @param sheet
     * @return
     */
    private static int getMaxColRowNum(XSSFSheet sheet) {
        int rowNum = 0;
        int maxCol = 0;
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            XSSFRow row = sheet.getRow(r);
            if (row != null && maxCol < row.getPhysicalNumberOfCells()) {
                maxCol = row.getPhysicalNumberOfCells();
                rowNum = r;
            }
        }
        return rowNum;
    }

    private static int getMaxColRowNumHSS(HSSFSheet sheet) {
        int rowNum = 0;
        int maxCol = 0;
        for (int r = sheet.getFirstRowNum(); r < sheet.getPhysicalNumberOfRows(); r++) {
            HSSFRow row = sheet.getRow(r);
            if (row != null && maxCol < row.getPhysicalNumberOfCells()) {
                maxCol = row.getPhysicalNumberOfCells();
                rowNum = r;
            }
        }
        return rowNum;
    }


    /**
     * excel垂直对齐方式映射到pdf对齐方式
     *
     * @param aglin
     * @return
     */
    private static int getVerAglin(int aglin) {
        switch (aglin) {
            case 1:
                return com.itextpdf.text.Element.ALIGN_MIDDLE;
            case 2:
                return com.itextpdf.text.Element.ALIGN_BOTTOM;
            case 3:
                return com.itextpdf.text.Element.ALIGN_TOP;
            default:
                return com.itextpdf.text.Element.ALIGN_MIDDLE;
        }
    }

    /**
     * excel水平对齐方式映射到pdf水平对齐方式
     *
     * @param aglin
     * @return
     */


    private static int getHorAglin(int aglin) {
        switch (aglin) {
            case 2:
                return com.itextpdf.text.Element.ALIGN_CENTER;
            case 3:
                return com.itextpdf.text.Element.ALIGN_RIGHT;
            case 1:
                return com.itextpdf.text.Element.ALIGN_LEFT;
            default:
                return com.itextpdf.text.Element.ALIGN_CENTER;
        }
    }

    /**
     * 格式化数字
     *
     * @param pattern
     * @param num
     * @return
     */


    private static String numFormat(String pattern, double num) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(num);
    }


}
