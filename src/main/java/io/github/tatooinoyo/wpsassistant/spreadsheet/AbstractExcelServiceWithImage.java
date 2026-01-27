package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportError;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportResult;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.strategy.ExcelImportRouter;
import io.github.tatooinoyo.wpsassistant.spreadsheet.utils.ExcelDataValidator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
@Slf4j
public abstract class AbstractExcelServiceWithImage<S extends IService4Excel<T>, T extends ISetImages, EI extends IGetImages, EO>
        extends AbstractExcelService<S, T, EI, EO> {
    private final ImageHandler imageHandler;

    public AbstractExcelServiceWithImage(ExcelImportRouter<T, EI> excelImportRouter, S service, IExcelConverter<T, EI, EO> excelConverter, ImageHandler imageHandler) {
        super(excelImportRouter, service, excelConverter);
        this.imageHandler = imageHandler;
    }

    @Override
    public ImportResult importExcel(IMultipartFile file) {
        // TODO 需要将WithImage也变更成Small,Medium,Large三类Strategy
//        AtomicInteger count = new AtomicInteger();
//
//        try {
//            EasyExcel.read(file.getInputStream(), getExcelImportClass(), new WPSReadListener<EI>((dataList, context) -> {
//                ArrayList<T> pos = new ArrayList<>();
//                int startRow = count.get() + 1;
//
//                // 数据校验
//                if (isValidateOnImport()) {
//                    List<ImportError> errors = ExcelDataValidator.validateAll(dataList);
//                    for (int i = 0; i < dataList.size(); i++) {
//                        int rowNum = startRow + i;
//                        for (ImportError error : errors) {
//                            if (error.getRowNumber() == rowNum) {
//                                importErrors.add(error);
//                            }
//                        }
//                    }
//
//                    // 如果有校验错误，跳过保存
//                    if (!errors.isEmpty()) {
//                        return;
//                    }
//                }
//
//                Object custom = context.getCustom();
//                Map<String, String> imageMap;
//                if (custom instanceof Map customMap) {
//                    imageMap = (Map<String, String>) customMap;
//                } else {
//                    log.info("初始化图片资源字典表");
//                    imageMap = imageHandler.initCellImages(context);
//                }
//
//                for (EI e : dataList) {
//
//                    int num = count.getAndIncrement();
//                    String downloadUrl = imageHandler.wpsImageFunToDownloadUrl(imageMap, e.getImage());
//                    T po = excelConverter.toPOFromExcelElement(e);
//                    po.setImage(downloadUrl);
//                    pos.add(po);
//
//                    num++;
//                    count.set(num);
//                }
//
//                service.saveBatch(pos);
//            }, 500)).sheet().doRead();
//            if (count.intValue() == 0) {
//                isResult = false;
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        try {
            return excelImportRouter.route(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
