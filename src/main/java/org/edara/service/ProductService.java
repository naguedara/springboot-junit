package org.edara.service;

import org.edara.entity.Product;

public interface ProductService {
	public Product addProduct(Product product);

	public Product getProduct(long id);

	public Product updateProduct(Product product);

    void deleteProduct(long id);
}
