package io.github.tatooinoyo.wpsassistant.spreadsheet.api.io;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelInput {
    String getOriginalFilename();

    InputStream getInputStream() throws IOException;

    long getSize();
}
