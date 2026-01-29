package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import com.alibaba.excel.context.AnalysisContext;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入上下文
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@Getter
@Builder
public class ImportContext {

    /** 任务ID，LargeImportStrategy 生成 */
    private final String taskId;
    /** 每批处理的数据量 */
    private final int batchSize;
    /** 是否允许部分成功 */
    private final boolean allowPartial;
    /** 是否启用校验 */
    private final boolean validationEnabled;

    /* ========= 执行期状态 ========= */
    /** Excel 读到的行数 */
    private int readCount;
    /** 校验通过的行数 */
    private int validCount;
    /** 实际保存成功数 */
    private int successCount;
    /** 失败数（校验 + 保存） */
    private int failCount;
    /** 最大失败记录限制 */
    private int maxErrors = 1000;
    /** 整体的导入状态 */
    private ImportStatus status = ImportStatus.PROCESSING;
    /** 当前行是否中止 */
    private boolean rowAborted;
    /** 整体是否中止 */
    private boolean aborted;

    /** 错误文件ID，LargeImportStrategy 处理时使用 */
    private String errorFileId = null;

    /** 导入错误集合，按行号索引 */
    @Nonnull
    private final Map<Integer, List<ImportError>> importErrors = new HashMap<>();
    // EasyExcel 读取时的上下文
    @Setter
    private AnalysisContext analysisContext;

    /* ========= 行为方法 ========= */

    /**
     * 标记读取一行数据
     */
    public void markTotal() {
        readCount++;
    }

    /**
     * 标记校验通过的行数
     * @param count 通过的行数
     */
    public void markValid(int count) {
        validCount += count;
    }

    /**
     * 标记保存成功的行数
     * @param count 成功的行数
     */
    public void markSuccess(int count) {
        successCount += count;
    }

    /**
     * 标记失败（失败数+1）
     */
    public void markFail() {
        failCount++;
    }

    /**
     * 标记失败的行数
     * @param count 失败的行数
     */
    public void markFail(int count) {
        failCount += count;
    }

    /**
     * 重置当前行中止状态
     */
    public void resetRowAborted() {
        rowAborted = false;
    }

    /**
     * 中止当前行处理
     */
    public void rowAbort() {
        rowAborted = true;
    }

    /**
     * 中止整个导入过程
     */
    public void abort() {
        this.status = ImportStatus.FAIL;
    }

    /**
     * 添加行的错误信息
     * @param rowNum 行号
     * @param rowErrors 该行的错误列表
     */
    public void addErrors(Integer rowNum, List<ImportError> rowErrors) {
        if (importErrors.size() >= maxErrors) {
            aborted = true;
            return;
        }
        importErrors.put(rowNum, rowErrors);
    }

    /* ========= 只读暴露 ========= */

    public ImportResult toResult() {
        if (aborted) {
            status = ImportStatus.FAIL;
        } else {
            status = ImportStatus.SUCCESS;
        }

        return new ImportResult(taskId, status, readCount, validCount,
                successCount, failCount, importErrors, errorFileId);
    }


}
