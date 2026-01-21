package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Tatooi Noyo
 */
public class WPSReadListener<T> implements ReadListener<T> {
    /**
     * Default single handle the amount of data
     */
    public static int BATCH_COUNT = 100;
    /**
     * Temporary storage of data
     */
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private int batchCount = BATCH_COUNT;

    private final BiConsumer<List<T>, AnalysisContext> consumer;


    public WPSReadListener(BiConsumer<List<T>, AnalysisContext> consumer) {
        this.consumer = consumer;
    }

    public WPSReadListener(BiConsumer<List<T>, AnalysisContext> consumer, int batchCount) {
        this.batchCount = batchCount;
        this.consumer = consumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);

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
