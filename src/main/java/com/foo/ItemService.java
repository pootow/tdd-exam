package com.foo;

import java.util.List;

/**
 * ItemService 提供跟item相关的操作
 */
public interface ItemService {
    /**
     * 把传入的sku聚合成item输出<p>
     *
     * <p>聚合规则：
     * <li>对于 SKU 类型为原始商品(ORIGIN)的, 按货号聚合成 ITEM</li>
     * <li>对于 SKU 类型为数字化商品(DIGITAL)的, 按 SPUId 聚合成 ITEM</li>
     *
     * @param skuIds 需要聚合的sku id的列表，不能超过100个
     * @return 经过聚合的 item 的列表，数量最多不超过100个，无需分页
     */
    List<ItemDTO> getBySkuIds(List<String> skuIds);
}
