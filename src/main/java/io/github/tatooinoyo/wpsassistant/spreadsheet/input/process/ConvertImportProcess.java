package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportExcelConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import jakarta.annotation.Nullable;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public class ConvertImportProcess<T, EI> implements ImportProcess<T, EI> {
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
