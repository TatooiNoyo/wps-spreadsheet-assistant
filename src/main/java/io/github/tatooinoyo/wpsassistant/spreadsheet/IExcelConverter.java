package io.github.tatooinoyo.wpsassistant.spreadsheet;

/**
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IExcelConverter<T, EI, EO> {
    T toPOFromExcelElement(EI excelInputElement);

    EO toExcelFromPO(T po);
}
