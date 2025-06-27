package org.yearup.models;
//
import java.math.BigDecimal;

public class ShoppingCartItem {

    private Product product;
    private int quantity;
    private int discountPercent = 0;

    public ShoppingCartItem(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    public ShoppingCartItem() {

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getLineTotal()
    {
        if (product == null || product.getPrice() == null)
            return BigDecimal.ZERO;

        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = total.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100));
        return total.subtract(discount);
    }

    public void setDiscountPercent(BigDecimal zero) {
    }
}
