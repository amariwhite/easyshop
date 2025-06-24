package org.yearup;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.yearup.data.ProductDao;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MySqlProductDao implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public MySqlProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> getByCategoryId(int categoryId) {
        String sql = "SELECT * FROM products WHERE category_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), categoryId);
    }

    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color) {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" AND category_id = ?");
            params.add(categoryId);
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }
        if (color != null && !color.isBlank()) {
            sql.append(" AND color LIKE ?");
            params.add("%" + color + "%");
        }

        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(Product.class), params.toArray());
    }

    @Override
    public List<Product> listByCategoryId(int categoryId) {
        return getByCategoryId(categoryId);
    }

    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Product.class), productId);
    }

    @Override
    public Product create(Product product) {
        String sql = """
            INSERT INTO products (name, price, category_id, description, color, stock, image_url, featured)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getCategoryId(),
                product.getDescription(),
                product.getColor(),
                product.getStock(),
                product.getImageUrl(),
                product.isFeatured()
        );
        return product;
    }

    @Override
    public void update(int productId, Product product) {
        String sql = """
            UPDATE products
            SET name = ?, price = ?, category_id = ?, description = ?, color = ?, stock = ?, image_url = ?, featured = ?
            WHERE product_id = ?
        """;
        jdbcTemplate.update(sql,
                product.getName(),
                product.getPrice(),
                product.getCategoryId(),
                product.getDescription(),
                product.getColor(),
                product.getStock(),
                product.getImageUrl(),
                product.isFeatured(),
                productId
        );
    }

    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        jdbcTemplate.update(sql, productId);
    }
}
