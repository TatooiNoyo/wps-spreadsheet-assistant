package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.Getter;
import lombok.Setter;

/**
 * 导入错误信息
 *
 * <p>用于记录Excel导入过程中每行的校验错误详情</p>
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@Setter
@Getter
public class ImportError {
    private int rowNumber;
    private String fieldName;
    private String errorMessage;
    private Object originalValue;
}
