package com.github.hicolors.best.practices.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * ValidatedModel
 *
 * @author Weichao Li (liweichao0102@gmail.com)
 * @since 2019/11/25
 */
@Data
public class ValidatedModel {

    @NotNull(message = "id 不能为空")
    @Min(value = 10, message = "id 不能小于 10")
    private Long id;

    @NotBlank(message = "name 不能为空")
    @Length(max = 5, message = "name 长度不能超过 5")
    private String name;
}
