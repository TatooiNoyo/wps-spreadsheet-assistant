package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ImportRouterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 测试用 ExcelImportRouter 配置
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@Component
@RequiredArgsConstructor
public class TestExcelImportRouter {
    private final TestService testService;
    private final ImageHandler imageHandler;

    @Bean
    public ExcelImportRouter<TestPO, TestImportExcel> make() {
        return ImportRouterFactory.<TestPO, TestImportExcel>builder()
                .service(testService)
                .excelClass(TestImportExcel.class)
                .converter(TestConverter.INSTANCE)
                .imageHandler(imageHandler)
                .build()
                .toRouter();
    }
}
