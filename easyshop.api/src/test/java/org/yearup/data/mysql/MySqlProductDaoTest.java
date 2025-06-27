package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MySqlProductDaoTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private MySqlProductDao productDao;

    @BeforeEach
    void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        productDao = new MySqlProductDao(dataSource) {
            @Override
            public List<Product> getByCategoryId(int categoryId) {
                return List.of();
            }
        };
    }

    @Test
    void testSearchByCategory() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("product_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Product");
        when(resultSet.getBigDecimal("price")).thenReturn(new BigDecimal("99.99"));
        when(resultSet.getInt("category_id")).thenReturn(1);
        when(resultSet.getString("description")).thenReturn("Test Description");
        when(resultSet.getString("color")).thenReturn("Red");
        when(resultSet.getInt("stock")).thenReturn(10);
        when(resultSet.getBoolean("featured")).thenReturn(false);
        when(resultSet.getString("image_url")).thenReturn("test.jpg");

        // Act
        List<Product> products = productDao.search(1, null, null, null);

        // Assert
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
        verify(preparedStatement).setObject(1, 1);
    }

    @Test
    void testSearchByPriceRange() throws SQLException {
        // Arrange
        BigDecimal minPrice = new BigDecimal("25.00");
        BigDecimal maxPrice = new BigDecimal("100.00");

        when(resultSet.next()).thenReturn(false);

        // Act
        List<Product> products = productDao.search(null, minPrice, maxPrice, null);

        // Assert
        assertNotNull(products);
        assertTrue(products.isEmpty());
        verify(preparedStatement).setObject(1, minPrice);
        verify(preparedStatement).setObject(2, maxPrice);
    }

    @Test
    void testSearchByColor() throws SQLException {
        // Arrange
        when(resultSet.next()).thenReturn(false);

        // Act
        List<Product> products = productDao.search(null, null, null, "red");

        // Assert
        assertNotNull(products);
        assertTrue(products.isEmpty());
        verify(preparedStatement).setObject(1, "%red%");
    }
}