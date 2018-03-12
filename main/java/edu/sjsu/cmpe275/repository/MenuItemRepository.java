package edu.sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.cmpe275.domain.MenuItem;

public interface MenuItemRepository extends CrudRepository<MenuItem, Integer>{
	
	MenuItem findMenuItemByName(String name);
	
	Iterable<MenuItem> findMenuItemByCategory(Integer category);
}
