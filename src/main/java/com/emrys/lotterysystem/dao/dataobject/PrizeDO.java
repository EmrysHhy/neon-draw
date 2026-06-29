package com.emrys.lotterysystem.dao.dataobject;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class PrizeDO extends BaseDO {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;

}
