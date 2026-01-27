package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

import java.util.Collection;

/**
 * @author Tatooi Noyo
 * @since 2026/1/27 10:35
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
