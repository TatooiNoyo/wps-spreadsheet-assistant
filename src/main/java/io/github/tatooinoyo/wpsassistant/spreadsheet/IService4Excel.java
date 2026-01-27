package io.github.tatooinoyo.wpsassistant.spreadsheet;

import io.github.tatooinoyo.wpsassistant.spreadsheet.input.IService4ImportExcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * E 代表 实体
 *
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IService4Excel<T> extends IService4ImportExcel<T> {




    /**
     * 根据ID集合批量查询实体列表
     *
     * @param idList ID集合
     * @return 实体列表
     */
    List<T> listByIds(Collection<? extends Serializable> idList);

    /**
     * 基于游标的数据查询，按ID排序
     *
     * @param lastId    上一批次的最后一个ID，首次查询传null
     * @param batchSize 批次大小
     * @return 数据列表
     */
    default List<T> listByIdCursor(Serializable lastId, int batchSize) {
        return new ArrayList<>();
    }

    /**
     * 获取实体的ID
     *
     * @param entity 实体对象
     * @return 实体ID
     */
    default Serializable getEntityId(T entity) {
        return null;
    }
}
