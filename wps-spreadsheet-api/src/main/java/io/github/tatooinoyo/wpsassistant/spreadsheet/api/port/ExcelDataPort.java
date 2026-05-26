package io.github.tatooinoyo.wpsassistant.spreadsheet.api.port;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ExcelDataPort<T> {
    boolean saveBatch(Collection<T> entityList);

    List<T> listByIds(Collection<? extends Serializable> idList);

    default List<T> listByIdCursor(Serializable lastId, int batchSize) {
        throw new UnsupportedOperationException("Large export requires listByIdCursor implementation");
    }

    default Serializable getEntityId(T entity) {
        throw new UnsupportedOperationException("Large export requires getEntityId implementation");
    }
}
