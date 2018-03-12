package edu.sjsu.cmpe275.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.cmpe275.domain.OrderMenu;

public interface OrderMenuRepository extends CrudRepository<OrderMenu, Integer>{
	void deleteAll();
	List<OrderMenu> findOrderMenuByOrderid(Integer orderid);
}
