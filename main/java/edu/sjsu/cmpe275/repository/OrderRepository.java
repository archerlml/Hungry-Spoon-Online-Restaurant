package edu.sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.cmpe275.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>{
	Iterable<Order> findByUserId(Integer userid);
	
	Iterable<Order> findByStatus(Integer status);
	void deleteAll();
}