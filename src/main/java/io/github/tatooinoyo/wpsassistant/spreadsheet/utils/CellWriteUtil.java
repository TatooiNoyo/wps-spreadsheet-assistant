package io.github.tatooinoyo.wpsassistant.spreadsheet.utils;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import jakarta.annotation.Nonnull;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellWriteUtil extends AbstractColumnWidthStyleStrategy {
    private static final int MAX_COLUMN_WIDTH = 255;
    private final Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>(8);

    public CellWriteUtil() {
    }

    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, @Nonnull List<WriteCellData<?>> cellDataList, Cell cell,
                                  Head head,
                                  Integer relativeRowIndex, Boolean isHead) {
        // 判断 是否为表头 || 导出内容是否为空
        boolean needSetWidth = isHead || !cellDataList.isEmpty();
        if (!needSetWidth) {
            return;
        }
        Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(), key -> new HashMap<>(16));
        Integer columnWidth = dataLength(cellDataList, cell, isHead);
        if (columnWidth < 0) {
            return;
        }
        // 超过最大值255时则设置为255
        if (columnWidth > MAX_COLUMN_WIDTH) {
            columnWidth = MAX_COLUMN_WIDTH;
        }
        // 比较该列的宽度，如果比原来的宽度大，则重新设置
        Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
        if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
            maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        // 如果是表头，则返回表头的宽度
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        }
        // 如果是单元格内容，则根据类型返回其内容的宽度
        WriteCellData<?> cellData = cellDataList.get(0);
        CellDataTypeEnum type = cellData.getType();
        if (type == null) {
            return -1;
        }
        return switch (type) {
            case STRING -> cellData.getStringValue().getBytes().length;
            case BOOLEAN -> cellData.getBooleanValue().toString().getBytes().length;
            case NUMBER -> cellData.getNumberValue().toString().getBytes().length;
            default -> -1;
        };
    }
}
