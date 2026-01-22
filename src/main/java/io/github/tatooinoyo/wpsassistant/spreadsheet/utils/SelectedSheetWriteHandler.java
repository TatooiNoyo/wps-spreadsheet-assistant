package io.github.tatooinoyo.wpsassistant.spreadsheet.utils;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.Map;

/**
 * 下拉框写入处理器（基于隐藏 Sheet 和名称管理器实现，解决选项过多的问题）
 *
 * @author Tatooi Noyo
 */
public class SelectedSheetWriteHandler implements SheetWriteHandler {

    private final Map<String, List<String>> dropdownOptions;
    private static final String HIDDEN_SHEET_NAME = "dictionary_hidden";

    public SelectedSheetWriteHandler(Map<String, List<String>> dropdownOptions) {
        this.dropdownOptions = dropdownOptions;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        if (dropdownOptions == null || dropdownOptions.isEmpty()) {
            return;
        }

        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet mainSheet = writeSheetHolder.getSheet();

        // 获取 EasyExcel 解析好的表头属性映射 (Index -> Head)
        Map<Integer, Head> headMap = writeSheetHolder.getExcelWriteHeadProperty().getHeadMap();
        
        // 1. 创建或获取隐藏 Sheet
        Sheet hiddenSheet = workbook.getSheet(HIDDEN_SHEET_NAME);
        if (hiddenSheet == null) {
            hiddenSheet = workbook.createSheet(HIDDEN_SHEET_NAME);
            workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
        }

        DataValidationHelper helper = mainSheet.getDataValidationHelper();
        int hiddenColumnIndex = 0;

        for (Map.Entry<String, List<String>> entry : dropdownOptions.entrySet()) {
            String headName = entry.getKey();
            List<String> options = entry.getValue();

            if (options == null || options.isEmpty()) {
                continue;
            }

            // 寻找匹配该名称的列索引
            Integer targetColumnIndex = null;
            for (Map.Entry<Integer, Head> headEntry : headMap.entrySet()) {
                List<String> headNames = headEntry.getValue().getHeadNameList();
                if (headNames != null && headNames.contains(headName)) {
                    targetColumnIndex = headEntry.getKey();
                    break;
                }
            }

            if (targetColumnIndex == null) {
                continue;
            }

            // 2. 将选项写入隐藏 Sheet 的一列
            for (int i = 0; i < options.size(); i++) {
                Row row = hiddenSheet.getRow(i);
                if (row == null) {
                    row = hiddenSheet.createRow(i);
                }
                Cell cell = row.createCell(hiddenColumnIndex);
                cell.setCellValue(options.get(i));
            }

            // 3. 创建名称管理器 (Named Range)
            String colName = getColumnName(hiddenColumnIndex);
            String nameName = "dict_" + targetColumnIndex + "_" + System.currentTimeMillis() % 10000;
            Name name = workbook.createName();
            name.setNameName(nameName);
            String formula = HIDDEN_SHEET_NAME + "!$" + colName + "$1:$" + colName + "$" + options.size();
            name.setRefersToFormula(formula);

            // 4. 在主 Sheet 设置数据有效性 (下拉框)
            CellRangeAddressList addressList = new CellRangeAddressList(1, 1000, targetColumnIndex, targetColumnIndex);
            DataValidationConstraint constraint = helper.createFormulaListConstraint(nameName);
            DataValidation validation = helper.createValidation(constraint, addressList);
            
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox("输入错误", "请从下拉列表中选择有效选项");

            mainSheet.addValidationData(validation);
            
            hiddenColumnIndex++;
        }
    }

    /**
     * 将列索引转换为 Excel 列名 (0->A, 1->B, 26->AA)
     */
    private String getColumnName(int columnIndex) {
        StringBuilder sb = new StringBuilder();
        while (columnIndex >= 0) {
            sb.insert(0, (char) ('A' + (columnIndex % 26)));
            columnIndex = (columnIndex / 26) - 1;
        }
        return sb.toString();
    }
}
