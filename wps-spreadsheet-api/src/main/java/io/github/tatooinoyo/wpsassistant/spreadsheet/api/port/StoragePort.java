package io.github.tatooinoyo.wpsassistant.spreadsheet.api.port;

import java.io.InputStream;

public interface StoragePort {
    String storage(String filename, InputStream inputStream);
}
