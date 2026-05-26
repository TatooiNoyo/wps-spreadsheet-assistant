package io.github.tatooinoyo.wpsassistant.spreadsheet.adapter.servlet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IMultipartFile;
import io.github.tatooinoyo.wpsassistant.spreadsheet.api.SpreadsheetService;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Deprecated
public interface LegacyServletExcelService extends SpreadsheetService {
    default void downloadTemplate(HttpServletResponse response) {
        ServletSpreadsheetSupport.downloadTemplate(this, response);
    }

    default ImportResult importExcel(IMultipartFile file) {
        return importExcel((io.github.tatooinoyo.wpsassistant.spreadsheet.api.io.ExcelInput) file);
    }

    default void exportExcel(HttpServletResponse response, List<String> ids) {
        ServletSpreadsheetSupport.exportExcel(this, response, ids);
    }

    default void exportLargeData(HttpServletResponse response, int batchSize) {
        ServletSpreadsheetSupport.exportLargeData(this, response, batchSize);
    }
}
