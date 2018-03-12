package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.domain.OrderMenu;

public interface OrderMenuService {
	
	Iterable<OrderMenu> listAllOrderMenus();
	 
	OrderMenu getOrderMenuById(Integer id);
 
	OrderMenu saveOrderMenu(OrderMenu ordermenu);
 
    void deleteOrderMenu(Integer id);
    
    void deleteAll();
    
    List<OrderMenu> getOrderMenusByOrderId(Integer orderid);
}
