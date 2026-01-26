package io.github.tatooinoyo.wpsassistant.spreadsheet;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatooi Noyo
 * @since 2026/1/20 16:02
 */
@Setter
@Getter
public class ImportError {
    private int rowNumber;
    private String fieldName;
    private String errorMessage;
    private Object originalValue;
}
