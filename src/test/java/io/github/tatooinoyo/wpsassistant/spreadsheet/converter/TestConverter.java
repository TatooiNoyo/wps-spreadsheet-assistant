package io.github.tatooinoyo.wpsassistant.spreadsheet.converter;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IExcelConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.entity.TestPO;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestExportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.excel.TestImportExcel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Tatooi Noyo
 */
@Mapper
public interface TestConverter extends IExcelConverter<TestPO, TestImportExcel, TestExportExcel> {
    TestConverter INSTANCE = Mappers.getMapper(TestConverter.class);
}
