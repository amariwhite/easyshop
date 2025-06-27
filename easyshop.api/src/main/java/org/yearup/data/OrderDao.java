package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import java.math.BigDecimal;

public interface OrderDao {
    Order create(Order order);
    void createLineItem(OrderLineItem lineItem);
    Order getById(int orderId);

    void addOrderLineItem(int orderId, int productId, int quantity, BigDecimal price);
}
