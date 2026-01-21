package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IStorageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author Tatooi Noyo
 */
public class StorageServiceImpl implements IStorageService {

    @Override
    public String storage(String filename, InputStream inputStream) {
        try {
            String storagePath = System.getProperty("user.dir") + File.separator + "storage";
            File storageDir = new File(storagePath);
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            File targetFile = new File(storageDir, filename);
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return targetFile.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败: " + filename, e);
        }
    }
}
