package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 *
 * @author Emrys
 * content:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PrizeDO extends BaseDO {
    private Long adminId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;

}
