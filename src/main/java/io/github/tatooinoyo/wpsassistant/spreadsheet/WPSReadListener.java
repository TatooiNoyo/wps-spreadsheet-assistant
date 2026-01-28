package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Tatooi Noyo
 * @since v1.2
 */
public class WPSReadListener<T> implements ReadListener<T> {
    /**
     * Default single handle the amount of data
     */
    public static int BATCH_COUNT = 100;
    /**
     * Temporary storage of data
     */
    private List<RowWrapper<T>> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private int batchCount = BATCH_COUNT;

    private final BiConsumer<List<RowWrapper<T>>, AnalysisContext> consumer;


    /**
     * 构造函数
     * @param consumer 数据消费函数
     */
    public WPSReadListener(BiConsumer<List<RowWrapper<T>>, AnalysisContext> consumer) {
        this.consumer = consumer;
    }

    /**
     * 构造函数
     * @param consumer 数据消费函数
     * @param batchCount 批次大小
     */
    public WPSReadListener(BiConsumer<List<RowWrapper<T>>, AnalysisContext> consumer, int batchCount) {
        this.batchCount = batchCount;
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        // EasyExcel 的真实行号（0-based，需要 +1）
        int excelRowNum = context.readRowHolder().getRowIndex() + 1;
        cachedDataList.add(new RowWrapper<>(data, excelRowNum));

        if (cachedDataList.size() >= batchCount) {
            consumer.accept(cachedDataList, context);
            cachedDataList = ListUtils.newArrayListWithExpectedSize(batchCount);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(cachedDataList)) {
            consumer.accept(cachedDataList, context);
        }
    }
}
