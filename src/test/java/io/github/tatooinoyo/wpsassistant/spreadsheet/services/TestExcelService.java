package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.AbstractExcelService;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestExportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ImportRouterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tatooi Noyo
 */
@Service("testExcelService")
@Slf4j
public class TestExcelService extends AbstractExcelService<TestService, TestPO, TestImportExcel, TestExportExcel> {

    private final TestService testService;
    private final ImageHandler imageHandler;

    @Autowired
    public TestExcelService(TestService testService, ImageHandler imageHandler) {
        super(createRouter(testService, imageHandler), testService, TestConverter.INSTANCE);
        this.testService = testService;
        this.imageHandler = imageHandler;
    }

    /**
     * 创建 ExcelImportRouter
     */
    private static ExcelImportRouter<TestPO, TestImportExcel> createRouter(TestService testService, ImageHandler imageHandler) {
        log.info("创建 TestExcelService 的 ExcelImportRouter");
        return ImportRouterFactory.<TestPO, TestImportExcel>builder()
                .service(testService)
                .excelClass(TestImportExcel.class)
                .converter(TestConverter.INSTANCE)
                .imageHandler(imageHandler)
                .build()
                .toRouter();
    }

    @Override
    protected Map<String, List<String>> getDropdownOptions() {
        HashMap<String, List<String>> dropdownOptions = new HashMap<>();
        ArrayList<String> objects = new ArrayList<>();
        objects.add("张三");
        objects.add("李四");

        dropdownOptions.put("姓名", objects);
        return dropdownOptions;
    }

    @Override
    protected Class<TestImportExcel> getExcelImportClass() {
        return TestImportExcel.class;
    }

    @Override
    protected Class<TestExportExcel> getExcelOutputClass() {
        return TestExportExcel.class;
    }

    @Override
    protected String getFilename() {
        return "导出的文件名";
    }
}
