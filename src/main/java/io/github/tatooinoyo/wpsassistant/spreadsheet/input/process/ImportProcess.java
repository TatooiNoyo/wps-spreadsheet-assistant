package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import jakarta.annotation.Nullable;

/**
 * Excel 导入处理器接口
 *
 * <p>定义 Excel 导入过程中的处理逻辑，包括批量处理前、处理中、处理后三个阶段</p>
 * <p>支持链式处理，每个处理器接收前一个处理器的输出作为输入</p>
 *
 * @param <T>  PO (Persistent Object) 类型，最终要保存的数据类型
 * @param <EI> Excel 输入数据类型
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface ImportProcess<T, EI> {
    /**
     * 批量处理前调用
     *
     * @param ctx EasyExcel 分析上下文
     */
    void beforeBatch(AnalysisContext ctx);

    /**
     * 处理每一行数据
     *
     * <p>处理器链式执行，每个 processor 接收前一个的输出作为输入</p>
     *
     * @param row 每条记录，通过 RowWrapper 包裹，包含 Excel 上的实际行号
     * @param po  上一个处理器处理后的结果，可为 null（第一个处理器）
     * @param ctx 导入上下文，包含统计信息、EasyExcel 上下文等
     * @return 处理后的 PO，可为 null（如果该处理器不需要处理）
     */
    @Nullable
    T process(RowWrapper<EI> row, @Nullable T po, ImportContext ctx);

    /**
     * 批量处理后调用
     *
     * @param ctx EasyExcel 分析上下文
     */
    void afterBatch(AnalysisContext ctx);

}
