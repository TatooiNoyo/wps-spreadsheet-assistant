package io.github.tatooinoyo.wpsassistant.spreadsheet.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.tatooinoyo.wpsassistant.spreadsheet.IGetImages;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatooi Noyo
 */
@Getter
@Setter
public class TestImportExcel implements IGetImages {
    @NotBlank(message = "姓名不能为空")
    @ExcelProperty("姓名")
    private String name;
    @Min(message = "不能小于18", value = 18)
    @Max(message = "不能大于24", value = 24)
    @ExcelProperty("年龄")
    private Integer age;
    @ExcelProperty("头像图片")
    private String image;
}
