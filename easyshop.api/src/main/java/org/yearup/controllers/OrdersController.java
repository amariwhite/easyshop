package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
public class OrdersController {

    private OrderDao orderDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;

    @Autowired
    public OrdersController(OrderDao orderDao, ShoppingCartDao shoppingCartDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    @PostMapping("")
    public Order checkout(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Get the user's shopping cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            if (cart.getItems().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
            }

            // Create new order
            Order order = new Order();
            order.setUserId(userId);
            order.setDate(LocalDateTime.now());
            order.setAddress(user.getProfile().getAddress());
            order.setCity(user.getProfile().getCity());
            order.setState(user.getProfile().getState());
            order.setZip(user.getProfile().getZip());
            order.setShippingAmount(cart.getTotal().multiply(new java.math.BigDecimal("0.1"))); // 10% shipping

            // Create the order and get the generated order ID
            Order createdOrder = orderDao.create(order);
            int orderId = createdOrder.getOrderId();

            // Add each cart item as an order line item
            for (ShoppingCartItem item : cart.getItems().values()) {
                orderDao.addOrderLineItem(orderId, item.getProduct().getProductId(),
                        item.getQuantity(), item.getProduct().getPrice());
            }

            // Clear the shopping cart
            shoppingCartDao.clearCart(userId);

            return createdOrder;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}