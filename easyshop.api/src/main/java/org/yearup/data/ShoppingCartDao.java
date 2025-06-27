package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
;   void addOrIncrement(int id, int productId);
    void updateQuantity(int userId, int productId, int quantity);
    void clear(int userId);

    void clearCart(int userId);
}


