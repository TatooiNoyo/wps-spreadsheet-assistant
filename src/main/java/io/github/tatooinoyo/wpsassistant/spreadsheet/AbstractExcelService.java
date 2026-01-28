package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportError;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.CellWriteUtil;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.RequiredFieldWriteHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.SelectedSheetWriteHandler;
import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * （ 泛型：S 是 基础服务 对象，T 是实体 ）
 *
 * @author Tatooi Noyo
 * @since 1.0
 */
@RequiredArgsConstructor
public abstract class AbstractExcelService<S extends IService4Excel<T>, T, EI, EO> implements IExcelService {

    protected final ExcelImportRouter<T, EI> excelImportRouter;

    /**
     * 基础服务对象
     */
    protected final S service;
    /**
     * Excel转换器
     */
    protected final IExcelConverter<T, EI, EO> excelConverter;

    /**
     * 获取Excel导入类类型
     * @return Excel导入类类型
     */
    abstract protected Class<EI> getExcelImportClass();

    /**
     * 获取Excel输出类类型
     * @return Excel输出类类型
     */
    abstract protected Class<EO> getExcelOutputClass();

    /**
     * @return 文件名, 不包含后缀
     */
    abstract protected String getFilename();

    /**
     * 设置 Excel 文件下载的响应头
     * @param response HTTP响应对象
     * @param filename 文件名（不含扩展名）
     */
    protected void setExcelResponseHeaders(HttpServletResponse response, String filename) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", 
            "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(filename + ".xlsx", StandardCharsets.UTF_8));
    }

    /**
     * 生成模板数据
     * @return 模板数据列表，默认为空列表
     */
    protected List<EI> generateTemplateData() {
        return new ArrayList<>();
    }

    /**
     * 获取下拉框配置：Map<Excel属性名, 选项列表>
     * 子类可重写此方法以提供动态下拉选项，Key 应对应 @ExcelProperty 中的 value 值
     * @return 下拉配置映射
     */
    protected Map<String, List<String>> getDropdownOptions() {
        return new HashMap<>();
    }

    /**
     * 获取必填字段配置：Map<Excel属性名, 是否必填>
     * 子类可重写此方法以声明哪些字段是必填的
     * 必填字段在下载模板时会显示黄色背景标识
     * 默认会扫描EI类上的@NotBlank和@NotNull注解来确定必填字段
     * @return 必填字段配置映射
     */
    protected Map<String, Boolean> getRequiredFields() {
        return getRequiredFieldsByAnnotations(getExcelImportClass());
    }

    /**
     * 通过反射扫描EI类上的@NotBlank和@NotNull注解来获取必填字段配置
     *
     * @param eiClass EI类的Class对象
     * @return 必填字段配置映射
     */
    protected Map<String, Boolean> getRequiredFieldsByAnnotations(Class<EI> eiClass) {
        Map<String, Boolean> requiredFields = new HashMap<>();

        Field[] fields = eiClass.getDeclaredFields();
        for (Field field : fields) {
            // 检查字段上是否有@NotBlank注解
            if (field.isAnnotationPresent(NotBlank.class)) {
                // 获取@ExcelProperty注解的值作为字段名
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty excelProp = field.getAnnotation(ExcelProperty.class);
                    String headName = excelProp.value().length > 0 ? excelProp.value()[0] : field.getName();
                    requiredFields.put(headName, true);
                } else {
                    // 如果没有@ExcelProperty注解，则使用字段名
                    requiredFields.put(field.getName(), true);
                }
            }
            // 检查字段上是否有@NotNull注解
            else if (field.isAnnotationPresent(NotNull.class)) {
                // 获取@ExcelProperty注解的值作为字段名
                if (field.isAnnotationPresent(ExcelProperty.class)) {
                    ExcelProperty excelProp =
                            field.getAnnotation(ExcelProperty.class);
                    String headName = excelProp.value().length > 0 ? excelProp.value()[0] : field.getName();
                    requiredFields.put(headName, true);
                } else {
                    // 如果没有@ExcelProperty注解，则使用字段名
                    requiredFields.put(field.getName(), true);
                }
            }
        }

        return requiredFields;
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        List<EI> importElements = generateTemplateData();
        String fileName = getFilename();
        try {
            setExcelResponseHeaders(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            
            ExcelWriterBuilder writerBuilder = EasyExcel.write(outputStream, getExcelImportClass());
            
            // 注册下拉框处理器
            Map<String, List<String>> dropdownOptions = getDropdownOptions();
            if (!dropdownOptions.isEmpty()) {
                writerBuilder.registerWriteHandler(new SelectedSheetWriteHandler(dropdownOptions));
            }

            // 注册必填字段处理器（只对必填字段应用样式）
            Map<String, Boolean> requiredFields = getRequiredFields();
            if (!requiredFields.isEmpty()) {
                writerBuilder.registerWriteHandler(new RequiredFieldWriteHandler(requiredFields));
            }
            
            writerBuilder.sheet("sheet1").doWrite(importElements);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入错误列表
     */
    protected List<ImportError> importErrors = new ArrayList<>();

    /**
     * 是否在导入时进行数据校验
     * 子类可重写此方法以启用/禁用校验
     * @return 是否启用校验，默认启用
     */
    protected boolean isValidateOnImport() {
        return true;
    }
    
    @Override
    public ImportResult importExcel(IMultipartFile file) {
        try {
            return excelImportRouter.route(file);
        } catch (IOException e) {
            throw new RuntimeException("导入Excel失败", e);

        }
    }

    /**
     * @param file 模拟文件对象
     * @throws RuntimeException 文件检查异常
     */
    protected void prepareCheckFile(IMultipartFile file) {
        if (null == file) {
            throw new RuntimeException("文件不能为空");
        }
        String extName = Optional.of(file).map(IMultipartFile::getOriginalFilename)
                .map(filename -> filename.split("\\."))
                .map(strArr -> {
                    if (strArr.length > 1)
                        return strArr[strArr.length - 1];
                    else
                        return "";
                })
                .orElse(null);
        if (!"xls".equals(extName) && !"xlsx".equals(extName)) {
            throw new RuntimeException("不支持的文件格式");
        }
    }

    /**
     * 查询导出数据并转换为Excel输出对象
     * @param ids 数据ID列表
     * @return Excel输出对象列表
     */
    protected List<EO> queryExportData(List<String> ids) {
        return service.listByIds(ids).stream()
                .map(excelConverter::toExcelFromPO)
                .toList();
    }

    @Override
    public void exportExcel(HttpServletResponse response, @Nonnull List<String> ids) {
        List<EO> outputElements = queryExportData(ids);
        String fileName = getFilename();
        try {
            setExcelResponseHeaders(response, fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, getExcelOutputClass())
                    .registerWriteHandler(new CellWriteUtil())
                    .sheet("sheet1")
                    .doWrite(outputElements);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }

    /**
     * 执行流式导出的核心逻辑
     * @param excelWriter Excel写入器
     * @param writeSheet 写入的Sheet
     * @param batchSize 批次大小
     */
    protected void executeLargeDataExport(ExcelWriter excelWriter, WriteSheet writeSheet, int batchSize) {
        Serializable lastId = null;
        List<T> dataList;

        do {
            dataList = service.listByIdCursor(lastId, batchSize);
            if (dataList != null && !dataList.isEmpty()) {
                List<EO> outputElements = dataList.stream()
                        .map(excelConverter::toExcelFromPO)
                        .toList();

                excelWriter.write(outputElements, writeSheet);

                T lastEntity = dataList.get(dataList.size() - 1);
                lastId = service.getEntityId(lastEntity);
            }
        } while (dataList != null && dataList.size() == batchSize);
    }

    /**
     * 大数据导出实现，使用基于ID游标的查询和流式写入，避免内存溢出
     * 适用于导出大量数据的场景
     * 相比于传统分页，游标方式更加高效且不会出现数据重复或漏查
     *
     * @param response  HTTP响应对象
     * @param batchSize 每次查询的数据量，建议500-1000条
     */
    @Override
    public void exportLargeData(HttpServletResponse response, int batchSize) {
        if (batchSize <= 0) {
            batchSize = 500;
        }

        String fileName = getFilename();
        ExcelWriter excelWriter = null;
        ServletOutputStream outputStream = null;

        try {
            setExcelResponseHeaders(response, fileName);
            outputStream = response.getOutputStream();

            excelWriter = EasyExcel.write(outputStream, getExcelOutputClass())
                    .registerWriteHandler(new CellWriteUtil())
                    .build();

            WriteSheet writeSheet = EasyExcel.writerSheet("sheet1").build();
            executeLargeDataExport(excelWriter, writeSheet, batchSize);

        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("关闭输出流失败", e);
                }
            }
        }
    }
}
