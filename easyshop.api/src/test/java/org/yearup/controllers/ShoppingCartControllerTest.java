package org.yearup.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartDao shoppingCartDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ProductDao productDao;

    @Mock
    private Principal principal;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private Product testProduct;
    private ShoppingCart testCart;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shoppingCartController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));

        testCart = new ShoppingCart();
        Map<String, ShoppingCartItem> items = new HashMap<>();
        ShoppingCartItem item = new ShoppingCartItem(testProduct, 2);
        items.put("1", item);
        testCart.setItems(items);
        testCart.setTotal(new BigDecimal("199.98"));
    }

    @Test
    void getCart_ShouldReturnCart_WhenUserIsAuthenticated() throws Exception {
        // Arrange
        when(principal.getName()).thenReturn("testuser");
        when(userDao.getByUserName("testuser")).thenReturn(testUser);
        when(shoppingCartDao.getByUserId(1)).thenReturn(testCart);

        // Act & Assert
        mockMvc.perform(get("/cart")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(199.98))
                .andExpect(jsonPath("$.items.1.quantity").value(2));

        verify(shoppingCartDao).getByUserId(1);
    }

    @Test
    void addToCart_ShouldCreateNewItem_WhenProductNotInCart() throws Exception {
        // Arrange
        when(principal.getName()).thenReturn("testuser");
        when(userDao.getByUserName("testuser")).thenReturn(testUser);
        when(productDao.getById(1)).thenReturn(testProduct);
        when(shoppingCartDao.getCartItem(1, 1)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/cart/products/1")
                        .principal(principal))
                .andExpect(status().isCreated());

        verify(shoppingCartDao);
    }
}