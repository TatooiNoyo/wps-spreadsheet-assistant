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
    /** 小数据量导入策略 */
    private final SmallImportStrategy<T, EI> smallImportStrategy;
    /** 中等数据量导入策略 */
    private final MediumImportStrategy mediumImportStrategy;
    /** 大数据量导入策略 */
    private final LargeImportStrategy largeImportStrategy;
    /** 导入选项 */
    private final ImportOptions importOptions;

    /**
     * 根据文件自动路由到合适的导入策略
     *
     * @param file 上传的 Excel 文件
     * @return 导入结果
     * @throws IOException 文件读取异常
     */
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

    /**
     * 构建导入上下文
     *
     * @return 导入上下文
     */
    public ImportContext buildContext() {

        return ImportContext.builder()
                .taskId(null)
                .allowPartial(importOptions.isAllowPartial())
                .validationEnabled(importOptions.isValidationEnabled())
                .maxErrors(1000)
                .build();
    }


    /**
     * 根据文件大小检测导入级别
     *
     * @param file 上传的 Excel 文件
     * @return 导入级别
     */
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
