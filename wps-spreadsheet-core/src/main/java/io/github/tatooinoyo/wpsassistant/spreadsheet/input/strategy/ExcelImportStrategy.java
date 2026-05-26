package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;

import java.io.InputStream;

/**
 * Excel 导入策略接口
 *
 * <p>定义不同数据量级别的 Excel 导入策略</p>
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface ExcelImportStrategy {
    /**
     * 导入 Excel 文件
     *
     * @param in Excel 文件输入流
     * @param context 导入上下文
     * @return 导入结果
     * @throws RuntimeException 导入过程中发生错误时抛出
     */
    ImportResult importExcel(InputStream in, ImportContext context) throws RuntimeException;
}
