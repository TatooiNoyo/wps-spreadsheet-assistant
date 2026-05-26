package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportExcelConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import jakarta.annotation.Nullable;

/**
 * 转换导入处理器
 *
 * <p>负责将 Excel 行数据转换为 PO 对象</p>
 *
 * @param <T>  PO 类型
 * @param <EI> Excel 输入数据类型
 * @author Tatooi Noyo
 * @since v1.3
 */
public class ConvertImportProcess<T, EI> implements ImportProcess<T, EI> {
    /** Excel 数据转换器 */
    private final ImportExcelConverter<T, EI> importExcelConverter;

    public ConvertImportProcess(ImportExcelConverter<T, EI> importExcelConverter) {
        this.importExcelConverter = importExcelConverter;
    }

    @Override
    public void beforeBatch(AnalysisContext ctx) {

    }

    @Nullable
    @Override
    public T process(RowWrapper<EI> row, @Nullable T po, ImportContext ctx) {
        po = importExcelConverter.toPOFromExcelElement(row.getData());
        return po;
    }

    @Override
    public void afterBatch(AnalysisContext ctx) {

    }
}
