package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.*;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.exception.ImportAbortException;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.ExcelDataValidator;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小数据策略（≤ 1k）
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
@RequiredArgsConstructor
public class SmallImportStrategy<T, EI> implements ExcelImportStrategy {
    private final ImportExcelConverter<T, EI> importExcelConverter;
    private final IService4ImportExcel<T> service;
    private final Class<EI> excelImportClass;
    private final boolean validationEnabled;


    @Override
    public ImportResult importExcel(InputStream in, ImportContext context) {
        try (InputStream inputStream = in) {
            processImportData(inputStream, context);
        } catch (IOException e) {
            throw new RuntimeException("导入Excel失败", e);
        }
        return context.toResult();
    }


    /**
     * 处理导入的Excel数据
     *
     * @param inputStream   Excel文件输入流
     * @param importContext 上下文
     */
    protected void processImportData(InputStream inputStream, ImportContext importContext) {
        EasyExcel.read(inputStream, excelImportClass, new PageReadListener<EI>(dataList -> {
            List<EI> validDataList = new ArrayList<>();

            // 数据校验
            if (validationEnabled) {
                List<ImportError> errors = ExcelDataValidator.validateAll(dataList);
                Map<Integer, List<ImportError>> errorMap = errors.stream()
                        .collect(Collectors.groupingBy(ImportError::getRowNumber));
                for (EI ei : dataList) {
                    // 更正当前行数
                    importContext.advanceRow(1);
                    int rowNum = importContext.getRowNum();

                    // 计数: 总数
                    importContext.markTotal();

                    List<ImportError> rowErrors = errorMap.get(rowNum);

                    if (rowErrors != null) {
                        // 如果该行记录有错误
                        importContext.addErrors(rowNum, rowErrors);
                        // 计数: 失败数
                        importContext.markFail();
                        if (!importContext.isAllowPartial()) {
                            throw new ImportAbortException("Validation failed at row " + rowNum);
                        }
                        continue;
                    }

                    validDataList.add(ei);
                }
            } else {
                // 未开启校验, 则认定导入 Excel 的全部数据为有效数据
                validDataList.addAll(dataList);
            }

            // 计数: 校验通过数
            importContext.markValid(validDataList.size());
            if (!validDataList.isEmpty()) {
                // 空集合防御
                List<T> pos = convertImportDataToPO(validDataList);
                // 失败时, 都导入不成功
                if (service.saveBatch(pos)) {
                    // 计数: 成功数
                    importContext.markSuccess(validDataList.size());
                } else {
                    // 计数: 失败数
                    importContext.markFail(validDataList.size());
                }

            }

        }, 500)).sheet().doRead();
    }

    /**
     * 将导入的Excel元素转换为PO对象列表
     *
     * @param dataList Excel数据列表
     * @return PO对象列表
     */
    protected List<T> convertImportDataToPO(List<EI> dataList) {
        List<T> pos = new ArrayList<>();
        for (EI e : dataList) {
            T po = importExcelConverter.toPOFromExcelElement(e);
            pos.add(po);
        }
        return pos;
    }
}
