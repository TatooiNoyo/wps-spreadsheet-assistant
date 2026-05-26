package io.github.tatooinoyo.wpsassistant.spreadsheet.entity;

import io.github.tatooinoyo.wpsassistant.spreadsheet.ISetImage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tatooi Noyo
 */
@Getter
@Setter
public class TestPO implements ISetImage {
    private String name;
    private Integer age;
    private String image;
}
