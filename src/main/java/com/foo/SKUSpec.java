package com.foo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SKUSpec {
    private SKUType type;
    private String artNo;
    private BigDecimal price;
    private List inventory = new ArrayList();

    public void setType(SKUType type) {
        this.type = type;
    }

    public SKUType getType() {
        return type;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }

    public String getArtNo() {
        return artNo;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<InventorySpec> getInventory() {
        return inventory;
    }

}
