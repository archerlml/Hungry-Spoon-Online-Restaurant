package edu.sjsu.cmpe275.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.domain.Rating;
import edu.sjsu.cmpe275.repository.RatingRepository;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepository ratingRepository;
	
	@Override
	public Rating getRatingByOrderIdAndMenuItemId(Integer orderId, Integer menuItemId) {
		return ratingRepository.findRatingByOrderIdAndMenuItemId(orderId, menuItemId);
	}

	@Override
	public List<Rating> getRatingsByOrderId(Integer orderId) {
		return ratingRepository.findRatingByOrderId(orderId);
	}

	@Override
	public List<Rating> getRatingsByMenuItemId(Integer menuItemId) {
		return ratingRepository.findRatingByMenuItemId(menuItemId);
	}

	@Override
	public Rating save(Rating rating) {
		return ratingRepository.save(rating);
	}

	@Override
	public void delete(Rating rating) {
		ratingRepository.delete(rating);
	}

	@Override
	public Double getAverageRatingByMenuItemId(Integer menuItemId) {
		List<Rating> ratings = getRatingsByMenuItemId(menuItemId);
		
		if (ratings == null || ratings.size() == 0) {
			return 0.0;
		}
		Double totalRating = 0.0;
		for (Rating rating : ratings) {
			if (rating.getRated() && rating.getRating() >=0 && rating.getRating() <= 5) {
				totalRating += rating.getRating();
			}
		}
		
		return totalRating / ratings.size();
	}

	@Override
	public void deleteRatingByMenuItemId(Integer menuItemId) {
		ratingRepository.deleteRatingByMenuItemId(menuItemId);
	}

}
