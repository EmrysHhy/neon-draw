package com.emrys.neondraw.controller.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class CreatePrizeParam implements Serializable {
    @NotBlank(message = "奖品名称不能为空")
    private String prizeName;
    @NotNull(message = "奖品价格不能为空")
    private BigDecimal price;
    private String description;

}
