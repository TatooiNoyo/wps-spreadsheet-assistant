package io.github.tatooinoyo.wpsassistant.spreadsheet;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IExcelService {
    void downloadTemplate(HttpServletResponse response);

    /**
     *
     * @param response
     * @param file 模拟的文件
     * @return
     * @throws RuntimeException 在导入中途出现问题时抛出
     */
    boolean importExcel(HttpServletResponse response, IMultipartFile file);

    void exportExcel(HttpServletResponse response, List<String> ids);
}
