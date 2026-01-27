package io.github.tatooinoyo.wpsassistant.spreadsheet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportExcelConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.output.ExportExcelConverter;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IExcelConverter<T, EI, EO> extends
        ImportExcelConverter<T, EI>,
        ExportExcelConverter<T, EO> {

}
