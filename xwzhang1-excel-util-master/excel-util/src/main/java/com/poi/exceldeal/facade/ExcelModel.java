package com.poi.exceldeal.facade;

import com.poi.exceldeal.util.ExcelModelRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangxianwen
 * @since 2018/11/28 10:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExcelModel extends ExcelModelRequest {

    @NotBlank(message = "姓名不能为空，")
    @Size(min = 3, max = 5, message = "姓名个数必须在3到5个字之间，")
    private String name;

    @NotNull(message = "年龄不能为空，")
    @Max(value = 100, message = "年龄不能超过100，")
    private Integer age;
}
