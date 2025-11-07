package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.AbstractExcelServiceWithImage;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.converter.TestConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestExportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import org.springframework.stereotype.Service;

/**
 * @author Tatooi Noyo
 */
@Service("testExcelService")
public class TestExcelService extends AbstractExcelServiceWithImage<TestService, TestPO, TestImportExcel, TestExportExcel> {
    public TestExcelService(TestService service, ImageHandler imageHandler) {
        super(service, TestConverter.INSTANCE, imageHandler);
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
