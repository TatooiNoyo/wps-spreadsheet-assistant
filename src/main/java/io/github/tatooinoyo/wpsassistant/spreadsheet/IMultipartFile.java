package io.github.tatooinoyo.wpsassistant.spreadsheet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IMultipartFile {
    String getOriginalFilename();

    InputStream getInputStream() throws IOException;

    void transferTo(File dest) throws IOException, IllegalStateException;
}
