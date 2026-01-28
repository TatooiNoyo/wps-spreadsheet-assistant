package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;

/**
 * @author Tatooi Noyo
 * @since 2026/1/28 15:50
 */
public interface ImportProcess<T, EI> {
    void beforeBatch(AnalysisContext ctx);

    void process(EI row, T po, AnalysisContext ctx);

    void afterBatch(AnalysisContext ctx);

}
