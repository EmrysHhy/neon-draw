package com.emrys.neondraw.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class PrizeDTO {
    private Long prizeId;
    private String prizeName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
