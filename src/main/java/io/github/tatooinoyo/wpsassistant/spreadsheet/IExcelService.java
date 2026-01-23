package io.github.tatooinoyo.wpsassistant.spreadsheet;

import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IExcelService {
    /**
     * 下载Excel模板
     * @param response HTTP响应对象
     */
    void downloadTemplate(HttpServletResponse response);

    /**
     * 导入Excel文件
     * @param response HTTP响应对象
     * @param file 模拟的文件
     * @return 是否导入成功
     * @throws RuntimeException 在导入中途出现问题时抛出
     */
    boolean importExcel(HttpServletResponse response, IMultipartFile file);

    /**
     * 导出Excel文件
     * @param response HTTP响应对象
     * @param ids 数据ID列表
     */
    void exportExcel(HttpServletResponse response, List<String> ids);

    /**
     * 大数据导出方法，使用游标方式进行数据查询和流式写入
     * @param response HTTP响应对象
     * @param batchSize 每批次查询的数据量，建议500-1000
     */
    void exportLargeData(HttpServletResponse response, int batchSize);
}
