package io.github.tatooinoyo.wpsassistant.spreadsheet.input.process;

import com.alibaba.excel.context.AnalysisContext;
import io.github.tatooinoyo.wpsassistant.spreadsheet.IGetImages;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ISetImages;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;

import java.util.Map;

/**
 * @author Tatooi Noyo
 * @since 2026/1/28 15:56
 */
public class ImageImportProcess<T extends ISetImages, EI extends IGetImages> implements ImportProcess<T, EI> {
    protected final ImageHandler imageHandler;

    public ImageImportProcess(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    @Override
    public void beforeBatch(AnalysisContext ctx) {

    }

    @Override
    public void process(EI row, T po, AnalysisContext ctx) {
        Object custom = ctx.getCustom();
        Map<String, String> imageMap;
        if (custom instanceof Map customMap) {
            imageMap = (Map<String, String>) customMap;
        } else {
            imageMap = imageHandler.initCellImages(ctx);
        }
        String downloadUrl = imageHandler.wpsImageFunToDownloadUrl(imageMap, row.getImage());
        po.setImage(downloadUrl);
    }

    @Override
    public void afterBatch(AnalysisContext ctx) {

    }
}
