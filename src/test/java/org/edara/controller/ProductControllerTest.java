package org.edara.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edara.entity.Product;
import org.edara.exceptions.ProductNotFoundException;
import org.edara.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @MockitoBean
    ProductService productService;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addProduct() throws Exception {
        Product product = new Product(1L, "Laptop", 1200);
        when(productService.addProduct(product)).thenReturn(product);
        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))) // Convert object to JSON
                .andExpect(status().isCreated()) // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").value(1)) // Verify response JSON
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.00));
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        // Mock product
        Product product = new Product(1L, "Laptop", 1200);

        // Mock service behavior for valid ID
        when(productService.getProduct(1L)).thenReturn(product);

        // Perform GET request
        mockMvc.perform(get("/product/1")) // Request to /api/product/1
                .andExpect(status().isFound()) // Expect HTTP 302 FOUND
                .andExpect(jsonPath("$.id").value(1)) // Verify response JSON
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        // Mock service behavior for invalid ID
        when(productService.getProduct(2L)).thenThrow(new RuntimeException("Product not found"));

        // Perform GET request with a non-existent product
        mockMvc.perform(get("/product/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1);
        mockMvc.perform(delete("/product?id=1"))
                .andExpect(status().isOk()) //
                .andExpect(content().string("Deleted"));
    }
}