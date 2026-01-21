package io.github.tatooinoyo.wpsassistant.spreadsheet;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

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

        try {
            EasyExcel.read(file.getInputStream(), getExcelImportClass(), new WPSReadListener<EI>((dataList, context) -> {
                ArrayList<T> pos = new ArrayList<>();

                Object custom = context.getCustom();
                Map<String, String> imageMap;
                if (custom instanceof Map customMap) {
                    imageMap = (Map<String, String>) customMap;
                } else {
                    imageMap = imageHandler.initCellImages(context);
                    log.warn("未解析到XML资源中的图片内容");
                }

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
        }


        return isResult;
    }
}
