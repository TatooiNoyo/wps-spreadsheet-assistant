package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public class RowWrapper<T> {
    private final T data;
    private final int rowNum; // Excel 行号（1-based）

    public RowWrapper(T data, int rowNum) {
        this.data = data;
        this.rowNum = rowNum;
    }

    public T getData() {
        return data;
    }

    public int getRowNum() {
        return rowNum;
    }
}
