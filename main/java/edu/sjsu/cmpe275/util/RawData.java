package edu.sjsu.cmpe275.util;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RawData {
	private Integer userId;
	private String menuOrders;
	
	@JsonFormat(pattern="MM/dd/yyyy HH:mm")
    private Date pickuptime;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMenuOrders() {
		return menuOrders;
	}

	public void setMenuOrders(String menuOrders) {
		this.menuOrders = menuOrders;
	}

	public Date getPickuptime() {
		return pickuptime;
	}

	public void setPickuptime(Date pickuptime) {
		this.pickuptime = pickuptime;
	}
}
