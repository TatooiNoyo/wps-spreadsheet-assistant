package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ImportResult {

    private String taskId;
    private ImportStatus status;

    private int readCount;
    private int validCount;
    private int successCount;
    private int failCount;

    private Map<Integer, List<ImportError>> importErrors;
    private String errorFileId;
}
