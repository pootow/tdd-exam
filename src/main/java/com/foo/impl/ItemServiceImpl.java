package com.foo.impl;

import com.foo.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    private SkuService skuService;
    private PriceService priceService;
    private InventoryService inventoryService;

    public ItemServiceImpl(SkuService skuService, PriceService priceService, InventoryService inventoryService) {
        this.skuService = skuService;
        this.priceService = priceService;
        this.inventoryService = inventoryService;
    }

    @Override
    public List<ItemDTO> getBySkuIds(List<String> skuIds) {
        SkuInfoDTO sku = skuService.findByIds(skuIds).get(0);
        ItemDTO item = new ItemDTO();
        item.setName(sku.getName());
        item.setArtNo(sku.getArtNo());
        item.setSpuId(sku.getSpuId());

        item.setInventory(getAllInventory(sku));

        updatePrice(item, sku);

        List<ItemDTO> items = new ArrayList<>();
        items.add(item);
        return items;
    }

    private void updatePrice(ItemDTO item, SkuInfoDTO sku) {
        BigDecimal price = getSKUPrice(sku);
        if (null == item.getMinPrice()) {
            item.setMinPrice(price);
            item.setMaxPrice(price);
        } else {
            item.setMaxPrice(max(price, item.getMaxPrice()));
            item.setMinPrice(min(price, item.getMinPrice()));
        }
    }

    private BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    private BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private BigDecimal getSKUPrice(SkuInfoDTO sku) {
        return priceService.getBySkuId(sku.getId());
    }

    private BigDecimal getAllInventory(SkuInfoDTO sku) {
        List<ChannelInventoryDTO> inventory = inventoryService.getBySkuId(sku.getId());
        BigDecimal total = BigDecimal.ZERO;
        for (ChannelInventoryDTO ch : inventory) {
            total = total.add(ch.getInventory());
        }
        return total;
    }
}
