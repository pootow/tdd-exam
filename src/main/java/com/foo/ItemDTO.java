package com.foo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 经过sku聚合而成的 item， 聚合规则参考 {@link ItemService#getBySkuIds(List)}
 */
@Data
public class ItemDTO {
    /**
     * item名称
     */
    private String name;
    /**
     * 货号
     */
    private String artNo;
    /**
     * spu id
     */
    private String spuId;
    /**
     * 全渠道库存之和
     */
    private BigDecimal inventory;
    /**
     * 一款内sku的最低价
     */
    private BigDecimal minPrice;
    /**
     * 一款内sku的最高价
     */
    private BigDecimal maxPrice;
}
