package edu.sjsu.cmpe275.domain;

import java.io.Serializable;

public class RatingId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Integer orderId;
	
	Integer menuItemId;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((menuItemId == null) ? 0 : menuItemId.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RatingId other = (RatingId) obj;
		if (menuItemId == null) {
			if (other.menuItemId != null)
				return false;
		} else if (!menuItemId.equals(other.menuItemId))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		return true;
	}


}
