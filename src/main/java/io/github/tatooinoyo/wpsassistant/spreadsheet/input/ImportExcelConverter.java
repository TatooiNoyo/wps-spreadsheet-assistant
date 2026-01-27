package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

/**
 * @author Tatooi Noyo
 * @since 2026/1/27 10:28
 */
public interface ImportExcelConverter<T,EI> {
    /**
     * 将Excel元素转换为持久化对象
     * @param excelInputElement Excel输入元素
     * @return 持久化对象
     */
    T toPOFromExcelElement(EI excelInputElement);
}
