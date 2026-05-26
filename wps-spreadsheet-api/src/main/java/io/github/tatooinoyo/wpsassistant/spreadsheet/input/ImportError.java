package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportError {
    private int rowNumber;
    private String fieldName;
    private String errorMessage;
    private Object originalValue;
}
