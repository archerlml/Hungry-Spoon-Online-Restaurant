package edu.sjsu.cmpe275.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.domain.MenuItem;
import edu.sjsu.cmpe275.repository.MenuItemRepository;

@Service
public class MenuItemServiceImpl implements MenuItemService{
	
	private MenuItemRepository menuItemRepository;
	 
	@Autowired
	public void setMenuItemRepository(MenuItemRepository menuItemRepository) {
	    this.menuItemRepository = menuItemRepository;
	}

	@Override
	public Iterable<MenuItem> listAllMenuItems() {
		return menuItemRepository.findAll();
	}

	@Override
	public MenuItem getMenuItemById(Integer id) {
		return menuItemRepository.findOne(id);
	}

	@Override
	public MenuItem saveMenuItem(MenuItem menuItem) {
		return menuItemRepository.save(menuItem);
	}

	@Override
	public void deleteMenuItem(Integer id) {
		menuItemRepository.delete(id);
	}

	@Override
	public Iterable<MenuItem> getMenuItemByCategory(Integer category) {
		return menuItemRepository.findMenuItemByCategory(category);
	}

	@Override
	public MenuItem getMenuItemByName(String name) {
		return menuItemRepository.findMenuItemByName(name);
	}
}
