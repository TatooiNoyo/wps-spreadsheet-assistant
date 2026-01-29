package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

/**
 * Excel 行数据包装器
 *
 * <p>将Excel读取的原始数据与其行号进行关联，便于错误定位</p>
 *
 * @param <T> 数据类型
 * @author Tatooi Noyo
 * @since v1.3
 */
public class RowWrapper<T> {
    /** 包装的数据 */
    private final T data;
    /** Excel 行号（从1开始计数） */
    private final int rowNum;

    public RowWrapper(T data, int rowNum) {
        this.data = data;
        this.rowNum = rowNum;
    }

    /**
     * 获取包装的数据
     * @return 数据对象
     */
    public T getData() {
        return data;
    }

    /**
     * 获取 Excel 行号
     * @return 行号（从1开始）
     */
    public int getRowNum() {
        return rowNum;
    }
}
