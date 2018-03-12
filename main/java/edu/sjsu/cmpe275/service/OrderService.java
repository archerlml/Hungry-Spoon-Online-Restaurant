package edu.sjsu.cmpe275.service;


import edu.sjsu.cmpe275.domain.Order;

public interface OrderService {
	Iterable<Order> listAllOrders();
	 
	Order getOrderById(Integer id);
	
	Iterable<Order> getOrderByUserId(Integer userid);
 
	Order saveOrder(Order order);
 
    void deleteOrder(Integer id);
    
    Iterable<Order> getOrderByStatus(Integer status);
    
    void deleteAll();
    
}
