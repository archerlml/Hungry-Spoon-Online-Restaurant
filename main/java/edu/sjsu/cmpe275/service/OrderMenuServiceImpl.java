package edu.sjsu.cmpe275.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.domain.OrderMenu;
import edu.sjsu.cmpe275.repository.OrderMenuRepository;

@Service
public class OrderMenuServiceImpl implements OrderMenuService{

	private OrderMenuRepository orderMenuRepository;
	
	@Autowired
	public void setOrderMenuRepository(OrderMenuRepository orderMenuRepository) {
	    this.orderMenuRepository = orderMenuRepository;
	}
	
	@Override
	public Iterable<OrderMenu> listAllOrderMenus() {
		return orderMenuRepository.findAll();
	}

	@Override
	public OrderMenu getOrderMenuById(Integer id) {
		return orderMenuRepository.findOne(id);
	}

	@Override
	public OrderMenu saveOrderMenu(OrderMenu ordermenu) {
		return orderMenuRepository.save(ordermenu);
	}

	@Override
	public void deleteOrderMenu(Integer id) {
		orderMenuRepository.delete(id);
	}
	
	@Override
	public void deleteAll() {
		orderMenuRepository.deleteAll();
	}
	@Override
	public List<OrderMenu> getOrderMenusByOrderId(Integer orderid) {
		List<OrderMenu> ordermenus = orderMenuRepository.findOrderMenuByOrderid(orderid);
		return ordermenus;
	}

}
