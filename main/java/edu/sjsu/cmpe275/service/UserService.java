package edu.sjsu.cmpe275.service;

import edu.sjsu.cmpe275.domain.User;

public interface UserService {
	Iterable<User> listAllUsers();
	 
	User getUserById(Integer id);
	
	User findUserByUsername(String username);
 
	User saveUser(User user);
 
    void deleteUser(Integer id);
    
    User findUserById(Integer id);
}
