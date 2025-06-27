package org.yearup.data;

import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao
{
    Product create(Product product);
    List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color);
    List<Product> listByCategoryId(int categoryId);
    Product getById(int productId);
    void update(Product product);
    void update(int productId, Product product);
    void delete(int productId);

    List<Product> getByCategoryId(int categoryId);
}
