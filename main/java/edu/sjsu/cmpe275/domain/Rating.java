package edu.sjsu.cmpe275.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@IdClass(RatingId.class)
public class Rating {
	
	@Id
	Integer orderId;
	
	@Id
	Integer menuItemId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn(name="orderId")
//	@JoinColumn(name="orderId")
	Order order;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn(name="menuItemId")
//	@JoinColumn(name="menuItemId")
	MenuItem menuItem;
	
	Integer rating;
	

	Boolean rated;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getMenuItemId() {
		return menuItemId;
	}

	public void setMenuItemId(Integer menuItemId) {
		this.menuItemId = menuItemId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Boolean getRated() {
		return rated;
	}

	public void setRated(Boolean rated) {
		this.rated = rated;
	}

	@Override
	public String toString() {
		return "Rating [orderId=" + orderId + ", menuItemId=" + menuItemId + ", order=" + order + ", menuItem="
				+ menuItem + ", rating=" + rating + ", rated=" + rated + "]";
	}
	
	
	

}
