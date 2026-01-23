package io.github.tatooinoyo.wpsassistant.spreadsheet;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IExcelConverter<T, EI, EO> {
    /**
     * 将Excel元素转换为持久化对象
     * @param excelInputElement Excel输入元素
     * @return 持久化对象
     */
    T toPOFromExcelElement(EI excelInputElement);

    /**
     * 将持久化对象转换为Excel输出对象
     * @param po 持久化对象
     * @return Excel输出对象
     */
    EO toExcelFromPO(T po);
}
