package io.github.tatooinoyo.wpsassistant.spreadsheet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IMultipartFile {
    /**
     * 获取原始文件名
     * @return 原始文件名
     */
    String getOriginalFilename();

    /**
     * 获取输入流
     * @return 输入流
     * @throws IOException IO异常
     */
    InputStream getInputStream() throws IOException;

    /**
     * 将文件传输到目标位置
     * @param dest 目标文件
     * @throws IOException IO异常
     * @throws IllegalStateException 非法状态异常
     */
    void transferTo(File dest) throws IOException, IllegalStateException;

    /**
     * @return 获取文件大小
     */
    long getSize();

}
