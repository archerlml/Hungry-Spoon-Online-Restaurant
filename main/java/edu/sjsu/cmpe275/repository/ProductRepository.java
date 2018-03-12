package edu.sjsu.cmpe275.repository;

import edu.sjsu.cmpe275.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}