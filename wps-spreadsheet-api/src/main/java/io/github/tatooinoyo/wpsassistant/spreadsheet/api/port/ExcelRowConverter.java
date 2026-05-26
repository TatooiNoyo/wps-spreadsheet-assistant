package io.github.tatooinoyo.wpsassistant.spreadsheet.api.port;

public interface ExcelRowConverter<T, EI, EO> {
    T toPOFromExcelElement(EI excelInputElement);

    EO toExcelFromPO(T po);
}
