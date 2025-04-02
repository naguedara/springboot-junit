package org.edara.controller;

import org.edara.entity.Product;
import org.edara.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProductController {

	private final ProductService productService;

	@PostMapping("/product")
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		product = productService.addProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable long id) {
		System.out.println("Product Id " + id);
		try {
			Product product = productService.getProduct(id);
			return ResponseEntity.status(HttpStatus.FOUND).body(product);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}



	@PutMapping("/product")
	public ResponseEntity<Product> getProductById(@RequestBody Product product) {
		product = productService.updateProduct(product);
		return ResponseEntity.status(HttpStatus.OK).body(product);
	}


	@DeleteMapping("/product")
	public ResponseEntity<String> deleteProduct(@RequestParam long id) {
		System.out.println("Deleted product id "+id);
		productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.OK).body("Deleted");
	}

}
