package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public abstract class MySqlProductDao extends MySqlDaoBase implements ProductDao {
    public MySqlProductDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, category_id = ?, description = ?, color = ?, stock = ?, image_url = ?, featured = ? WHERE product_id = ?";

        try (Connection conn = getDataSource().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getCategoryId());
            stmt.setString(4, product.getDescription());
            stmt.setString(5, product.getColor());
            stmt.setInt(6, product.getStock());
            stmt.setString(7, product.getImageUrl());
            stmt.setBoolean(8, product.isFeatured());
            stmt.setInt(9, product.getProductId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}