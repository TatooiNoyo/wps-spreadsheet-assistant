package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.IGetImages;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ISetImages;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.ImportContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.input.RowWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 图片导入处理器
 *
 * <p>负责将 Excel 中的 WPS 图片转换为可下载的 URL 链接</p>
 *
 * @param <T>  实现了 ISetImages 接口的实体类型
 * @param <EI> 实现了 IGetImages 接口的 Excel 行数据类型
 * @author Tatooi Noyo
 * @since v1.3
 */
@Slf4j
public class ImageImportProcess<T extends ISetImages, EI extends IGetImages> implements ImportProcess<T, EI> {
    /** 图片处理器 */
    protected final ImageHandler imageHandler;

    public ImageImportProcess(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    @Override
    public void beforeBatch(AnalysisContext ctx) {

    }

    @Override
    public T process(RowWrapper<EI> row, T po, ImportContext context) {
        // 图片处理过程, po实体不为null
        if (po == null) {
            log.error("实体不能为 null!");
            context.abort();
            return null;
        }
        // 获取或收集图片关系字典
        AnalysisContext ctx = context.getAnalysisContext();
        Object custom = ctx.getCustom();
        Map<String, String> imageMap;
        if (custom instanceof Map customMap) {
            imageMap = (Map<String, String>) customMap;
        } else {
            imageMap = imageHandler.initCellImages(ctx);
        }
        // 将图片通过 imageHandler 对象存放并返回相关链接
        String downloadUrl = imageHandler.wpsImageFunToDownloadUrl(imageMap, row.getData().getImage());
        po.setImage(downloadUrl);
        return po;
    }

    @Override
    public void afterBatch(AnalysisContext ctx) {

    }
}
