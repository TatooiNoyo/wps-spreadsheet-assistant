package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportError;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.exception.ImportAbortException;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.ExcelDataValidator;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public class ValidationImportProcess<T, EI> implements ImportProcess<T, EI> {
    @Override
    public void beforeBatch(AnalysisContext ctx) {

    }

    @Nullable
    @Override
    public T process(RowWrapper<EI> row, @Nullable T po, ImportContext ctx) {

        List<ImportError> importErrors = ExcelDataValidator.validate(row.getData(), row.getRowNum());

        if (!importErrors.isEmpty()) {
            ctx.addErrors(row.getRowNum(), importErrors);
            ctx.markFail();
            ctx.rowAbort();
            if (!ctx.isAllowPartial()) {
                // 如果不允许部分失败
                throw new ImportAbortException("Validation failed at row " + row.getRowNum());
            }
        }

        return null;
    }

    @Override
    public void afterBatch(AnalysisContext ctx) {

    }
}
