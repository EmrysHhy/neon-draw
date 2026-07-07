package com.emrys.neondraw.controller.result;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Emrys
 * content:
 */
@Data
public class FindPrizeListResult implements Serializable {
    private Integer total;//总奖品数量
    private List<PrizeInfo> records;//奖品列表

    /**
     * 奖品信息
     */
    @Data
    public static class PrizeInfo implements Serializable {
        private Long prizeId;
        private String prizeName;
        private String description;
        private BigDecimal price;
        private String imageUrl;
    }
}
