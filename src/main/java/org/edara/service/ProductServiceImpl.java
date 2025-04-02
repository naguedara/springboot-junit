package org.edara.service;

import org.edara.entity.Product;
import org.edara.exceptions.ProductNotFoundException;
import org.edara.repo.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Override
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product getProduct(long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("Product Not Available with Id ::" + id));

	}

	@Override
	public Product updateProduct(Product product) {
		Product actualProduct = getProduct(product.getId());
		//BeanUtils.copyProperties(product, actualProduct);
		product = updateTheProduct(product, actualProduct);
		product =  productRepository.saveAndFlush(product);
		System.out.println("updated Product ::"+product);
		return product;
	}

	@Override
	public void deleteProduct(long id) {
		productRepository.deleteById(id);
		System.out.println("Id deleted successfully");
	}

	private Product updateTheProduct(Product product, Product actualProduct) {
		actualProduct.setId(product.getId());
		actualProduct.setName(product.getName());
		actualProduct.setPrice(product.getPrice());
		return actualProduct;
	}

	private String getProductName(Product product) {
		return product.getName();
	}

}
