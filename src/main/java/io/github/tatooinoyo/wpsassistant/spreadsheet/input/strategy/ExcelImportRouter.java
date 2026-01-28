package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IMultipartFile;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportLevel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportOptions;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

/**
 * 同一结构的Excel导入, 对应一个router实例
 *
 * @author Tatooi Noyo
 * @since 1.3
 */
@RequiredArgsConstructor
public class ExcelImportRouter<T, EI> {
    private final SmallImportStrategy<T, EI> smallImportStrategy;
    private final MediumImportStrategy mediumImportStrategy;
    private final LargeImportStrategy largeImportStrategy;
    private final ImportOptions importOptions;

    public ImportResult route(IMultipartFile file) throws IOException {

        ImportLevel level = detectLevel(file);

        ExcelImportStrategy strategy = switch (level) {
            case SMALL -> smallImportStrategy;
            case MEDIUM -> mediumImportStrategy;
            case LARGE -> largeImportStrategy;
        };

        try (InputStream in = file.getInputStream()) {
            return strategy.importExcel(in, buildContext());
        }
    }

    public ImportContext buildContext() {

        return ImportContext.builder()
                .taskId(null)
                .allowPartial(importOptions.isAllowPartial())
                .validationEnabled(importOptions.isValidationEnabled())
                .build();
    }


    private ImportLevel detectLevel(IMultipartFile file) {

        long size = file.getSize(); // bytes

        // ⚠️ 经验值，可按业务调整
        if (size <= 1024 * 1024) {          // ≤ 1MB
            return ImportLevel.SMALL;
        }

        if (size <= 10 * 1024 * 1024) {         // ≤ 10MB
            return ImportLevel.MEDIUM;
        }

        return ImportLevel.LARGE;
    }
}
