package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;

import java.io.InputStream;

/**
 * 中等数据策略（1k–1w）
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
public class MediumImportStrategy implements ExcelImportStrategy {
    @Override
    public ImportResult importExcel(InputStream in, ImportContext context) {
        return null;
    }
}
