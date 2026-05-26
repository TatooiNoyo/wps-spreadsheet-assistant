package io.github.tatooinoyo.wpsassistant.spreadsheet.adapter.servlet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.api.io.ExcelFileDescriptor;
import io.github.tatooinoyo.wpsassistant.spreadsheet.api.io.ExcelOutput;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class HttpServletExcelOutput implements ExcelOutput {
    private final HttpServletResponse response;

    public HttpServletExcelOutput(HttpServletResponse response) {
        this.response = response;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    @Override
    public void applySuggestedHeaders(ExcelFileDescriptor descriptor) {
        response.setContentType(descriptor.contentType());
        response.setCharacterEncoding(descriptor.charset());
        response.setHeader(
                "Content-Disposition",
                "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(descriptor.fileName(), StandardCharsets.UTF_8)
        );
    }
}
