package edu.sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.domain.Order;
import edu.sjsu.cmpe275.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{

	private OrderRepository orderRepository;
	 
	@Autowired
	public void setOrderRepository(OrderRepository orderRepository) {
	    this.orderRepository = orderRepository;
	}

	@Override
	public Iterable<Order> listAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public Order getOrderById(Integer id) {
		return orderRepository.findOne(id);
	}

	@Override
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}

	@Override
	public void deleteOrder(Integer id) {
		orderRepository.delete(id);		
	}

	@Override
	public Iterable<Order> getOrderByUserId(Integer userid) {
		return orderRepository.findByUserId(userid);
	}

	@Override
	public Iterable<Order> getOrderByStatus(Integer status) {
		return orderRepository.findByStatus(status);
	}
	
	@Override
	public void deleteAll() {
		orderRepository.deleteAll();		
	}

}
