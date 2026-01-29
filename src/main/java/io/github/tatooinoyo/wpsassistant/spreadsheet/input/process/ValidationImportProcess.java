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
 * 校验导入处理器
 *
 * <p>使用 Jakarta Validation 注解对导入数据进行校验，</p>
 * <p>如果校验失败则记录错误信息并中止该行处理</p>
 *
 * @param <T>  PO 类型
 * @param <EI> Excel 输入数据类型
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
