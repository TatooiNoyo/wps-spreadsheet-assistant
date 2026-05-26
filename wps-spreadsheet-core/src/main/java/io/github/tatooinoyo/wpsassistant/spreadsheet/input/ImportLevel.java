package io.github.tatooinoyo.wpsassistant.spreadsheet.input;

/**
 * 导入规模等级枚举
 *
 * <p>根据数据量选择不同的导入策略：</p>
 * <ul>
 *   <li>SMALL: 小数据量 (< 1000行)，单次导入</li>
 *   <li>MEDIUM: 中等数据量 (1000-10000行)，分批导入</li>
 *   <li>LARGE: 大数据量 (> 10000行)，流式导入</li>
 * </ul>
 *
 * @author Tatooi Noyo
 * @since v1.3
 */
public enum ImportLevel {
    /** 小数据量，单次导入 */
    SMALL,
    /** 中等数据量，分批导入 */
    MEDIUM,
    /** 大数据量，流式导入 */
    LARGE;
}
