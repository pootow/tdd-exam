package com.foo.impl;

import com.foo.InventoryService;
import com.foo.ItemDTO;
import com.foo.ItemService;
import com.foo.PriceService;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    private PriceService priceService;
    private InventoryService inventoryService;

    public ItemServiceImpl(PriceService priceService, InventoryService inventoryService) {

        this.priceService = priceService;
        this.inventoryService = inventoryService;
    }

    @Override
    public List<ItemDTO> getBySkuIds(List<String> skuIds) {
        return null;
    }
}
