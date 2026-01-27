package io.github.tatooinoyo.wpsassistant.spreadsheet;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tatooi Noyo
 */

public class MultipartFileAdaptor implements IMultipartFile {
    private final MultipartFile file;

    public MultipartFileAdaptor(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String getOriginalFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        file.transferTo(dest);
    }

    @Override
    public long getSize() {
        return file.getSize();
    }
}
