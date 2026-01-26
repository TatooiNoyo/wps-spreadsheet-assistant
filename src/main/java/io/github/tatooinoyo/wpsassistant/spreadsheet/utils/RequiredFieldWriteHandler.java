package io.github.tatooinoyo.wpsassistant.spreadsheet.utils;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 必填字段样式处理器
 * 在下载模板时为必填字段添加黄色背景色标识
 * 
 * @author Tatooi Noyo
 * @since 1.3.0
 */
public class RequiredFieldWriteHandler implements SheetWriteHandler {

    /**
     * 必填字段集合（Excel列名）
     */
    private final Set<String> requiredFields;

    /**
     * 构造函数
     * @param requiredFields 必填字段集合
     */
    public RequiredFieldWriteHandler(Set<String> requiredFields) {
        this.requiredFields = requiredFields;
    }

    /**
     * 构造函数
     * @param requiredFields 必填字段Map（值为true表示必填）
     */
    public RequiredFieldWriteHandler(Map<String, Boolean> requiredFields) {
        this.requiredFields = requiredFields.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = writeSheetHolder.getSheet();
        
        // 创建黄色背景样式
        CellStyle requiredStyle = workbook.createCellStyle();
        requiredStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        requiredStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        requiredStyle.setBorderTop(BorderStyle.THIN);
        requiredStyle.setBorderBottom(BorderStyle.THIN);
        requiredStyle.setBorderLeft(BorderStyle.THIN);
        requiredStyle.setBorderRight(BorderStyle.THIN);
        
        // 获取表头行
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return;
        }
        
        // 遍历表头单元格，找到必填字段并设置样式
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String headerName = getCellStringValue(cell);
                if (requiredFields.contains(headerName)) {
                    // 设置表头单元格样式
                    cell.setCellStyle(requiredStyle);
                    
                    // 整列设置背景色
                    sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 500);
                }
            }
        }
    }
    
    /**
     * 获取单元格字符串值
     * @param cell 单元格
     * @return 字符串值
     */
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cellType == CellType.FORMULA) {
            return cell.getStringCellValue();
        }
        return "";
    }
}
