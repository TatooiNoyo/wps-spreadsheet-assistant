package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public AbstractExcelServiceWithImage(S service, IExcelConverter<T, EI, EO> excelConverter, ImageHandler imageHandler) {
        super(service, excelConverter);
        this.imageHandler = imageHandler;
    }

    @Override
    public boolean importExcel(HttpServletResponse response, IMultipartFile file) {


        boolean isResult = true;


        AtomicInteger count = new AtomicInteger();

        // 保存至临时文件
        File tmp = null;
        try {
            tmp = File.createTempFile("import-excel-", ".tmp");
            file.transferTo(tmp);

            Map<String, String> imageMap = imageHandler.loadImagesFromWPSSpreadsheetAndConvertToMap(tmp);
            EasyExcel.read(tmp, getExcelImportClass(), new PageReadListener<EI>(dataList -> {
                ArrayList<T> pos = new ArrayList<>();

                for (EI e : dataList) {

                    int num = count.getAndIncrement();
                    String downloadUrl = imageHandler.wpsImageFunToDownloadUrl(imageMap, e.getImage());
                    T po = excelConverter.toPOFromExcelElement(e);
                    po.setImage(downloadUrl);
                    pos.add(po);

                    num++;
                    count.set(num);
                }

                service.saveBatch(pos);
            }, 500)).sheet().doRead();
            if (count.intValue() == 0) {
                isResult = false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tmp != null) {
                if (!tmp.delete()) {
                    log.warn("删除失败: {}", tmp.getAbsolutePath());
                }
            }

        }


        return isResult;
    }
}
