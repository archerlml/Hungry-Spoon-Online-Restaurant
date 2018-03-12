package edu.sjsu.cmpe275.repository;

import org.springframework.data.repository.CrudRepository;
import edu.sjsu.cmpe275.domain.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	User findByUsername(String username);
	User findById(Integer id);
}
