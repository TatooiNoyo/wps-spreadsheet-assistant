package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
@Setter
@Getter
public class ImportError {
    private int rowNumber;
    private String fieldName;
    private String errorMessage;
    private Object originalValue;
}
