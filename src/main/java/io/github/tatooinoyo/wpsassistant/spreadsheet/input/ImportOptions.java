package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.Builder;
import lombok.Getter;

/**
 * 预定义的导入选项
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@Getter
@Builder
public class ImportOptions {
    // 是否校验数据
    private boolean validationEnabled = true;
    // 是否允许部分保存
    private boolean allowPartial = true;

}
