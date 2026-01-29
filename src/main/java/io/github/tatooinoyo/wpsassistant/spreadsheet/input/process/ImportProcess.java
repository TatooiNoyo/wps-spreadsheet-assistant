package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import jakarta.annotation.Nullable;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface ImportProcess<T, EI> {
    void beforeBatch(AnalysisContext ctx);

    /**
     * 处理row 和 po
     *
     * @param row 非空, 每条记录, 通过 RowWrapper包裹, 会提供Excel上的实际行数
     * @param po  可为空, 如果不需要对它处理
     * @param ctx 导入时的上下文(包含统计, EasyExcel上下文, 规则决定继续 / 中断)
     * @return 处理后的 po, 可以为null(如果该process不需要处理它)
     */
    @Nullable
    T process(RowWrapper<EI> row, @Nullable T po, ImportContext ctx);

    void afterBatch(AnalysisContext ctx);

}
