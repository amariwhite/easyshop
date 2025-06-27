package org.yearup.models;
//
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Integer, ShoppingCartItem> items = new HashMap<>();
    private BigDecimal total = BigDecimal.ZERO;

    public Map<Integer, ShoppingCartItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, ShoppingCartItem> items) {
        this.items = items;
        updateTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void updateTotal()
    {
        total = BigDecimal.ZERO;
        for (ShoppingCartItem item : items.values()) {
            total = total.add(item.getLineTotal());
        }
    }

    public void add(ShoppingCartItem item) {
    }
}
