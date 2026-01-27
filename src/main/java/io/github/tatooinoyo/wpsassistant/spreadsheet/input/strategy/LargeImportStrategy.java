package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;

import java.io.InputStream;

/**
 * 大数据策略（1w+）
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
public class LargeImportStrategy implements ExcelImportStrategy {
    @Override
    public ImportResult importExcel(InputStream in, ImportContext context) {
        return null;
    }
}
