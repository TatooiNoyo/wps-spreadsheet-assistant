package io.github.tatooinoyo.wpsassistant.spreadsheet.api.io;

public record ExcelFileDescriptor(
        String fileName,
        String contentType,
        String charset
) {
}
