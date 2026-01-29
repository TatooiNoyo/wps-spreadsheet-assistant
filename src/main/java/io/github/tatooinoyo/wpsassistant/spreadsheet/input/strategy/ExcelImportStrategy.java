package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;

import java.io.InputStream;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface ExcelImportStrategy {
    ImportResult importExcel(InputStream in, ImportContext context) throws RuntimeException;
}
