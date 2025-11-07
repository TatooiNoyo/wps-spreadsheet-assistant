package io.github.tatooinoyo.wpsassistant.spreadsheet;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * E 代表 实体
 *
 * @author Tatooi Noyo
 * @since 1.0
 */
public interface IService4Excel<T> {

    boolean saveBatch(Collection<T> entityList);

    List<T> listByIds(Collection<? extends Serializable> idList);
}
