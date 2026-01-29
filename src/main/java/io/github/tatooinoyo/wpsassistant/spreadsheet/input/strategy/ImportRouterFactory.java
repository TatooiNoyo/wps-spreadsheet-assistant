package io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy;

import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.IService4ImportExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportExcelConverter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportOptions;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ConvertImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ImageImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ImportProcess;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.process.ValidationImportProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * ExcelImportRouter 工厂类
 *
 * <p>封装 ExcelImportRouter 的创建逻辑，简化不同业务导入的路由器配置</p>
 * <p>支持灵活的处理器链配置，可选是否包含图片处理</p>
 *
 * <p>使用示例 - 不含图片处理：</p>
 * <pre>
 * ImportRouterFactory.<TestPO, TestImportExcel>builder()
 *     .service(testService)
 *     .excelClass(TestImportExcel.class)
 *     .converter(TestConverter.INSTANCE)
 *     .build()
 *     .toRouter();
 * </pre>
 *
 * <p>使用示例 - 含图片处理（需要实现图片接口）：</p>
 * <pre>
 * ImportRouterFactory.<TestPO, TestImageExcel>builder()
 *     .service(testService)
 *     .excelClass(TestImageExcel.class)
 *     .converter(TestConverter.INSTANCE)
 *     .imageHandler(imageHandler)
 *     .build()
 *     .toRouter();
 * </pre>
 *
 * @param <T>  PO (Persistent Object) 类型，需实现 ISetImages 接口才支持图片处理
 * @param <EI> Excel 输入数据类型，需实现 IGetImages 接口才支持图片处理
 * @author Tatooi Noyo
 * @since v1.3
 */
public class ImportRouterFactory<T, EI> {

    /**
     * 导入服务
     */
    private final IService4ImportExcel<T> service;
    /**
     * Excel 数据类型
     */
    private final Class<EI> excelClass;
    /**
     * Excel 到 PO 的转换器
     */
    private final ImportExcelConverter<T, EI> converter;
    /**
     * 图片处理器（可选）
     */
    private final ImageHandler imageHandler;
    /**
     * 导入选项
     */
    private final ImportOptions importOptions;
    /**
     * 自定义处理器列表（可选）
     */
    private final List<ImportProcess<T, EI>> customProcesses;

    private ImportRouterFactory(Builder<T, EI> builder) {
        this.service = builder.service;
        this.excelClass = builder.excelClass;
        this.converter = builder.converter;
        this.imageHandler = builder.imageHandler;
        this.importOptions = builder.importOptions != null ? builder.importOptions :
                ImportOptions.builder().allowPartial(true).validationEnabled(true).build();
        this.customProcesses = builder.customProcesses;
    }

    /**
     * 创建 Builder
     *
     * @param <T>  PO 类型，需实现 ISetImages 接口才支持图片处理
     * @param <EI> Excel 输入数据类型，需实现 IGetImages 接口才支持图片处理
     * @return Builder 实例
     */
    public static <T, EI> Builder<T, EI> builder() {
        return new Builder<>();
    }

    /**
     * 创建 ExcelImportRouter
     *
     * @return 配置好的路由器
     */
    public ExcelImportRouter<T, EI> toRouter() {
        List<ImportProcess<T, EI>> importProcesses = buildProcesses();
        return new ExcelImportRouter<>(
                new SmallImportStrategy<>(service, excelClass, importProcesses),
                new MediumImportStrategy<>(service, excelClass, importProcesses),
                new LargeImportStrategy<>(service, excelClass, importProcesses),
                importOptions
        );
    }

    /**
     * 构建处理器链
     *
     * @return 处理器列表
     */
    private List<ImportProcess<T, EI>> buildProcesses() {
        List<ImportProcess<T, EI>> processes = new ArrayList<>();

        // 1. 校验处理器
        processes.add(new ValidationImportProcess<>());

        // 2. 转换处理器
        if (converter != null) {
            processes.add(new ConvertImportProcess<>(converter));
        }

        // 3. 图片处理器（仅当 imageHandler 不为 null 时添加）
        if (imageHandler != null) {
            processes.add(new ImageImportProcess<>(imageHandler));
        }

        // 4. 自定义处理器
        if (customProcesses != null) {
            processes.addAll(customProcesses);
        }

        return processes;
    }

    /**
     * Builder 类
     *
     * @param <T>  PO 类型
     * @param <EI> Excel 输入数据类型
     */
    public static class Builder<T, EI> {
        private IService4ImportExcel<T> service;
        private Class<EI> excelClass;
        private ImportExcelConverter<T, EI> converter;
        private ImageHandler imageHandler;
        private ImportOptions importOptions;
        private List<ImportProcess<T, EI>> customProcesses;

        public Builder<T, EI> service(IService4ImportExcel<T> service) {
            this.service = service;
            return this;
        }

        public Builder<T, EI> excelClass(Class<EI> excelClass) {
            this.excelClass = excelClass;
            return this;
        }

        public Builder<T, EI> converter(ImportExcelConverter<T, EI> converter) {
            this.converter = converter;
            return this;
        }

        public Builder<T, EI> imageHandler(ImageHandler imageHandler) {
            this.imageHandler = imageHandler;
            return this;
        }

        public Builder<T, EI> importOptions(ImportOptions importOptions) {
            this.importOptions = importOptions;
            return this;
        }

        public Builder<T, EI> customProcesses(List<ImportProcess<T, EI>> customProcesses) {
            this.customProcesses = customProcesses;
            return this;
        }

        public ImportRouterFactory<T, EI> build() {
            return new ImportRouterFactory<>(this);
        }
    }
}
