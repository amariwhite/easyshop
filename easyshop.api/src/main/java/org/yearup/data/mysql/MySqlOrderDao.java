package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }

    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, order.getUserId());
            statement.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getCity());
            statement.setString(5, order.getState());
            statement.setString(6, order.getZip());
            statement.setBigDecimal(7, order.getShippingAmount());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    order.setOrderId(orderId);
                    return order;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void createLineItem(OrderLineItem lineItem) {
        String sql = "INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, lineItem.getOrderId());
            statement.setInt(2, lineItem.getProductId());
            statement.setBigDecimal(3, lineItem.getSalesPrice());
            statement.setInt(4, lineItem.getQuantity());
            statement.setBigDecimal(5, lineItem.getDiscount());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int lineItemId = generatedKeys.getInt(1);
                    lineItem.setOrderLineItemId(lineItemId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getById(int orderId) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, orderId);

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void addOrderLineItem(int orderId, int productId, int quantity, BigDecimal price) {

    }

    private Order mapRow(ResultSet row) throws SQLException {
        Order order = new Order();
        order.setOrderId(row.getInt("order_id"));
        order.setUserId(row.getInt("user_id"));
        order.setDate(row.getTimestamp("date").toLocalDateTime());
        order.setAddress(row.getString("address"));
        order.setCity(row.getString("city"));
        order.setState(row.getString("state"));
        order.setZip(row.getString("zip"));
        order.setShippingAmount(row.getBigDecimal("shipping_amount"));
        return order;
    }
}