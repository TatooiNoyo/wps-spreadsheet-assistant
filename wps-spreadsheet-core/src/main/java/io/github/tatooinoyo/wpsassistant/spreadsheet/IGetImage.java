package io.github.tatooinoyo.wpsassistant.spreadsheet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.api.port.ImageReadable;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IGetImage extends ImageReadable {
    /**
     * 获取图片
     * @return 图片路径或URL
     */
    String getImage();
}
