package io.github.tatooinoyo.wpsassistant.spreadsheet.api.io;

import java.io.IOException;
import java.io.OutputStream;

public interface ExcelOutput {
    OutputStream getOutputStream() throws IOException;

    default void applySuggestedHeaders(ExcelFileDescriptor descriptor) {
    }
}
