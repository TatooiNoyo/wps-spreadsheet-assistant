package io.github.tatooinoyo.wpsassistant.spreadsheet.api;

import io.github.tatooinoyo.wpsassistant.spreadsheet.api.io.ExcelInput;
import io.github.tatooinoyo.wpsassistant.spreadsheet.api.io.ExcelOutput;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;

import java.util.List;

public interface SpreadsheetService {
    void downloadTemplate(ExcelOutput output);

    ImportResult importExcel(ExcelInput file);

    void exportExcel(ExcelOutput output, List<String> ids);

    void exportLargeData(ExcelOutput output, int batchSize);
}
