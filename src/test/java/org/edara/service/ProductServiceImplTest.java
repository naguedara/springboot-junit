package org.edara.service;

import org.edara.entity.Product;
import org.edara.exceptions.ProductNotFoundException;
import org.edara.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void addProductTest() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mobile");
        when(productRepository.save(product)).thenReturn(product);
        product = productService.addProduct(product);
        assertEquals(1, product.getId());
    }

    @Test
    public void getProductTest() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mobile");
        long id=1;
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        product = productService.getProduct(1);
        assertEquals(1, product.getId());
    }

    @Test
    public void exception_Handling_getProductNotFoundTest() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mobile");
        long id=1;
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(1); });
        assertEquals("Product Not Available with Id ::"+id,productNotFoundException.getMessage());
    }

    @Test
    public void updateProductTest() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mobile");
        when(productRepository.saveAndFlush(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        product = productService.updateProduct(product);
        assertEquals(1, product.getId());
    }


    @Test
    public void voidMethodAccess_deleteProductTest() {
        Product product = new Product();
        product.setId(1);
        product.setName("Mobile");
        doNothing().when(productRepository).deleteById(product.getId());
        productService.deleteProduct(product.getId());
        verify(productRepository,times(1)).deleteById(product.getId());
    }


    @Test
    public void privateMethodAccess_getProductNameTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //ProductServiceImpl
        Product product = new Product();
        product.setName("Nagesh");
        Method method = ProductServiceImpl.class.getDeclaredMethod("getProductName",Product.class);
        method.setAccessible(true);
        String value = (String) method.invoke(productService, product);
        assertEquals("Nagesh", value);
    }
}
