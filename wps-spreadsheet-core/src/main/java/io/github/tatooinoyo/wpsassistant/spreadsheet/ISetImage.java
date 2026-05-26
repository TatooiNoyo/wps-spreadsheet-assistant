package io.github.tatooinoyo.wpsassistant.spreadsheet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.api.port.ImageWritable;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface ISetImage extends ImageWritable {
    /**
     * 设置图片
     * @param image 图片路径或URL
     */
    void setImage(String image);
}
