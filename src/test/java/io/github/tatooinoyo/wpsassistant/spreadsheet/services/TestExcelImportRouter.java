package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportOptions;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ConvertImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ImageImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ValidationImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.LargeImportStrategy;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.MediumImportStrategy;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.SmallImportStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
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
        ArrayList<ImportProcess<TestPO, TestImportExcel>> importProcesses = new ArrayList<>();
        importProcesses.add(new ValidationImportProcess<>());
        importProcesses.add(new ConvertImportProcess<>(TestConverter.INSTANCE));
        importProcesses.add(new ImageImportProcess<>(imageHandler));

        SmallImportStrategy<TestPO, TestImportExcel> smallImportStrategy =
                new SmallImportStrategy<>(testService,
                        TestImportExcel.class, importProcesses

        );
        MediumImportStrategy<TestPO, TestImportExcel> mediumImportStrategy =
                new MediumImportStrategy<>(testService,
                        TestImportExcel.class, importProcesses

                );
        LargeImportStrategy<TestPO, TestImportExcel> largeImportStrategy =
                new LargeImportStrategy<>(testService,
                        TestImportExcel.class, importProcesses

                );
        ImportOptions importOptions = ImportOptions.builder().allowPartial(true).validationEnabled(true).build();
        return new ExcelImportRouter<>(smallImportStrategy, mediumImportStrategy, largeImportStrategy, importOptions);
    }
}
