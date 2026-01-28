package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import java.util.Collection;

/**
 * @author Tatooi Noyo
 * @since v1.3
 */
public interface IService4ImportExcel<T> {
    /**
     * 批量保存实体列表
     *
     * @param entityList 实体集合
     * @return 保存是否成功
     */
    boolean saveBatch(Collection<T> entityList);
}
