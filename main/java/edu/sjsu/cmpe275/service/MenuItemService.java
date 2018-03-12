package edu.sjsu.cmpe275.service;

import edu.sjsu.cmpe275.domain.MenuItem;

public interface MenuItemService {
	
	Iterable<MenuItem> listAllMenuItems();
	 
	MenuItem getMenuItemById(Integer id);
	
	Iterable<MenuItem> getMenuItemByCategory(Integer category);
 
	MenuItem saveMenuItem(MenuItem menuItem);
 
    void deleteMenuItem(Integer id);
    
    MenuItem getMenuItemByName(String name);
    
}
