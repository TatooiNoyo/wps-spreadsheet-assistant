package io.github.tatooinoyo.wpsassistant.spreadsheet.services;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IStorageService;

import java.io.InputStream;

/**
 * @author Tatooi Noyo
 */
public class StorageServiceImpl implements IStorageService {

    @Override
    public String storage(String filename, InputStream inputStream) {
        return "存储后的下载地址";
    }
}
