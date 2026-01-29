package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * 导入结果
 *
 * <p>包含Excel导入的完整结果信息，包括读取行数、校验通过数、保存成功数、</p>
 * <p>失败数以及详细的错误信息列表</p>
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@Getter
@AllArgsConstructor
public class ImportResult {

    private String taskId;
    private ImportStatus status; // SUCCESS / FAIL / PROCESSING

    private int readCount;
    private int validCount;
    private int successCount;
    private int failCount;

    private Map<Integer, List<ImportError>> importErrors;
    private String errorFileId;
}
