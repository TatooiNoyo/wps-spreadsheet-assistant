package io.github.tatooinoyo.wpsassistant.spreadsheet.adapter.servlet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.api.SpreadsheetService;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public final class ServletSpreadsheetSupport {
    private ServletSpreadsheetSupport() {
    }

    public static void downloadTemplate(SpreadsheetService spreadsheetService, HttpServletResponse response) {
        spreadsheetService.downloadTemplate(new HttpServletExcelOutput(response));
    }

    public static void exportExcel(SpreadsheetService spreadsheetService, HttpServletResponse response, List<String> ids) {
        spreadsheetService.exportExcel(new HttpServletExcelOutput(response), ids);
    }

    public static void exportLargeData(SpreadsheetService spreadsheetService, HttpServletResponse response, int batchSize) {
        spreadsheetService.exportLargeData(new HttpServletExcelOutput(response), batchSize);
    }
}
