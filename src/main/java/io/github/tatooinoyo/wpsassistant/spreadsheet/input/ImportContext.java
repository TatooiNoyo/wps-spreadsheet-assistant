package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;

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

    /* ========= 不可变参数（构造期注入） ========= */
    private final String taskId; // LargeStrategy 会生成任务
    private final int batchSize;
    private final boolean allowPartial;
    private final boolean validationEnabled;

    /* ========= 执行期状态 ========= */
    private int readCount; // Excel 读到的行数
    private int validCount; // 校验通过
    private int successCount; //  实际保存成功数
    private int failCount; //  失败数（校验 + 保存）
    private int maxErrors = 1000; // 最大失败记录限制
    private ImportStatus status = ImportStatus.PROCESSING;
    private boolean aborted;

    private String errorFileId = null; // LargeImportStrategy 处理时,使用

    @Nonnull
    private final Map<Integer, List<ImportError>> importErrors = new HashMap<>();

    /* ========= 行为方法 ========= */


    public void markTotal() {
        readCount++;
    }

    public void markValid(int count) {
        validCount += count;
    }
    public void markSuccess(int count) {
        successCount += count;
    }

    public void markFail() {
        failCount++;
    }

    public void markFail(int count) {
        failCount += count;
    }


    public void abort() {
        this.status = ImportStatus.FAIL;
    }

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
