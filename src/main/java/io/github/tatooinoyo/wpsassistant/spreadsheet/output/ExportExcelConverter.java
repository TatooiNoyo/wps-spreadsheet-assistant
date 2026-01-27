package io.github.tatooinoyo.wpsassistant.spreadsheet.output;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface ExportExcelConverter<T, EO> {
    /**
     * 将持久化对象转换为Excel输出对象
     *
     * @param po 持久化对象
     * @return Excel输出对象
     */
    EO toExcelFromPO(T po);
}
