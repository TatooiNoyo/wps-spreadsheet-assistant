package io.github.tatooinoyo.wpsassistant.spreadsheet.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.tatooinoyo.wpsassistant.spreadsheet.IGetImages;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatooi Noyo
 */
@Getter
@Setter
public class TestImportExcel implements IGetImages {
    @NotBlank
    @ExcelProperty("姓名")
    private String name;
    @NotBlank
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty("头像图片")
    private String image;
}
