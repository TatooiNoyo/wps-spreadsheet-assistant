package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportOptions;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.LargeImportStrategy;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.MediumImportStrategy;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.SmallImportStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Tatooi Noyo
 */
@Component
@RequiredArgsConstructor
public class TestExcelImportRouter {
    private final TestService testService;

    @Bean
    public ExcelImportRouter<TestPO, TestImportExcel> make() {
        SmallImportStrategy<TestPO, TestImportExcel> smallImportStrategy = new SmallImportStrategy<>(TestConverter.INSTANCE,
                testService, TestImportExcel.class, true
        );
        ImportOptions importOptions = ImportOptions.builder().allowPartial(true).build();
        return new ExcelImportRouter<>(smallImportStrategy, new MediumImportStrategy(), new LargeImportStrategy(), importOptions);
    }
}
