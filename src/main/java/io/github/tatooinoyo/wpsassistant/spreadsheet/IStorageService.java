package io.github.tatooinoyo.wpsassistant.spreadsheet;

import java.io.InputStream;

/**
 * 图片文件存储服务
 *
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IStorageService {
    /**
     * @param filename    文件名称
     * @param inputStream 文件输入流
     * @return 可以是下载地址或序列化后的json字符串, 亦或是其他想存储的字符串数据
     */
    String storage(String filename, InputStream inputStream);
}
