package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import com.alibaba.excel.EasyExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.WPSReadListener;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.IService4ImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ImportProcess;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 中等数据策略（1k–1w）
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@RequiredArgsConstructor
public class MediumImportStrategy<T, EI> implements ExcelImportStrategy {
    /** 导入服务 */
    private final IService4ImportExcel<T> service;
    /** Excel 数据类型 */
    private final Class<EI> excelImportClass;
    /** EI to T 中间的过程处理器列表 */
    private final List<ImportProcess<T, EI>> processes;

    @Override
    public ImportResult importExcel(InputStream in, ImportContext context) {
        processImportData(in, context);
        return context.toResult();
    }

    /**
     * 处理导入的Excel数据
     *
     * @param inputStream   Excel 文件输入流
     * @param importContext 上下文
     */
    protected void processImportData(InputStream inputStream, ImportContext importContext) {
        EasyExcel.read(inputStream, excelImportClass, new WPSReadListener<EI>((dataList, context) -> {
            importContext.setAnalysisContext(context);
            List<T> batchPos = new ArrayList<>();

            for (RowWrapper<EI> eiRowWrapper : dataList) {
                // 计数: 总数
                importContext.markTotal();
                importContext.resetRowAborted();

                T po = null;
                for (ImportProcess<T, EI> process : processes) {
                    po = process.process(eiRowWrapper, po, importContext);
                    if (importContext.isRowAborted()) break; // 如果该条记录被放弃
                }
                if (po != null) {
                    // importContext.isRowAborted() 为 true, 代表被抛弃,不需要保存.
                    // 但如果被抛弃, po数据会是null, 故减少冗余判断 importContext.isRowAborted()
                    batchPos.add(po);
                }
            }

            if (service.saveBatch(batchPos)) {
                // 计数: 成功数
                importContext.markSuccess(batchPos.size());
            } else {
                // 计数: 失败数
                importContext.markFail(batchPos.size());
            }

        }, 500)).sheet().doRead();
    }
}
