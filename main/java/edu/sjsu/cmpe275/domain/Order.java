package edu.sjsu.cmpe275.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "`order`")
public class Order {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	

	@Column(name = "userid")
	private Integer userId;
	
	private String menuorders;
	
    private String pickuptime;
    
    private Integer preparetime;
    
    public Integer getPreparetime() {
		return preparetime;
	}
	public void setPreparetime(Integer preparetime) {
		this.preparetime = preparetime;
	}
	private String ordertime;
    
    private String f_start_time;
    
    private String readytime;
    
    @OneToMany(mappedBy="p_order")
    @JsonIgnore
    private List<OrderMenu> menus;
    
    private String email;
    
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name="userid", referencedColumnName = "id",insertable = false, updatable = false)
//    private User userinfo;
//    
//    public User getUserinfo() {
//		return userinfo;
//	}
//	public void setUserinfo(User userinfo) {
//		this.userinfo = userinfo;
//	}
	public List<OrderMenu> getMenus() {
		return menus;
	}
	public void setMenus(List<OrderMenu> menus) {
		this.menus = menus;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getF_start_time() {
		return f_start_time;
	}
	public void setF_start_time(String f_start_time) {
		this.f_start_time = f_start_time;
	}
	public String getReadytime() {
		return readytime;
	}
	public void setReadytime(String readytime) {
		this.readytime = readytime;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	private Integer status;
    
    private Double price;
    
    
    
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userid) {
		this.userId = userid;
	}
	public String getMenuorders() {
		return menuorders;
	}
	public void setMenuorders(String menuorders) {
		this.menuorders = menuorders;
	}
	public String getPickuptime() {
		return pickuptime;
	}
	public void setPickuptime(String pickuptime) {
		this.pickuptime = pickuptime;
	}
}
