package edu.sjsu.cmpe275.service;

import java.util.List;

import edu.sjsu.cmpe275.domain.Rating;

public interface RatingService {
	
	Rating getRatingByOrderIdAndMenuItemId(Integer orderId, Integer menuItemId);
	
	List<Rating> getRatingsByOrderId(Integer orderId);
	
	List<Rating> getRatingsByMenuItemId(Integer menuItemId);
	
	Rating save(Rating rating);
	
	void delete(Rating rating);
	
	Double getAverageRatingByMenuItemId(Integer menuItemId);
	
	void deleteRatingByMenuItemId(Integer menuItemId);
}
