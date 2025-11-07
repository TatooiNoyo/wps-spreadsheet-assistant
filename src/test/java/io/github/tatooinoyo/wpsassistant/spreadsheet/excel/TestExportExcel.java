package io.github.tatooinoyo.wpsassistant.spreadsheet.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Tatooi Noyo
 */
@Getter
@Setter
public class TestExportExcel {
    @NotBlank
    @ExcelProperty("姓名")
    private String name;
    @NotBlank
    @ExcelProperty("年龄")
    private Integer age;
}
