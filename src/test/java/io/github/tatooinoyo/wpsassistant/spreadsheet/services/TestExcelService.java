package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.AbstractExcelServiceWithImage;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestExportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tatooi Noyo
 */
@Service("testExcelService")
public class TestExcelService extends AbstractExcelServiceWithImage<TestService, TestPO, TestImportExcel, TestExportExcel> {
    public TestExcelService(ExcelImportRouter<TestPO, TestImportExcel> excelImportRouter, TestService service, ImageHandler imageHandler) {
        super(excelImportRouter, service, TestConverter.INSTANCE, imageHandler);
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
