package edu.sjsu.cmpe275.service;

import edu.sjsu.cmpe275.domain.Product;

public interface ProductService {
	Iterable<Product> listAllProducts();
	 
    Product getProductById(Integer id);
 
    Product saveProduct(Product product);
 
    void deleteProduct(Integer id);
}
