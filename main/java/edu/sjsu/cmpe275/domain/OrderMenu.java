package edu.sjsu.cmpe275.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ordermenu")
public class OrderMenu {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	private Integer orderid;
	
	private Integer menuid;
	
	private Integer quantity;
	
	private Double rate;

	@ManyToOne(fetch=FetchType.LAZY)//, cascade=CascadeType.ALL)
	@JoinColumn(name="orderid", referencedColumnName = "id",insertable = false, updatable = false)
	@JsonIgnore
	private Order p_order;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="menuid", referencedColumnName = "id",insertable = false, updatable = false)
//	private MenuItem menuinfo;
//	
	
	
	public Order getP_order() {
		return p_order;
	}

	public void setP_order(Order p_order) {
		this.p_order = p_order;
	}
//
//	public MenuItem getMenuinfo() {
//		return menuinfo;
//	}
//
//	public void setMenuinfo(MenuItem menuinfo) {
//		this.menuinfo = menuinfo;
//	}

	public Order getOrder() {
		return p_order;
	}

	public void setOrder(Order p_order) {
		this.p_order = p_order;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public Integer getMenuid() {
		return menuid;
	}

	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
