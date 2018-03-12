package edu.sjsu.cmpe275.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.domain.Rating;

public interface RatingRepository extends CrudRepository<Rating,Integer> {
	
	Rating findRatingByOrderIdAndMenuItemId(Integer orderId, Integer menuItemId);

	List<Rating> findRatingByMenuItemId(Integer menuItemId);
	
	List<Rating> findRatingByOrderId(Integer orderId);

	@Transactional
	@Modifying
	@Query("DELETE from Rating r where r.menuItemId = ?1")
	List<Rating> deleteRatingByMenuItemId(Integer menuItemId);
}
