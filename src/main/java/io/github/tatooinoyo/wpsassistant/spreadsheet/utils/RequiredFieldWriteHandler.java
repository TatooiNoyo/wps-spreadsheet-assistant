package io.github.tatooinoyo.wpsassistant.spreadsheet.utils;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 必填字段样式处理器
 * 在下载模板时为必填字段添加黄色背景色标识
 * 
 * @author Tatooi Noyo
 * @since 1.3.0
 */
public class RequiredFieldWriteHandler implements CellWriteHandler {

    /**
     * 必填字段集合（Excel列名）
     */
    private final Set<String> requiredFields;

    /**
     * 构造函数
     * @param requiredFields 必填字段集合
     */
    public RequiredFieldWriteHandler(Set<String> requiredFields) {
        this.requiredFields = requiredFields != null ? requiredFields : new HashSet<>();
    }

    /**
     * 构造函数
     * @param requiredFields 必填字段Map（值为true表示必填）
     */
    public RequiredFieldWriteHandler(Map<String, Boolean> requiredFields) {
        this.requiredFields = requiredFields != null ?
                requiredFields.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                        .collect(java.util.stream.Collectors.toSet())
                : new HashSet<>();
    }

    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        // 只处理表头单元格
        Boolean isHead = context.getHead();
        if (isHead == null || !isHead) {
            return;
        }

        // 获取表头名称
        String headName = getHeadName(context);
        if (headName == null || !requiredFields.contains(headName)) {
            return;
        }

        // 设置表头单元格样式
        WriteCellData<?> cellData = context.getFirstCellData();
        // 这里需要去cellData 获取样式
        // 很重要的一个原因是 WriteCellStyle 和 dataFormatData绑定的 简单的说 比如你加了 DateTimeFormat
        // ，已经将writeCellStyle里面的dataFormatData 改了 如果你自己new了一个WriteCellStyle，可能注解的样式就失效了
        // 然后 getOrCreateStyle 用于返回一个样式，如果为空，则创建一个后返回
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();

        WriteFont writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.RED.getIndex());
        writeFont.setFontName("宋体");
        writeFont.setBold(true);
        writeFont.setFontHeightInPoints((short) 14);
        writeCellStyle.setWriteFont(writeFont);

        setupRequiredStyle(writeCellStyle);
    }

    /**
     * 获取表头名称
     * @param context 单元格写入上下文
     * @return 表头名称
     */
    private String getHeadName(CellWriteHandlerContext context) {
        // 尝试从 cell 获取字符串值
        Cell cell = context.getCell();
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return null;
    }

    /**
     * 设置必填字段样式
     *
     * @param style 需要设置的单元格样式
     */
    private void setupRequiredStyle(WriteCellStyle style) {
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setHorizontalAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

    }

    /**
     * 在 sheet 创建后设置列宽
     *
     * @param writeWorkbookHolder 工作簿持有者
     * @param writeSheetHolder    Sheet持有者
     */
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 可以在此方法中调整列宽
    }
}
